create schema if not exists mybatis_learn default character set utf8;

create table if not exists mybatis_learn.t_blog (
  id      int not null auto_increment,
  title   varchar(128) not null,
  primary key(id),
  unique key(title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- create index idx_title on mybatis_learn.t_blog(title);

insert into mybatis_learn.t_blog (title) values (
  ('Mybatis简介'),
  ('Mybatis源码分析');