--liquibase formatted sql

--changeset rybindev:1
CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(64)  NOT NULL UNIQUE,
    password VARCHAR(128) DEFAULT '{noop}123',
    role     VARCHAR(16)
);
