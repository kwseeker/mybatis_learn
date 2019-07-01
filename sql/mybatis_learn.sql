create schema if not exists mybatis_learn default character set utf8;

create table if not exists mybatis_learn.t_blog (
  id      int not null auto_increment,
  title   varchar(128) not null,
  nickname  varchar(32) not null,
  primary key(id),
  unique key(title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- create index idx_title on mybatis_learn.t_blog(title);

-- 用户表
create table if not exists mybatis_learn.t_user (
  id        int not null auto_increment,
  username  varchar(32) not null,
  nickname  varchar(32) not null,
  primary key(id),
  unique key(nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 评论表
create table if not exists mybatis_learn.t_comment (
  id      int not null auto_increment,
  blog_id int not null,
  comment varchar(512) not null,
  primary key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into mybatis_learn.t_blog (id, title, nickname) values
  (1, 'Mybatis简介', 'kwseeker'),
  (2, 'Mybatis源码分析', 'kwseeker'),
  (3, 'Mybatis动态sql', 'stormchaser');

insert into mybatis_learn.t_user (id, username, nickname) values
  (1, 'Arvin Lee', 'kwseeker'),
  (2, 'Erik Yang', 'stormchaser');

insert into mybatis_learn.t_comment (id, blog_id, comment) values
  (1, 1, '好'),
  (1, 2, '很好'),
  (1, 1, '非常好'),
  (1, 3, '很深入'),
  (1, 1, '一般');