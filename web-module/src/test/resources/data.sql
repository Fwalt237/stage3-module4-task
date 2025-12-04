
-- Insert test authors
INSERT INTO author (id, name, created_date, last_updated_date)
VALUES
    (1, 'John Doe', NOW(), NOW()),
    (2, 'Jane Smith', NOW(), NOW()),
    (3, 'Bob Johnson', NOW(), NOW());

-- Insert test tags
INSERT INTO tags (id, name)
VALUES
    (1, 'Sports'),
    (2, 'Technology'),
    (3, 'Politics'),
    (4, 'Entertainment');

-- Insert test news
INSERT INTO news (id, title, content, created_date, last_updated_date, author_id)
VALUES
    (1, 'Breaking Sports News', 'This is sports news content with details', NOW(), NOW(), 1),
    (2, 'Tech Innovation Update', 'Latest technology innovations explained', NOW(), NOW(), 2),
    (3, 'Political Analysis', 'Deep dive into political events', NOW(), NOW(), 1),
    (4, 'Entertainment Weekly', 'Celebrity news and updates', NOW(), NOW(), 3);

-- Link news with tags (many-to-many relationship)
INSERT INTO news_tags (news_id, tag_id)
VALUES
    (1, 1), -- Breaking Sports News -> Sports
    (1, 2), -- Breaking Sports News -> Technology
    (2, 2), -- Tech Innovation Update -> Technology
    (3, 3), -- Political Analysis -> Politics
    (4, 4); -- Entertainment Weekly -> Entertainment

-- Insert test comments
INSERT INTO comments (id, content, created, modified, news_id)
VALUES
    (1, 'Great article about sports!', NOW(), NOW(), 1),
    (2, 'Very informative content', NOW(), NOW(), 1),
    (3, 'Interesting tech perspective', NOW(), NOW(), 2),
    (4, 'I disagree with this analysis', NOW(), NOW(), 3),
    (5, 'Thanks for sharing this news', NOW(), NOW(), 4);

-- Reset sequences (if using PostgreSQL)
-- ALTER SEQUENCE author_id_seq RESTART WITH 4;
-- ALTER SEQUENCE tags_id_seq RESTART WITH 5;
-- ALTER SEQUENCE news_id_seq RESTART WITH 5;
-- ALTER SEQUENCE comments_id_seq RESTART WITH 6;

-- For H2 Database (common for testing), use:
ALTER TABLE author ALTER COLUMN id RESTART WITH 4;
ALTER TABLE tags ALTER COLUMN id RESTART WITH 5;
ALTER TABLE news ALTER COLUMN id RESTART WITH 5;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 6;