CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(25) PRIMARY KEY,
    username VARCHAR (200) NOT NULL UNIQUE,
    email VARCHAR (200) NOT NULL UNIQUE,
    password TEXT,
    is_admin BOOLEAN NOT NULL DEFAULT false,
    is_artist BOOLEAN NOT NULL DEFAULT false,
    is_verified BOOLEAN NOT NULL DEFAULT false,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS account_profile (
    account_id VARCHAR(25) PRIMARY KEY,
    display_name VARCHAR (200) NOT NULL,
    bio TEXT,
    custom_url TEXT,
    avatar_url TEXT,
    FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE IF NOT EXISTS `session` (
    id VARCHAR (25) PRIMARY KEY,
    payload JSON NOT NULL,
    expires TIMESTAMP
);

CREATE INDEX IF NOT EXISTS session_expires_idx ON session (expires);