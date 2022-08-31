drop table member if exists cascade;

create table member(
    member_id character varying[10],
    money integer not null default 0,
    primary key (member_id)
)