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
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    CONSTRAINT book_pk PRIMARY KEY (book_id),
    CONSTRAINT book_author_fk FOREIGN KEY (author_id)
        REFERENCES author(author_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE OR REPLACE FUNCTION update_timestamp_column()
RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
   NEW.update_time = current_timestamp;
   RETURN NEW;
END;
$$;

CREATE TRIGGER update_book_timestamp BEFORE UPDATE
    ON book FOR EACH ROW EXECUTE PROCEDURE
    update_timestamp_column();

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
