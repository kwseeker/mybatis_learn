package top.kwseeker.mybatis.usage.dao;

import top.kwseeker.mybatis.usage.domain.Blog;

public interface BlogMapper {

    //通过id查找对应的博客
    Blog selectBlog(int id);

}

