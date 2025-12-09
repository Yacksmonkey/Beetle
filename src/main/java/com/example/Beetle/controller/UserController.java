package com.example.Beetle.controller;

import com.example.Beetle.dto.GoogleAuthRequest;
import com.example.Beetle.model.User;
import com.example.Beetle.model.Role;
import com.example.Beetle.service.UserService;
import com.example.Beetle.security.JwtUtil;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
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
        User created = userService.registerUser(user);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", created.getId()
        ));
    }


    // GOOGLE
    //--------------------------
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest request) {

        String idTokenString = request.getGoogleToken();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        ).setAudience(Collections.singletonList("911683984183-3rhume34n3v2qja16h1fk0p0orbq8a02.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken;

        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Goole Id token verification failed");
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
            user = userService.createGoogleUser(email, name);
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                roles
        );


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


    // UPDATE USER
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
