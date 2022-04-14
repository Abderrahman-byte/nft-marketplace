CREATE TABLE nft_collection (
    id VARCHAR (25) PRIMARY,
    name VARCHAR (200) NOT NULL,
    created_by VARCHAR(25) NOT NULL REFERENCES account (id),
    description TEXT,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW
);

CREATE TABLE nft_token (
    id VARCHAR (25) PRIMARY,
    title VARCHAR (200) NOT NULL,

    price NUMERIC (9,6) NOT NULL DEFAULT 0,
    artist_id VARCHAR(25) NOT NULL REFERENCES account (id),
    owner_id VARCHAR(25) NOT NULL REFERENCES account (id),
    description TEXT,

    preview_url TEXT NOT NULL,
    files_zip_url TEXT ,
    is_for_sall BOOLEAN NOT NULL DEFAULT false,
    collection_id VARCHAR NOT NULL REFERENCES nft_collection(id),

    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW
);

CREATE TABLE historique_trx (
    id VARCHAR PRIMARY,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW,
    account_from VARCHAR NOT NULL REFERENCES account (id),
    account_to VARCHAR NOT NULL REFERENCES account (id),
    nft_id VARCHAR NOT NULL REFERENCES nft_token(id)
);