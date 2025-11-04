-- Create an admin user (password must be BCrypt-hashed). Replace the hash accordingly.
-- Example BCrypt hash for password 'admin123' (generate with Spring or online tool):
-- $2a$10$9f5qkQ8f8m6rS7W8v0y0M.2FQWg0u5YzXG6V9hO5cM8cCwL2e6r5q

INSERT INTO users (username, email, password, role)
VALUES ('admin', 'admin@example.com', '$2a$10$9f5qkQ8f8m6rS7W8v0y0M.2FQWg0u5YzXG6V9hO5cM8cCwL2e6r5q', 'ROLE_ADMIN');


