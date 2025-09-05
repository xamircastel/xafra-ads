package com.develop.job;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import com.develop.job.auth.filter.AuthenticationHeaderFilter;
import com.develop.job.db.conf.PropertiesDB;

@Configuration
@ComponentScan(basePackages = "com.develop.job.*")
public class Configurations {

	@Autowired
	private PropertiesDB confdb;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(confdb.getDriverManager());
		ds.setUrl(confdb.getUrl());
		ds.setUsername(confdb.getUser());
		ds.setPassword(confdb.getPass());

		return ds;
	}

	@Bean
	public Jdbi jdbi(DataSource datasource) {
		return Jdbi.create(datasource).installPlugin(new SqlObjectPlugin());
	}

	@Bean("AutenticationHeaders")
	public FilterRegistrationBean<AuthenticationHeaderFilter> headerFilter() {
		FilterRegistrationBean<AuthenticationHeaderFilter> registrationBean = new FilterRegistrationBean<>();
		//registrationBean.setName("AutenticationHeaders");
		registrationBean.setFilter(new AuthenticationHeaderFilter());
		registrationBean.addUrlPatterns("/ads/confirm/*");

		return registrationBean;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
