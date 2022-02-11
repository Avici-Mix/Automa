package com.zzb.automa.dao;

import com.zzb.automa.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select(value = "select username,password from user where username = #{username}")
    @Results({@Result(property = "username",column = "username"),
            @Result(property = "password",column = "password")})
    User findUserByName(@Param("username") String username);

    @Insert("insert into user values(#{id},#{username},#{password},#{email})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void register(User user);

    @Select("select id from user where username = #{username} and password= #{password}")
    Long Login(User user);
}
