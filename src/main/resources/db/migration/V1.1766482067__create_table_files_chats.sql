CREATE TABLE IF NOT EXISTS attachments(
    id bigserial primary key,
    message_id bigint references messages(id),
    file_type varchar not null,
    original_file_name varchar not null,
    mime_type varchar not null,
    size varchar not null
);