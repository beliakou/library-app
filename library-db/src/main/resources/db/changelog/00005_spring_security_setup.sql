--liquibase formatted sql
--changeset viktor:spring-security-support splitStatements:true endDelimiter:;

create table users(
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(60) NOT NULL,
    enabled boolean NOT NULL
);

create table authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(60) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
CREATE UNIQUE index ix_auth_username ON authorities (username,authority);
