-- src/test/resources/schema.sql

-- Drop tables if they exist to ensure a clean start
DROP TABLE IF EXISTS news_tags;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS author;

-- 1. Create Author Table
CREATE TABLE author (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(15) NOT NULL,
                        created_date TIMESTAMP NOT NULL,
                        last_updated_date TIMESTAMP NOT NULL
);

-- 2. Create Tags Table
CREATE TABLE tags (
                      id BIGINT PRIMARY KEY,
                      name VARCHAR(15) NOT NULL UNIQUE
);

-- 3. Create News Table (with Foreign Key to Author)
CREATE TABLE news (
                      id BIGINT PRIMARY KEY,
                      title VARCHAR(30) NOT NULL,
                      content VARCHAR(255) NOT NULL,
                      created_date TIMESTAMP NOT NULL,
                      last_updated_date TIMESTAMP NOT NULL,
                      author_id BIGINT,
    -- Define Foreign Key constraint
                      FOREIGN KEY (author_id) REFERENCES author(id)
);

-- 4. Create Comments Table (with Foreign Key to News)
CREATE TABLE comments (
                          id BIGINT PRIMARY KEY,
                          content VARCHAR(255) NOT NULL,
                          created TIMESTAMP NOT NULL,
                          modified TIMESTAMP NOT NULL,
                          news_id BIGINT,
    -- Define Foreign Key constraint
                          FOREIGN KEY (news_id) REFERENCES news(id)
);

-- 5. Create News_Tags Join Table (Many-to-Many)
CREATE TABLE news_tags (
                           news_id BIGINT NOT NULL,
                           tag_id BIGINT NOT NULL,
                           PRIMARY KEY (news_id, tag_id),
                           FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE,
                           FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);