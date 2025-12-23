CREATE TABLE users(
                      id bigserial primary key,
                      username varchar not null unique,
                      phone_number varchar not null unique,
                      mail varchar not null unique,
                      password varchar not null,
                      bio varchar not null,
                      age smallint not null
);

CREATE TABLE pfp_pictures_user(
                                  id bigserial primary key,
                                  original_file_name varchar not null,
                                  mime_type varchar not null,
                                  size varchar not null,
                                  user_id bigint references users(id)
);

CREATE TABLE chat_rooms(
                           id bigserial primary key,
                           type varchar not null
);

CREATE TABLE chat_room_users(
                                chat_id bigint references chat_rooms(id),
                                user_id bigint references users(id)
);

CREATE TABLE messages(
                         id bigserial primary key,
                         chat_id bigint references chat_rooms(id),
                         sender_id bigint references users(id),
                         content varchar not null,
                         date_sent timestamp with time zone default now(),
                         status varchar not null default 'SENT'
);

