package top.kwseeker.mybatis.analysis.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtil {

    public static SqlSessionFactory getSqlSessionFactory(String xmlPath) {
//        String resource = "resources/mybatis-config.xml";
        SqlSessionFactory sqlSessionFactory = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream(xmlPath);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    public static SqlSession getSqlSession(String xmlPath) {
        return getSqlSession(xmlPath, true);
    }

    public static SqlSession getSqlSession(String xmlPath, boolean isAutoCommit) {
        return getSqlSessionFactory(xmlPath).openSession(isAutoCommit);
    }
}
