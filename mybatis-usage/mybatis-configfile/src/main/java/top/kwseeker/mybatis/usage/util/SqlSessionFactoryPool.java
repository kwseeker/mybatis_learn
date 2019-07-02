package top.kwseeker.mybatis.usage.util;

import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.ConcurrentHashMap;

public class SqlSessionFactoryPool {

    private static ConcurrentHashMap<String, SqlSessionFactory> ssfMap = new ConcurrentHashMap<>(2);

    public static SqlSessionFactory getSqlSessionFactory(String str) {
        return ssfMap.get(str);
    }

    public static boolean storeSqlSessionFactoryToMap(String str, SqlSessionFactory sqlSessionFactory) {
        if(ssfMap.containsKey(str)) {
            return false;
        }
        ssfMap.put(str, sqlSessionFactory);
        return true;
    }
}
