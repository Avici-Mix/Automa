package com.zzb.automa.dao;

import com.zzb.automa.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select(value = "select u.account_number,u.password from user u where u.account")
    @Results({@Result(property = "account_number",column = "account_number"),
            @Result(property = "password",column = "password")})
    User findUserByName(@Param("account_number") String account_number);

    @Insert("insert into user values(#{id},#{account_number},#{password},#{phone})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void register(User user);

    @Select("select u.id from user u where u.account_number = #{account_number} and password= #{password}")
    Long Login(User user);
}
