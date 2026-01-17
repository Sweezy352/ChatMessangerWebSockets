ALTER TABLE users add column if not exists birth_date DATE not null;
ALTER TABLE users add column if not exists date_registered DATE default now();
