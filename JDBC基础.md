JDBC教程（JDBC 2）    
https://www.yiibai.com/jdbc/jdbc_quick_guide.html  
ORACLE官方教程（JDBC 4）  
https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html  

4.2是最新的JDBC版本号。

jdbc mysql connector 源码  
maven repository 下载（View All处） mysql-connector-java-x.x.x-source.jar解压即可获得

## JDBC简介

JDBC API是一个Java API，可以访问任何类型表列数据，特别是存储在关系数据库中的数据。JDBC代表Java数据库连接。

JDBC API的功能：
+ 连接到数据库
+ 创建SQL语句
+ 在数据库中执行SQL语句
+ 查看和修改数据库中的数据记录

JDBC两层架构  
![image](https://beanlam.gitbooks.io/jdbc-4-2-specifications/content/two-tiers-model.jpg)  
JDBC三层架构  
![image](https://beanlam.gitbooks.io/jdbc-4-2-specifications/content/three-tiers-model.jpg)  

## JDBC版本与mysql-connector-java-xxx.jar包对应关系
可以查看JAR包META-INF/services/MANIFEST.MF中的内容  
```
Specification-Title: JDBC
Specification-Version: 4.2
Specification-Vendor: Oracle Corporation
Implementation-Title: MySQL Connector Java
Implementation-Version: 6.0.6
```
从上面可以看到 mysql-connector-java-6.0.6.jar对应的JDBC的版本是JDBC 4.2 。 

## JDBC开发流程

#### **JDBC 2的开发流程**  
（加载驱动、建立连接、增删改查、清理资源）  
参考第一个链接

#### **JDBC 4的开发流程**  
（建立连接、创建语句、执行询问、处理结果、关闭连接）  
参考第二个链接

建立一个 **JDBC 4** 应用程序，本教程中以Java连接MySQL为一个示例：

```
package top.kwseeker;

import top.kwseeker.util.PrintExceptionMsg;

import java.sql.*;
import java.util.Formatter;
import java.util.Properties;

/**
 * JDBC 4.0
 */
public class DriverManagerMain {

    /**
     * 后面将这些参数放到xml文件中，通过 Property.loadFromXML传入到程序代码中
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shopping?characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "112358";
    private static final String DB_NAME = "shopping";

    private static Connection conn = null;

    public static void main(String[] args) {
        // 1. Establishing a connection.
        // 两种连接方式
        // Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", USERNAME);
        connectionProps.put("password", PASSWORD);
        try {
            conn = DriverManager.getConnection(DB_URL, connectionProps);
            conn.setCatalog(DB_NAME);           //
        } catch (SQLException se) {
            se.printStackTrace();
        }
        System.out.println("Connected to database ...");

        try {
            // 2. Create a statement.
            // 有三种语句
            Statement stmt = conn.createStatement();
            // TODO: PreparedStatement
            // Statement stmtPrepared = conn.prepareStatement();
            // TODO: CallableStatement
            // Statement stmtCallable = conn.prepareCall();

            // 3. Execute the query.
            String sqlQuery = "select id, name, city, price from items";
            ResultSet resultSet = stmt.executeQuery(sqlQuery);

            // 4. Process the ResultSet object.
            Formatter formatter = new Formatter(System.out);
            formatter.format("%-3s\t %-11s\t %-7s\t %-7s\t\n", "id", "name", "city", "price");
            while (resultSet.next()) {
                formatter.format("%-3d\t %-11s\t %-7s\t %-7d\t\n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("city"),
                        resultSet.getInt("price"));
            }

            System.out.println("isAfterLast: " + (resultSet.isAfterLast()?"true":"false"));

            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if(conn != null){
                    // 5. Close the connection.
                    conn.close();
                    conn = null;
                }
            } catch (SQLException se) {
                PrintExceptionMsg.printSQLException(se);
            }
        }

    }
}

```

++这里有个坑++：  
DB_URL = "jdbc:mysql://localhost:3306/shopping?characterEncoding=utf8&useSSL=true&serverTimezone=UTC"   
++需要指定是否使用SSL和以及指定使用的时区（选择UTC）++。   

## 几个概念的关系

**JNDI**（Java Naming and Directory Interface）  
JNDI 对应于J2SE中的javax.naming包，这套API的主要作用在于：它可以把Java对象放在一个容器中（JNDI容器），并为容器中的java对象取一个名称，以后程序想获得Java对象，只需通过名称检索即可。  
其核心API为Context，它代表JNDI容器，其lookup方法为检索容器中对应名称的对象。  

**数据库连接池**：  
**DBCP** **C3P0** **Proxool** **BoneCP**  

**数据库访问接口规范**：  
**ODBC** **JDBC** 

**JNDI与连接池的关系**：  
通常可以在应用服务器的配置文件中配置连接池，并通过Java命名和目录接口（JNDI）访问它。

## JDBC 4 详解

#### JDBC连接的两种方式

+ DriverManager (不推荐使用)
+ DataSource （推荐使用）
    - 直连数据库（内部实现还是通过DriverManager）  
    - 池化连接  
    使用连接池可以减少创建和销毁连接产生的资源开销。
    - 分布事务连接  

#### 结果集 ResultSet

ResultSet 和 JdbcRowSet 是==连接型==；  
CachedRowSet及其三个子类FilterdRowSet、WebRowSet、JoinRowSet都是==离线型==。  

```
java.sql.ResultSet.java  
```
检索和操作查询结果的接口；ResultSet的三个功能特点：类型、并发性、光标可保存性。

+ **ResultSet 类型** （光标被操作方式）
    - TYPE_FORWARD_ONLY  
        结果集的游标只能向下滚动
    - TYPE_SCROLL_INSENSITIVE  
        结果集的游标可以上下移动，当数据库变化时，当前结果集不变。
    - TYPE_SCROLL_SENSITIVE  
        返回可滚动的结果集，当数据库变化时，当前结果集同步改变。  
        这个参数只对更新数据有效，删除、插入操作无效。   

        ++同时还有个坑：mysql-
connector-java-6.0.6.jar是不支持这个参数的++。

        **ResultSet.TYPE_SCROLL_SENSITIVE失效的原因有两个**：  
        1　jdbc本身不支持，这也告诫我们做事情不能先入为主想当然，还有就是写程序不要像mysql那样不严谨，不支持也没有任何提示，还是学微软的风格比较好。  
        2　因为数据被缓存了，没有从数据库读取更新后的数据。
        https://blog.csdn.net/l912943297/article/details/52292211
+ **ResultSet 并发性** （并发修改数据源数据）
    - CONCUR_READ_ONLY  
        不能用结果集更新数据库中的表  
    - CONCUR_UPDATETABLE   
        能用结果集更新数据库中的表
+ **ResultSet 光标可保存性**

#### RowSet

用于保存列表数据可以比ResultSet更灵活和方便(是对ResultSet接口的派生以及功能拓展 )。Oracle为RowSet的一些更流行的用法定义了5个RowSet接口，并且为这些RowSet接口提供了标准引用。  

+ **JdbcRowSet**  
    最大的区别在于它有一组属性和一个监听器通知机制使其成为JavaBeans组件。对 ResultSet 的一个封装，使其能够作为 JavaBeans 被使用，是唯一一个保持数据库连接的 RowSet。JdbcRowSet 对象是连接的 RowSet 对象，也就是说，它必须使用启用 JDBC 技术的驱动程序（“JDBC 驱动程序”）来持续维持它与数据源的连接。
    
    JdbcRowSet有以下默认属性：  
    ```    
    type: ResultSet.TYPE_SCROLL_INSENSITIVE (has a scrollable cursor)
    concurrency: ResultSet.CONCUR_UPDATABLE (can be updated)
    escapeProcessing: true (the driver will do escape processing; when escape processing is enabled, the driver will scan for any escape syntax and translate it into code that the particular database understands)
    maxRows: 0 (no limit on the number of rows)
    maxFieldSize: 0 (no limit on the number of bytes for a column value; applies only to columns that store BINARY, VARBINARY, LONGVARBINARY, CHAR, VARCHAR, and LONGVARCHAR values)
    queryTimeout: 0 (has no time limit for how long it takes to execute a query)
    showDeleted: false (deleted rows are not visible)
    transactionIsolation: Connection.TRANSACTION_READ_COMMITTED (reads only data that has been committed)
    typeMap: null (the type map associated with a Connection object used by this RowSet object is null)
    ```
    
+ **CacheRowSet**（CacheRowSetImpl.java）  
最常用的一种 RowSet。其他三种 RowSet（WebRowSet，FilteredRowSet，JoinRowSet）都是直接或间接继承于它并进行了扩展。它==提供了对数据库的离线操作==，可以将数据读取到内存中进行增删改查，再同步到数据源。CachedRowSet是可滚动的、可更新的、可序列化，可作为 JavaBeans 在网络间传输。支持事件监听，分页等特性。 CachedRowSet 对象通常包含取自结果集的多个行，但是也可包含任何取自表格式文件（如电子表格）的行。

+ **WebRowSet**  
继承自 CachedRowSet，除了提供CachedRowSet对象的所有功能外，它还可以将自己写成XML文档，还可以读取XML文档，将自己转换回WebRowSet对象。因为XML是不同的企业之间可以相互通信的语言，所以它已经成为Web服务通信的标准。因此，一个WebRowSet对象通过使Web服务以XML文档的形式从数据库发送和接收数据来满足实际需要。
+ **JoinRowSet**  
JoinRowSet实现允许您在RowSet对象之间创建一个SQL联接，当它们没有连接到数据源时。这很重要，因为它节省了创建一个或多个连接的开销。  
JoinRowSet接口是CachedRowSet接口的一个子接口，因此继承了CachedRowSet对象的功能。这意味着JoinRowSet对象是一个断开连接的RowSet对象，并且可以在不总是连接到数据源的情况下操作。
+ **FileteredRowSet**  
通过设置 Predicate（在 javax.sql.rowset 包中），提供数据过滤的功能。可以根据不同的条件对 RowSet 中的数据进行筛选和过滤。


#### Statement PreparedStatement CallableStatement 对比
CallableStatement 继承 PreparedStatement,  
PreparedStatement 继承 Statement，  
Statement 继承 Wrapper 和 AutoCloseable。  

Statement 接口提供了执行语句和获取结果的基本方法；++不能接收参数；++
每次执行sql语句，数据库都要执行sql语句的编译，++最好用于仅执行一次查询并返回结果的情形++;

PreparedStatement ++接口添加了处理 IN 参数的方法++；    执行的SQL语句中是可以带参数的,并支持++批量执行SQL++。由于采用Cache机制，则预先编译的语句，就会放在Cache中，下次执行相同SQL语句时，则可以直接从Cache中取出来。

CallableStatement ++接口添加了处理 OUT 参数的方法++。CallableStatement 中定义的所有方法都用于处理 OUT 参数或INOUT 参数的输出部分：注册 OUT 参数的 JDBC 类型（一般 SQL 类型）、从这些参数中检索结果，或者检查所返回的值是否为JDBC NULL。

PreparedStatement和CallableStatment还可以处理高级数据类型。

```
// PreparedStatement
PreparedStatement updateSales = null;
String updateSaleStr = "update " + dbName + ".COFFEES "     + "set SALES = ? where COF_NAME = ?";

updateSales = con.prepareStatement(updateSaleStr);
//...
updateSales.setInt(1, e.getValue());
updateSales.setString(2, e.getKey());
updateSales.executeUpdate();

con.commit();

// CallableStatement


```


#### JDBC SQLExceptions处理

+ SQLException.getMessage()
+ SQLException.getSQLState() 获取SQLState码
+ SQLException.getErrorCode() 获取错误码
+ SQLException.getCause() 
+ SQLException.getNextException() 获取链式异常

```
package top.kwseeker.util;

import java.sql.SQLException;

public class PrintExceptionMsg {

    public static boolean ignoreSQLException(String sqlState) {
        if (sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;
        return false;
    }

    // 打印异常信息，包括SQLState 错误码 异常消息
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (!ignoreSQLException(((SQLException)e).getSQLState())) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " + ((SQLException)e).getSQLState());
                    System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                    Throwable t = ex.getCause();
                    while (t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }

}

```

#### 事务处理与游标保持（Cursor Holdability）

**事务管理实现**  
JDBC事务控制管理  
https://blog.csdn.net/caomiao2006/article/details/22412755  

JDBC的事务管理实现基于SQL数据库的事务管理；在自动提交模式下，每个statement语句对应一个事务，同一时间锁只能被一个事务所持有；而在事务管理将多个语句绑定一个事务时，如下：  
Connection.setAutoCommit(false); //相当于start transaction，开始事务获取锁
Connection.commit();  //commit，SQL开始执行，执行完毕释放锁
Connection.rollback();  //rollback，执行异常回滚    

```
try{
    conn.setAutoCommit(false);
    // 事务代码 ...
    conn.commit();  //上面事务代码统一处理
} catch (SQLException se) {
    conn.rollback();    
} finally {
    conn.setAutoCommit(true);
}
```

事务隔离级别与隔离问题  

隔离级别 | 脏读（Dirty Read）| 可重复读（NonRepeatable Read） | 幻读（Phantom Read） 
---|---|---|---
未提交读（Read uncommitted）| 可能 | 可能 | 可能
已提交读（Read committed）| 不可能 | 可能 | 可能
可重复读（Repeatable read）| 不可能 | 不可能 |可能
可串行化（Serializable ）| 不可能 | 不可能 | 不可能
```
int TRANSACTION_NONE             = 0;
int TRANSACTION_READ_UNCOMMITTED = 1;
int TRANSACTION_READ_COMMITTED   = 2;
int TRANSACTION_REPEATABLE_READ  = 4;
int TRANSACTION_SERIALIZABLE     = 8;
```

事务的特性(ACID)  
（1）原子性（Atomicity）  
原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。   
（2）一致性（Consistency）  
事务前后数据的完整性必须保持一致。  
（3）隔离性（Isolation）  
事务的隔离性是指多个用户并发访问数据库时，一个用户的事务不能被其它用户的事务所干扰，多个并发事务之间数据要相互隔离。  
（4）持久性（Durability）  
持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来即使数据库发生故障也不应该对其有任何影响  


**游标保持**  
Calling the method Connection.commit can close the ResultSet objects that have been created during the current transaction.  
调用Connection.commit()之后，事务执行，并将ResultSet对象关闭。

+ HOLD_CURSORS_OVER_COMMIT (JDBC4 默认)   
    表示修改提交时ResultSet不关闭
+ CLOSE_CURSORS_AT_COMMIT (mysql-connnector-java-6.0.6.jar不支持)  
    表示修改提交时ResultSet关闭

在创建Statement时创建：  
```
Statement stmt3 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
```

#### 批处理更新
```
public void batchUpdate() throws SQLException {

    Statement stmt = null;
    try {
        this.con.setAutoCommit(false);
        stmt = this.con.createStatement();

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Amaretto', 49, 9.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Hazelnut', 49, 9.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Amaretto_decaf', 49, " +
            "10.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Hazelnut_decaf', 49, " +
            "10.99, 0, 0)");

        int [] updateCounts = stmt.executeBatch();
        this.con.commit();

    } catch(BatchUpdateException b) {
        JDBCTutorialUtilities.printBatchUpdateException(b);
    } catch(SQLException ex) {
        JDBCTutorialUtilities.printSQLException(ex);
    } finally {
        if (stmt != null) { stmt.close(); }
        this.con.setAutoCommit(true);
    }
}
```
```
con.setAutoCommit(false);
PreparedStatement pstmt = con.prepareStatement(
                              "INSERT INTO COFFEES VALUES( " +
                              "?, ?, ?, ?, ?)");
pstmt.setString(1, "Amaretto");
pstmt.setInt(2, 49);
pstmt.setFloat(3, 9.99);
pstmt.setInt(4, 0);
pstmt.setInt(5, 0);
pstmt.addBatch();

pstmt.setString(1, "Hazelnut");
pstmt.setInt(2, 49);
pstmt.setFloat(3, 9.99);
pstmt.setInt(4, 0);
pstmt.setInt(5, 0);
pstmt.addBatch();

// ... and so on for each new
// type of coffee

int [] updateCounts = pstmt.executeBatch();
con.commit();
con.setAutoCommit(true);

```

#### 带参数SQL语句
```
// 带参数（？）的SQL语句
    public void updateStorage(HashMap<String, Integer> stock) throws SQLException {

        String dbName = "shopping";
        String table = "items";
        PreparedStatement updateStorage = null;

        String updateStorageString =
                "update " + dbName + "." + table +
                        " set number = number + ? where name = ?";

        try {
            conn.setAutoCommit(false);
            updateStorage = conn.prepareStatement(updateStorageString);

            for (Map.Entry<String, Integer> e : stock.entrySet()) { // entry 迭代器
//                updateStorage.setInt(1, e.getValue().intValue()); // 无需 unbox
                updateStorage.setInt(1, e.getValue());
                updateStorage.setString(2, e.getKey());
                updateStorage.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e ) {
            PrintExceptionMsg.printSQLException(e);
            if (conn != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    conn.rollback();
                } catch(SQLException se) {
                    PrintExceptionMsg.printSQLException(se);
                }
            }
        } finally {
            if (updateStorage != null) {
                updateStorage.close();
            }
            conn.setAutoCommit(true);
        }
    }
```

#### 高级数据类型  

高级数据类型为关系数据库提供了更大的灵活性，可以用作表列的值。

+ **BLOB**: Blob interface
+ **CLOB**: Clob interface
+ **NCLOB**: NClob interface  
Blob、Clob和NClob Java对象的一个重要特性是，您可以操纵它们，而不必将它们的所有数据从数据库服务器带到客户端计算机。有些实现代表了这些类型的实例，其中有一个定位器（逻辑指针）到实例所代表的数据库中的对象。因为BLOB、CLOB或NCLOB SQL对象可能非常大，所以使用locator可以显著提高性能。然而，其他实现完全实现了客户端计算机上的大型对象。  
+ **ARRAY**: Array interface
+ **XML**: SQLXML interface  
Connection接口使用createSQLXML方法为创建SQLXML对象提供了支持。所创建的对象不包含任何数据。数据可以通过在SQLXML接口上调用setString、setBinaryStream、set字符流或setResult方法来添加到对象中。  
+ **Structured types**: Struct interface  
用户定义类型
+ **Customized type mappings**
+ **REF**(structured type): Ref interface
+ **ROWID**: RowId interface (MySQL不支持)  
RowId对象代表数据库表中行的一个地址。但是请注意，ROWID类型不是标准的SQL类型。ROWID的值可能是有用的，因为它们通常是访问单个行的最快方法，并且是表中各行的惟一标识。但是，您不应该使用ROWID值作为表的主键。例如，如果您从一个表中删除一个特定的行，那么数据库可能会将它的ROWID值重新分配给稍后插入的一行。
+ **DISTINCT**: Type to which the base type is mapped. For example, a DISTINCT value based on a SQL NUMERIC type maps to a java.math.BigDecimal type because NUMERIC maps to BigDecimal in the Java programming language.
+ **DATALINK**: java.net.URL object  
DATALINK通过URL引用底层数据源之外的资源。一个URL，统一资源定位器，是一个指向万维网上资源的指针。资源可以是像文件或目录那样简单的东西，也可以是对更复杂的对象的引用，比如对数据库或搜索引擎的查询。

#### 使用存储过程（类似编程语言的函数方法）
mysql存储过程详细教程  
https://www.jianshu.com/p/7b2d74701ccd

代码参考：   
https://github.com/kwseeker/jdbc_demo  
src.top.kwseeker.jdbcStoredProcedure.StoredProcedure.java

**存储过程**(Stored Procedure)：  
一组可编程的函数，是为了完成特定功能的SQL语句集，经编译创建并保存在数据库中，用户可通过指定存储过程的名字并给定参数(需要时)来调用执行。

**优点**(为什么要用存储过程？)：

1.存储过程只在创造时进行编译，以后每次执行存储过程都不需再重新编译，而一般SQL 语句每执行一次就编译一次所以使用存储过程可提高数据库执行速度。

2.当对数据库进行复杂操作时(如对多个表进行Update,Insert,Query,Delete 时）可将此复杂操作用存储过程封装起来与数据库提供的事务处理结合一起使用。
这些操作，如果用程序来完成，就变成了一条条的SQL 语句，可能要多次连接数据库。而换成存储，只需要连接一次数据库就可以了。

3.存储过程可以重复使用
可减少数据库开发人员的工作量。

4.安全性高
可设定只有某此用户才具有对指定存储过程的使用权。

5.更强的适应性：由于存储过程对数据库的访问是通过存储过程来进行的，因此数据库开发
人员可以在不改动存储过程接口的情况下对数据库进行任何改动，而这些改动不会对应用程序造成影响。

6.分布式工作：应用程序和数据库的编码工作可以分别独立进行，而不会相互压制。
一般来说，存储过程的编写比基本SQL语句复杂，编写存储过程需要更高的技能，更丰富的经验。

**缺点**：

使得代码可读性变差不易维护。

**存储过程模板**  
```
CREATE
    [DEFINER = { user | CURRENT_USER }]
　PROCEDURE sp_name ([proc_parameter[,...]])
    [characteristic ...] routine_body

proc_parameter:
    [ IN | OUT | INOUT ] param_name type

characteristic:
    COMMENT 'string'
  | LANGUAGE SQL
  | [NOT] DETERMINISTIC
  | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
  | SQL SECURITY { DEFINER | INVOKER }

routine_body:
　　Valid SQL routine statement

[begin_label:] BEGIN
　　[statement_list]
　　　　……
END [end_label]
```

```
drop PROCEDURE if EXISTS a;  
create  PROCEDURE a (in c int,inout d int,out f int)-- 在例子中c=12,d=10,f未赋值  
BEGIN  
    DECLARE e int;  -- 存储过程中声明不需要@  
    set e=d;-- e=10  
  
    set d=c+10;     -- @n=22  
    set f=e;        -- @x=10  
    set c=c+1;      -- c为in模式，修改它的值不影响@m，@m仍为12  
    
    -- SQL语句
    
END;  
-- --------------------------------------
set @m=12,@n=10;  
call a(@m,@n,@x);  
select @m,@n,@x;  
```

默认情况下，delimiter是分号;。在命令行客户端中，如果有一行命令以分号结束，那么回车后，mysql将会执行该命令。

**存储过程使用流程**：  

```
// 1. 创建存储过程（存储过程体、参数、语句块标签）  
String queryDropStr = "DROP PROCEDURE IF EXISTS RAISE_PRICE";
String createProcedureStr =
            "create procedure RAISE_PRICE(IN itemName varchar(50), IN maximumPercentage float, INOUT newPrice INT) " +
            "begin " +
                "main: BEGIN " +                                                        //使用标签
//                    "declare maximumNewPrice numeric(10,2); " +                         // 小数
                    "declare maximumNewPrice INT; " +                         // 小数
                    "declare oldPrice INT; " +
                    "select ITEMS.PRICE into oldPrice " +
                        "from ITEMS " +
                        "where ITEMS.name = itemName; " +
                    "set maximumNewPrice = oldPrice * (1 + maximumPercentage); " +
//                    "if (newPrice > maximumNewPrice) " +
//                    "then set newPrice = maximumNewPrice; " +
//                    "end if; " +
//                    "if (newPrice <= oldPrice) " +
//                    "then set newPrice = oldPrice;" +
//                    "leave main; " +
//                    "end if; " +
                    "if (ABS(newPrice-oldPrice) > ABS(oldPrice-maximumNewPrice)) " +
                    "then set newPrice = maximumNewPrice; " +
                    "end if; " +
                    "update ITEMS " +
                    "set ITEMS.PRICE = newPrice " +
                    "where ITEMS.NAME = itemName; " +
                    "select newPrice; " +
                "END main; " +
            "end";
            
Statement stmtDrop = con.createStatement();
Statement stmtDrop.execute(queryDropStr);
Statement stmt = con.createStatement();
Statement stmt.executeUpdate(createProcedureStr);

// 2. 调用存储过程
CallableStatement cs = null;
cs = this.con.prepareCall("{call RAISE_PRICE(?,?,?)}");
cs.setString(1, itemNameArg);
cs.setFloat(2, maximumPercentageArg);
cs.registerOutParameter(3, Types.INTEGER);
cs.setFloat(3, newPriceArg);
cs.execute();
```

**存储过程作用域**  
创建的存储过程是一直存在的(++存储在 mysql.proc表里,全局有效++)，除非手动删除（DROP PROCEDURE IF EXISTS GET_SUPPLIER_OF_ITEM; ）

```
-- 查看存储过程
SHOW PROCEDURE STATUS；
-- 查看自定义函数
SHOW FUNCTION STATUS;
```

#### 使用JDBC GUI API