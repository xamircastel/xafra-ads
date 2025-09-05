package com.develop.job.db.dao.bi;

import com.develop.job.db.entity.Users;

public interface IUsers {

	public Users getUsersByApiKey(String apikey);
}
