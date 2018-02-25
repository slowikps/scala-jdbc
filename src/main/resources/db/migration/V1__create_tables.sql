
create table author(
  id            uuid   not null     primary key,
  name          text   not null,
  surname       text   not null
);

create table book(
  id            uuid                      not null        primary key,
  name          text                      not null        unique,
  author        uuid                      not null        references author(id),
  created_at    timestamp with time zone  not null,
  modified_at   timestamp with time zone
);