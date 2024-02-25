CREATE DATABASE userDatabase;

create table user
(
    id                  varchar(36)  not null
        primary key,
    first_name          varchar(255) null,
    last_name           varchar(255) null,
    last_login_time_utc datetime     null
);
