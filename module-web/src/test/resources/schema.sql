
DROP TABLE IF EXISTS news_tags;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(15) NOT NULL,
                        created_date TIMESTAMP NOT NULL,
                        last_updated_date TIMESTAMP NOT NULL
);


CREATE TABLE tags (
                      id BIGINT PRIMARY KEY,
                      name VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE news (
                      id BIGINT PRIMARY KEY,
                      title VARCHAR(30) NOT NULL,
                      content VARCHAR(255) NOT NULL,
                      created_date TIMESTAMP NOT NULL,
                      last_updated_date TIMESTAMP NOT NULL,
                      author_id BIGINT,
                      FOREIGN KEY (author_id) REFERENCES author(id)
);


CREATE TABLE comments (
                          id BIGINT PRIMARY KEY,
                          content VARCHAR(255) NOT NULL,
                          created TIMESTAMP NOT NULL,
                          modified TIMESTAMP NOT NULL,
                          news_id BIGINT,
                          FOREIGN KEY (news_id) REFERENCES news(id)
);

CREATE TABLE news_tags (
                           news_id BIGINT NOT NULL,
                           tag_id BIGINT NOT NULL,
                           PRIMARY KEY (news_id, tag_id),
                           FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE,
                           FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);