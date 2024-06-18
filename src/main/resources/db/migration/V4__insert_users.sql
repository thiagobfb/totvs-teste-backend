CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO usuario (username, password)
VALUES ('admin', crypt('123', gen_salt('bf')));

DO $$
    DECLARE
        admin_id BIGINT;
    BEGIN
        SELECT id INTO admin_id FROM usuario WHERE username = 'admin';

        -- Insert profiles for the "admin" user
        INSERT INTO profile (user_id, profile) VALUES (admin_id, 1);
        INSERT INTO profile (user_id, profile) VALUES (admin_id, 2);
    END $$;

-- Insert the "user" user with encoded password
INSERT INTO usuario (username, password)
VALUES ('user', crypt('123', gen_salt('bf')));

DO $$
    DECLARE
        user_id BIGINT;
    BEGIN
        SELECT id INTO user_id FROM usuario WHERE username = 'user';

        -- Insert profile for the "user" user
        INSERT INTO profile (user_id, profile) VALUES (user_id, 2);
    END $$;
