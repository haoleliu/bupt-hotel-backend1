-- auto-generated definition
create table airconditioner
(
    id              bigint unsigned auto_increment
        primary key,
    room_id         varchar(50)                    not null,
    temperature     decimal(5, 2) default 26.00    not null,
    mode            varchar(10)   default 'cool'   not null,
    speed           int           default 2        not null,
    queue           varchar(10)   default '等待中' not null,
    last_start_time timestamp                      null,
    power           tinyint(1)    default 0        null,
    constraint id
        unique (id),
    constraint room_id
        unique (room_id),
    constraint fk_room
        foreign key (room_id) references room (room_number)
            on delete cascade
);

-- auto-generated definition
create table customer
(
    id_card        varchar(18)  not null
        primary key,
    name           varchar(100) not null,
    is_in          tinyint(1)   null,
    phone          varchar(15)  not null,
    room_number_id varchar(50)  not null,
    constraint fk_customer_room
        foreign key (room_number_id) references room (room_number)
            on delete cascade
);

-- auto-generated definition
create table room
(
    room_number             varchar(50)                  not null
        primary key,
    is_occupied             tinyint(1)     default 0     not null,
    check_in_date           timestamp                    null,
    check_out_date          timestamp                    null,
    total_fee               decimal(10, 2)               not null,
    environment_temperature int                          not null,
    current_temperature     decimal(5, 2)  default 26.00 not null,
    power                   tinyint(1)     default 0     not null,
    ac_usage_time           decimal(10, 2) default 0.00  not null,
    ac_fee                  decimal(10, 2) default 0.00  not null
);
-- auto-generated definition
create table waitqueue
(
    room_num          int auto_increment
        primary key,
    weight            decimal   null,
    last_request_time timestamp null,
    is_waiting        int       null
);



