# MyBatis 源码学习预备知识

## 1 JDBC基础

Mybatis是基于JDBC实现的ORM框架。所以要理解Mybatis工作原理，
需要先了解JDBC的使用。区分清Mybatis和JDBC的工作界线然后才更清楚Mybatis
怎么与JDBC交接任务，及Mybatis干了什么事。

## 2 为什么需要Mybatis等ORM框架

框架也是产品开发时也是需要做功能需求分析，然后设计架构。

#### 2.1 JDBC使用痛点、程序员需求、ORM架构设计

1）需要写一些硬编码，如数据库连接配置

+ 程序员

  能否将硬编码等部分的代码复用起来，使配置更简单。

+ ​ORM框架

  使用配置文件配置JDBC中硬编码配置（数据库驱动信息，数据库连接信息，sql语句，参数信息）。

  MyBatis 定义了一个全局配置文件 mybatis-config.xml（配置数据库驱动信息、数据库连接信息等运行时信息）；

  MyBatis 定义了映射文件（配置和业务相关的sql语句等信息）。

2）JDBC需要频繁地开启连接和关闭

+ 程序员

  能否复用连接。

+ ORM框架

  支持连接池。

3）JDBC使用不方便每次查询需要写sql，创建statement语句，获取结果集，从结果集取数据放入对象

+ 程序员

  能否实现接口，只需要调接口传参就可以获取结果对象。

+ ORM框架

  Sql语句（包括传参、结果返回、类型映射）需要程序员定义，放到映射文件，ORM框架读取Sql映射文件，解析自动生成statement语句，执行获取结果集并放到对象返回。

#### 2.2 ORM框架设计思想

上面分析了两个主要的需求：配置文件加载、sql语句自动解析执行。

1）配置文件设计

+ 配置文件结构

+ 配置文件加载流程

  a) 指定全局配置文件（XML格式）的路径

  b) 根据路径去读取配置文件到内存（InputStream）

  c) 通过dom解析（可使用SAX），读取InputStream流，获取一个Document对象

  d) 对Document对象按照mybatis的标签语义进行解析（Configuration对象，封装了整个全局配置文件和映射文件信息）

  e) 解析全局配置文件的时候，还需要解析映射文件，主要解析映射文件（Mapper）中的四个标签（Select、insert、update、delete），解析成一个对象MappedStatement；MappedStatement需要存储sql语句、入参Java类型、结果映射Java类型、statement类型。