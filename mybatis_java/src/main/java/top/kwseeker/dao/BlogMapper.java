package top.kwseeker.dao;

import top.kwseeker.pojo.Blog;

public interface BlogMapper {

    //通过id查找对应的博客
    Blog selectBlog(int id);

}

