CREATE TABLE IF NOT EXISTS test_bean (
  id          INT IDENTITY PRIMARY KEY,
  description VARCHAR(1024),
  create_date TIMESTAMP DEFAULT current_timestamp
);
DELETE FROM test_bean;