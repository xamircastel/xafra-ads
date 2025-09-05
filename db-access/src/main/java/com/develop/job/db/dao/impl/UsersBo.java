package com.develop.job.db.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.develop.job.db.dao.bi.IUsers;
import com.develop.job.db.dao.cache.UsersCache;
import com.develop.job.db.entity.Users;

@Service
public class UsersBo implements IUsers{

	@Autowired
	private UsersCache user;
	
	@Override
	public Users getUsersByApiKey(String apikey) {
		return user.getUsersByApiKey(apikey);
	}

}
