CREATE TABLE profile (
                          user_id BIGINT NOT NULL,
                          profile INTEGER NOT NULL,
                          PRIMARY KEY (user_id, profile),
                          FOREIGN KEY (user_id) REFERENCES usuario (id) ON DELETE CASCADE
);
