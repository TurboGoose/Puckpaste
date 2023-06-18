CREATE TABLE IF NOT EXISTS posts
(
    id          INTEGER PRIMARY KEY,
    title       TEXT NOT NULL CHECK (length(title) <= 100),
    description TEXT NOT NULL CHECK (length(description) <= 1500),
    content     TEXT NOT NULL CHECK (length(content) <= 20000),
    expires_at  TEXT NOT NULL,
    created_at  TEXT NOT NULL
);