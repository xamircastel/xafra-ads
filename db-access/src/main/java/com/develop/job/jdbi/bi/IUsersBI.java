package com.develop.job.jdbi.bi;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.develop.job.db.entity.Users;

public interface IUsersBI {

	@SqlQuery("SELECT * FROM auth_users where api_key =:apikey and active=1")
	@RegisterFieldMapper(Users.class)
	public Users getUsersByApiKey(@Bind("apikey") String apikey);
	
	@SqlQuery("SELECT * FROM auth_users where id_auth =:id and active=1")
	@RegisterFieldMapper(Users.class)
	public Users getUsersById(@Bind("id") Long id);
}
