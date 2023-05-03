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
insert into `ebook` (id, name, description) values (3, 'Spring Boot Basic', 'Learn Spring Boot with zero experience');
insert into `ebook` (id, name, description) values (4, 'Data Analysis', 'Learn data analysis with Python');

#-----Category table-------
drop table if exists `category`;
create table `category`
(
    `id`     bigint      not null,
    `parent` bigint      not null default 0,
    `name`   varchar(50) not null,
    `sort`   int,
    primary key (`id`)
) engine = innodb default charset = utf8mb4;

insert into `category` (id, parent, name, sort) VALUES (1000, 0000, 'Front-End Dev', 1000);
insert into `category` (id, parent, name, sort) VALUES (1001, 1000, 'Vue', 1001);
insert into `category` (id, parent, name, sort) VALUES (1002, 1000, 'HTML & CSS', 1002);
insert into `category` (id, parent, name, sort) VALUES (1003, 1000, 'React', 1003);
insert into `category` (id, parent, name, sort) VALUES (2000, 0000, 'Java', 2000);
insert into `category` (id, parent, name, sort) VALUES (2001, 2000, 'Java Basic', 2001);
insert into `category` (id, parent, name, sort) VALUES (2002, 2000, 'Java Frame', 2002);
insert into `category` (id, parent, name, sort) VALUES (3000, 3000, 'Database', 3000);
insert into `category` (id, parent, name, sort) VALUES (3001, 3000, 'MySQL', 3001);
insert into `category` (id, parent, name, sort) VALUES (4000, 4000, 'Python', 4000);
insert into `category` (id, parent, name, sort) VALUES (4001, 4000, 'Python Basic', 4001);
insert into `category` (id, parent, name, sort) VALUES (4002, 4000, 'Python Advance', 4002);
insert into `category` (id, parent, name, sort) VALUES (5000, 5000, 'Server', 5000);
insert into `category` (id, parent, name, sort) VALUES (5001, 5000, 'AWS', 5001);
insert into `category` (id, parent, name, sort) VALUES (6000, 6000, 'Data Structure', 6000);
insert into `category` (id, parent, name, sort) VALUES (6001, 6000, 'Array', 6001);
insert into `category` (id, parent, name, sort) VALUES (6002, 6000, 'Hash Table', 6002);

# --doc table
drop table if exists `doc`;
create table `doc` (
    `id` bigint not null,
    `ebook_id` bigint not null default 0,
    `parent` bigint not null default 0,
    `name` varchar(50) not null ,
    `sort` int,
    `view_count` int default 0,
    `like_count` int default 0,
    primary key (`id`)
) engine = innodb default charset = utf8mb4;

insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (1, 1, 0, 'doc1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (2, 1, 1, 'doc1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (3, 1, 0, 'doc2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (4, 1, 3, 'doc2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (5, 1, 3, 'doc2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, like_count) VALUES (6, 1, 5, 'doc2.2.1', 1, 0, 0);