INSERT INTO users (username, "password", enabled)
    VALUES  ('user', '$2a$10$G5.gdYrGF8cv6QPC59ARDu7d4m1CORmHgIL3lqw8HEsslfQ5GqsrS', true),
            ('admin', '$2a$10$RZjH9OdRT2TsVqHz3QI0s.P5kQzkBZnjGXNqZqAaNFZBitUWnt..G', true);

INSERT INTO authorities (username, authority)
    VALUES  ('user','ROLE_USER'),
            ('admin','ROLE_ADMIN');
