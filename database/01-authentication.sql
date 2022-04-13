CREATE TABLE account (
    id VARCHAR(25) PRIMARY KEY,
    username VARCHAR (200) NOT NULL UNIQUE,
    email VARCHAR (200) NOT NULL UNIQUE,
    password TEXT,
    is_admin BOOLEAN NOT NULL DEFAULT false,
    is_artist BOOLEAN NOT NULL DEFAULT false,
    avatar_url TEXT,

    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW,
    updated_date TIMESTAMP WITH TIME ZONE DEFAULT NOW
); 