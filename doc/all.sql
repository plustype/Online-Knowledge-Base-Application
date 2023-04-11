drop table if exists 'test';
create table 'test' (
    'id' bigint not null comment 'id',
    'name' varchar(50) comment 'name',
    'password' varchar(50) comment 'pass'
    primary key ('id')
) engine = innodb default charset=utf8mb4 comment 'test';

insert into `test` (id, name, password) values (1,'test', 'password');

drop table if exists `demo`;
create table `demo` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='demo'

insert into `demo` (id, name) VALUES (1, 'test');

# ebook table
drop table if exists `ebook`;
create table `ebook` (
    `id` bigint not null,
    `name` varchar(50),
    `category_1` bigint,
    `category_2` bigint,
    `description` varchar(200),
    `cover` varchar(200),
    `chapter_count` int,
    `view_count` int,
    `like_count` int,
    primary key (`id`)
) engine = innodb default charset = utf8mb4;

insert into `ebook` (id, name, description) values (1, 'Head First Java', 'A Brain-Friendly Guide');
insert into `ebook` (id, name, description) values (2, 'Java Advance', 'The advance topics for Java application');