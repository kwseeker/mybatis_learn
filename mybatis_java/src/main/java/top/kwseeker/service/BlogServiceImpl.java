package top.kwseeker.service;

import org.apache.ibatis.session.SqlSession;
import top.kwseeker.dao.BlogMapper;
import top.kwseeker.pojo.Blog;
import top.kwseeker.util.MybatisUtil;

public class BlogServiceImpl {

    public Blog getBlogById(int id) {
        SqlSession sqlSession = MybatisUtil.getSqlSession("mybatis-config.xml");
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        return blogMapper.selectBlog(id);
    }

}
