create table book(
  id            uuid                      not null        primary key,
  name          text                      not null        unique,
  author        text                      not null,
  created_at    timestamp with time zone  not null,
  modified_at   timestamp with time zone
);