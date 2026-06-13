package com.example.Beetle.controller;

import com.example.Beetle.dto.GoogleAuthRequest;
import com.example.Beetle.model.User;
import com.example.Beetle.model.Role;
import com.example.Beetle.service.UserService;
import com.example.Beetle.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import com.example.Beetle.dto.UserMeResponse;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Beetle.dto.UserUpdateRequest;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    // LOGIN
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody Map<String, String> loginRequest,
            HttpServletResponse response
    )
    {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        User user = userService.authenticate(email, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                roles
        );
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24h

        response.addCookie(cookie);


        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "email", user.getEmail(),
                "roles", roles
        ));
    }


    // REGISTER
    // -------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User created = userService.registerUser(user);

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "userId", created.getId()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }


    // GOOGLE
    //--------------------------
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
            @RequestBody GoogleAuthRequest request,
            HttpServletResponse response
    ) {

        String idTokenString = request.getGoogleToken();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        ).setAudience(Collections.singletonList("911683984183-dvd6d20059jceqmooeqrh7u08l1ob76r.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken;

        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Google Id token verification failed");
        }

        if (idToken == null) {
            return ResponseEntity.status(401).body("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userService.findByEmail(email);
        if (user == null) {
            user = userService.createGoogleUser(email, name, picture);
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                roles
        );

        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24h

        response.addCookie(cookie);



        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "email", user.getEmail(),
                "roles", roles
        ));
    }



    // GET ALL USERS (ADMIN)
    // -------------------------
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }



    // GET USER BY ID
    // -------------------------
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.isPresent()
                ? ResponseEntity.ok(user.get())
                : ResponseEntity.notFound().build();
    }

    // SEARCH USERS
    // -------------------------
    @GetMapping("/users/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> searchUsers(@RequestParam("q") String query) {
        List<User> results = userService.searchUsers(query);
        List<Map<String, Object>> safe = results.stream().map(u -> Map.of(
                "id", u.getId(),
                "name", u.getName(),
                "username", u.getUsername(),
                "picture", u.getPicture()
        )).toList();
        return ResponseEntity.ok(safe);
    }

    //GET ME
    //-------------------------------
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> me(
            @CookieValue(name = "auth_token", required = false) String token
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId = jwtUtil.extractUserId(token);

        return userService.getUserById(userId)
                .map(user -> {
                    List<String> roles = user.getRoles().stream()
                            .map(Role::getName)
                            .toList();

                    return ResponseEntity.ok(
                            new UserMeResponse(
                                    user.getId(),
                                    user.getName(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getPicture(),
                                    user.isPublicProfile(),
                                    user.getPhone(),
                                    user.getAddress(),
                                    user.getBio(),
                                    roles
                            )
                    );
                })
                .orElseGet(() -> ResponseEntity.status(404).build());
    }



    // PUT USER
    // -------------------------
    @PutMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {
        User updated = userService.updateUser(id, user);

        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    // PUT ME
    //----------------------------------------------------
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> updateMe(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestBody UserUpdateRequest request
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId = jwtUtil.extractUserId(token);

        User updated = userService.updateMyProfile(userId, request);

        if (updated == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok().build();
    }


    //UPDATE PROFILE
    //-----------------------------
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> updateMyProfile(
            @RequestHeader("Authorization") String auth,
            @RequestBody User updated
    ) {
        String token = auth.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        User saved = userService.updateProfile(userId, updated);

        return saved != null
                ? ResponseEntity.ok(saved)
                : ResponseEntity.status(404).body("User not found");
    }



    // DELETE USER (ADMIN)
    // -------------------------
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        return deleted
                ? ResponseEntity.ok("User deleted successfully")
                : ResponseEntity.status(404).body("User not found");
    }
}
