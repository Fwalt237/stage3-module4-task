
INSERT INTO author (id, name, created_date, last_updated_date)
VALUES
    (1, 'John Doe', NOW(), NOW()),
    (2, 'Jane Smith', NOW(), NOW()),
    (3, 'Bob Johnson', NOW(), NOW());

INSERT INTO tags (id, name)
VALUES
    (1, 'Sports'),
    (2, 'Technology'),
    (3, 'Politics'),
    (4, 'Entertainment');

INSERT INTO news (id, title, content, created_date, last_updated_date, author_id)
VALUES
    (1, 'Breaking Sports News', 'This is sports news content with details', NOW(), NOW(), 1),
    (2, 'Tech Innovation Update', 'Latest technology innovations explained', NOW(), NOW(), 2),
    (3, 'Political Analysis', 'Deep dive into political events', NOW(), NOW(), 1),
    (4, 'Entertainment Weekly', 'Celebrity news and updates', NOW(), NOW(), 3);

INSERT INTO news_tags (news_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (3, 3),
    (4, 4);

INSERT INTO comments (id, content, created, modified, news_id)
VALUES
    (1, 'Great article about sports!', NOW(), NOW(), 1),
    (2, 'Very informative content', NOW(), NOW(), 1),
    (3, 'Interesting tech perspective', NOW(), NOW(), 2),
    (4, 'I disagree with this analysis', NOW(), NOW(), 3),
    (5, 'Thanks for sharing this news', NOW(), NOW(), 4);

ALTER TABLE author ALTER COLUMN id RESTART WITH 4;
ALTER TABLE tags ALTER COLUMN id RESTART WITH 5;
ALTER TABLE news ALTER COLUMN id RESTART WITH 5;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 6;