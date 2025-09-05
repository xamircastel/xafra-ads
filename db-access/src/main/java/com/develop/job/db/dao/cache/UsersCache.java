package com.develop.job.db.dao.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.help.job.core.AbstractLocalCache;
import com.develop.job.db.dao.DAOUsers;
import com.develop.job.db.entity.Users;

@Service
public class UsersCache extends AbstractLocalCache<String, Users> {

	@Autowired
	private DAOUsers users;

	@Override
	protected String mapKey(Users paramV) {
		return paramV.getApiKey();
	}

	@Override
	protected Users loadValue(String paramK) {
		return users.getUsersByApiKey(paramK);
	}

	@Override
	protected String getCacheName() {
		return UsersCache.class.getName();
	}

	public Users getUsersByApiKey(String apikey) {
		return getByKey(apikey);
	}
}
