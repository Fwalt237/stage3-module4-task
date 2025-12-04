
-- 1. Delete join table records first
DELETE FROM news_tags;

-- 2. Delete comments (depends on news)
DELETE FROM comments;

-- 3. Delete news (depends on author and tags)
DELETE FROM news;

-- 4. Delete tags
DELETE FROM tags;

-- 5. Delete authors
DELETE FROM author;

-- For H2 Database, use:
ALTER TABLE author ALTER COLUMN id RESTART WITH 1;
ALTER TABLE tags ALTER COLUMN id RESTART WITH 1;
ALTER TABLE news ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;