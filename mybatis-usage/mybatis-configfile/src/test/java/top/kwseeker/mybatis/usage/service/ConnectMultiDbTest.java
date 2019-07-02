package top.kwseeker.mybatis.usage.service;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Assert;
import org.junit.Test;
import top.kwseeker.mybatis.usage.dao.BlogMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class ConnectMultiDbTest {

    /**
     * 这里面的操作流程就是Mybatis工作流程最主要的部分
     */
    @Test
    public void testConnectMultiDb(){

        InputStream inputStream1 = null;
        InputStream inputStream2 = null;
        try {
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            //TODO: DataSource的几种实现的区别？
            DataSource dataSource1 = new PooledDataSource("com.mysql.cj.jdbc.Driver",
                    "jdbc:mysql://127.0.0.1:3306/mybatis_learn?characterEncoding=utf8&useSSL=true&serverTimezone=UTC",
                    "root",
                    "112358flzt9t");
            DataSource dataSource2 = new PooledDataSource("com.mysql.cj.jdbc.Driver",
                    "jdbc:mysql://127.0.0.1:3307/mybatis_learn?characterEncoding=utf8&useSSL=true&serverTimezone=UTC",
                    "root",
                    "123456");
            Environment db1Env = new Environment("db1", transactionFactory, dataSource1);
            Environment db2Env = new Environment("db2", transactionFactory, dataSource2);

            Configuration configuration1 = new Configuration(db1Env);
            String resource = "mapper/BlogMapper.xml";
            inputStream1 = Resources.getResourceAsStream(resource);
            new XMLMapperBuilder(inputStream1, configuration1, resource, configuration1.getSqlFragments()).parse();
            Configuration configuration2 = new Configuration(db2Env);
            inputStream2 = Resources.getResourceAsStream(resource);
            new XMLMapperBuilder(inputStream2, configuration2, resource, configuration2.getSqlFragments()).parse();

            SqlSessionFactory ssf1 = new SqlSessionFactoryBuilder().build(configuration1);
            SqlSessionFactory ssf2 = new SqlSessionFactoryBuilder().build(configuration2);
            SqlSession sqlSession1 = ssf1.openSession();
            SqlSession sqlSession2 = ssf2.openSession();
            BlogMapper mapper1 = sqlSession1.getMapper(BlogMapper.class);
            BlogMapper mapper2 = sqlSession2.getMapper(BlogMapper.class);

            Assert.assertEquals("kwseeker", mapper1.selectBlog(1).getNickname());
            Assert.assertEquals("stormchaser", mapper2.selectBlog(3).getNickname());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream1 != null) {
                    inputStream1.close();
                }
                if(inputStream2 != null) {
                    inputStream2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
