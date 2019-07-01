package top.kwseeker.mybatis.usage.service;

import org.apache.ibatis.session.SqlSession;
import top.kwseeker.mybatis.usage.dao.BlogMapper;
import top.kwseeker.mybatis.usage.domain.Blog;
import top.kwseeker.mybatis.usage.util.MybatisUtil;

public class BlogServiceImpl implements BlogService {

    @Override
    public Blog getBlogById(int id) {
        SqlSession sqlSession = MybatisUtil.getSqlSession("mybatis-config.xml");
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        return blogMapper.selectBlog(id);
    }

}
