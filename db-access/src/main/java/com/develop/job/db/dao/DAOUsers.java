package com.develop.job.db.dao;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.Users;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.IUsersBI;

@Service
public class DAOUsers extends BaseJbdiDao<IUsersBI> {

	public DAOUsers() {
		super(IUsersBI.class);
	}

	public Users getUsersByApiKey(String apikey) {
		return handler((Handle handle) -> binder(handle).getUsersByApiKey(apikey));
	}

	public Users getUsersById(Long id) {
		return handler((Handle handle) -> binder(handle).getUsersById(id));
	}
}
