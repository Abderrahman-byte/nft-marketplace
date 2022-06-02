CREATE TABLE IF NOT EXISTS nft_collection (
    id VARCHAR (25) PRIMARY KEY,
    name VARCHAR (200) NOT NULL UNIQUE,
    created_by VARCHAR(25) NOT NULL REFERENCES account (id),
    description TEXT,
    image_url TEXT,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS token (
    id VARCHAR (25) PRIMARY KEY,
    title VARCHAR (200) NOT NULL UNIQUE,
    artist_id VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    collection_id VARCHAR(25) REFERENCES nft_collection(id) ON DELETE CASCADE,
    preview_url TEXT NOT NULL,
    description TEXT,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

-- When added multiple tokens support it should be a token setting for each owner

CREATE TABLE IF NOT EXISTS token_settings (
    token_id VARCHAR(25) NOT NULL REFERENCES token (id) ON DELETE CASCADE,
    is_for_sale BOOLEAN NOT NULL DEFAULT false,
    instant_sale BOOLEAN NOT NULL DEFAULT false,
    price DOUBLE,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS bids (
    id VARCHAR(25) PRIMARY KEY,
    token_id VARCHAR(25) NOT NULL REFERENCES token (id) ON DELETE CASCADE,
    from_account VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    to_account VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    bid_price DOUBLE NOT NULL,
    offre_response ENUM('ACCEPTED', 'REJECTED', 'PENDING', 'IMPLICITLY_REJECTED') NOT NULL DEFAULT 'PENDING',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR (25) PRIMARY KEY,
    token_id VARCHAR(25) NOT NULL REFERENCES token (id) ON DELETE CASCADE,
    from_account VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    to_account VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    price DOUBLE NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

-- Likes may point to a copy of a token

CREATE TABLE IF NOT EXISTS token_likes (
    token_id VARCHAR(25) NOT NULL REFERENCES token (id) ON DELETE CASCADE,
    account_id VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (token_id, account_id)
);