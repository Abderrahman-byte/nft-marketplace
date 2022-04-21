CREATE TABLE IF NOT EXISTS nft_collection (
    id VARCHAR (25) PRIMARY KEY,
    name VARCHAR (200) NOT NULL,
    created_by VARCHAR(25) NOT NULL REFERENCES account (id),
    description TEXT,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS nft_token (
    id VARCHAR (25) PRIMARY KEY,
    title VARCHAR (200) NOT NULL,

    price NUMERIC (9,6) NOT NULL DEFAULT 0,
    artist_id VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    owner_id VARCHAR(25) NOT NULL REFERENCES account (id),
    description TEXT,

    preview_url TEXT NOT NULL,
    is_for_sell BOOLEAN NOT NULL DEFAULT false,
    collection_id VARCHAR(25) REFERENCES nft_collection(id) ON DELETE CASCADE,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS historique_trx (
    id VARCHAR(25) PRIMARY KEY,
    account_from VARCHAR(25) NOT NULL REFERENCES account (id),
    account_to VARCHAR(25) NOT NULL REFERENCES account (id),
    nft_id VARCHAR(25) NOT NULL REFERENCES nft_token(id) ON DELETE CASCADE,
    price NUMERIC (9, 6) NOT NULL DEFAULT 0,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS nft_token_likes (
<<<<<<< HEAD
    token_id VARCHAR(25) NOT NULL REFERENCES nft_token (id) ON DELETE CASCADE,
    account_id VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    
=======
    token_id VARCHAR(25) NOT NULL REFERENCES nft_token (id),
    account_id VARCHAR(25) NOT NULL REFERENCES account (id),

>>>>>>> master
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);