package top.kwseeker.mybatis.multidatasource.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kwseeker.mybatis.multidatasource.domain.Blog;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BlogMapperTest {

    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void selectBlog() {
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog.toString());
    }
}