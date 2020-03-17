--liquibase formatted sql
--changeset viktor:create-initial-schema splitStatements:true endDelimiter:;

-- author table

CREATE SEQUENCE author_id_seq;

CREATE TABLE author (
    author_id INTEGER NOT NULL DEFAULT nextval('author_id_seq'),
    name VARCHAR(128) NOT NULL,
    CONSTRAINT author_pk PRIMARY KEY (author_id)
);


-- category table

CREATE SEQUENCE category_id_seq;

CREATE TABLE category (
    category_id INTEGER NOT NULL DEFAULT nextval('category_id_seq'),
    name VARCHAR(64) NOT NULL,
    CONSTRAINT category_pk PRIMARY KEY (category_id)
);


-- book table

CREATE SEQUENCE book_id_seq;

CREATE TABLE book (
    book_id INTEGER NOT NULL DEFAULT nextval('book_id_seq'),
    author_id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    isbn VARCHAR(25) NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY (book_id),
    CONSTRAINT book_author_fk FOREIGN KEY (author_id)
        REFERENCES author(author_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);


-- book <-> category many to many relation table

CREATE TABLE book_category (
    book_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    CONSTRAINT book_fk FOREIGN KEY (book_id)
        REFERENCES book(book_id),
    CONSTRAINT category_fk FOREIGN KEY (category_id)
            REFERENCES category(category_id),
    CONSTRAINT book_category_key UNIQUE (book_id, category_id)
);


-- account table

CREATE SEQUENCE account_id_seq;

CREATE TYPE account_role AS ENUM ('admin', 'user');

CREATE TABLE account (
    account_id INTEGER NOT NULL DEFAULT nextval('account_id_seq'),
    login VARCHAR(128),
    password CHAR(60),
    assigned_role account_role,
    CONSTRAINT account_pk PRIMARY KEY (account_id)
);
