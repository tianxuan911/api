package com.sandwish.site.dao.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sandwish.site.entity.User;
@Component("UserDao")
public interface UserDao {
	User getUser(@Param("userId") String userId);
}
