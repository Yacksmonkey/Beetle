CREATE TABLE preference_cards (
    id UUID PRIMARY KEY,
    label VARCHAR(255),
    card_key VARCHAR(255),
    emoji VARCHAR(50),
    image_url VARCHAR(500),
    parent_key VARCHAR(255),
    level INT,
    active BOOLEAN
);
