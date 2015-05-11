package com.sandwish.site.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sandwish.site.dao.user.UserDao;
import com.sandwish.site.entity.User;
import com.sandwish.site.service.dao.UserService;

@Transactional
@Service("UserService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userMapper;

	public User getUser(String userID) {
		return userMapper.getUser(userID);
	}
}
