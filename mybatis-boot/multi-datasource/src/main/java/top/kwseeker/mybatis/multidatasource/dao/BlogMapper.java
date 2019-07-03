package top.kwseeker.mybatis.multidatasource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.kwseeker.mybatis.multidatasource.domain.Blog;

@Repository
@Mapper
public interface BlogMapper {

    //通过id查找对应的博客
    @Select("select * from t_blog where id = #{id}")
    Blog selectBlog(Integer id);
}
