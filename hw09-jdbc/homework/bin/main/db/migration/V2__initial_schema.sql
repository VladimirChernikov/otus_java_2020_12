drop table if exists client;
create table client
(
    id   bigserial not null primary key,
    name varchar(500)
);

drop table if exists manager;
create table manager
(
    no   bigserial not null primary key,
    label varchar(500)
);

