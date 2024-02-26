CREATE DATABASE userDatabase;

create table users
(
    id                  varchar(36)          not null
        primary key,
    first_name          varchar(255)         null,
    last_name           varchar(255)         null,
    last_login_time_utc datetime             null,
    requests_number     int        default 0 null,
    is_blocked          tinyint(1) default 0 not null
);

