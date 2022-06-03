CREATE TABLE notifications (
    id VARCHAR (25) PRIMARY KEY,
    event VARCHAR (200) NOT NULL,
    metadata JSON NOT NULL DEFAULT (JSON_OBJECT()),
    to_account VARCHAR(25) NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    vued BOOLEAN NOT NULL DEFAULT false,

    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);