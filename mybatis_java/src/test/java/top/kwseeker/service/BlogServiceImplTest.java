package top.kwseeker.service;

import org.junit.BeforeClass;
import org.junit.Test;
import top.kwseeker.dao.BlogMapper;
import top.kwseeker.pojo.Blog;

import static org.junit.Assert.*;

public class BlogServiceImplTest {

    private static BlogServiceImpl blogService;

    @BeforeClass
    public static void beforeClass() {
        blogService = new BlogServiceImpl();
    }

    @Test
    public void getBlogById() throws Exception {
        Blog blog = blogService.getBlogById(101);
        System.out.println("title: " + blog.getTitle());
    }

}