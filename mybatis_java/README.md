## Mybatis 在java中的使用

注意项目配置与控制部分代码的书写，逻辑与数据部分代码是与业务相关的在学习Mybatis使用时不必看重。  

#### 项目配置

+ 导入包
```
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>x.x.x</version>
</dependency>
```

+ 环境配置（）

如果属性在不只一个地方进行了配置，那么 MyBatis 将按照下面的顺序来加载：  
在 properties 元素体内指定的属性首先被读取。  
然后根据 properties 元素中的 resource 属性读取类路径下属性文件或根据 url 属性指定的路径读取属性文件，并覆盖已读取的同名属性。  
最后读取作为方法参数传递的属性，并覆盖已读取的同名属性。  

+ Mybatis内置日志配置
    内置日志实现支持： 
        SLF4J
        Apache Commons Logging
        Log4j 2
        Log4j
        JDK logging
    logImple可选value： 
        SLF4J、LOG4J、LOG4J2、JDK_LOGGING、COMMONS_LOGGING、STDOUT_LOGGING、NO_LOGGIN
```
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

#### 控制部分代码

+ SQLSessionFactory实例创建
    每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为中心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。
    而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先定制的 Configuration 的实例构建出 SqlSessionFactory 的实例。  
    
    创建SQLSessionFactory实例的两种方式：
    
    1） 从XML配置文件构建
    
    2） 从Mybatis配置类构建
    
+ 获取SQLSession
    SqlSession 完全包含了面向数据库执行 SQL 命令所需的所有方法。
    
+ XML映射

+ 使用Generator构建SQL语句

#### 逻辑与数据部分代码