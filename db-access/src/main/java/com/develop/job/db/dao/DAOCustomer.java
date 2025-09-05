package com.develop.job.db.dao;

import java.util.List;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.Customer;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.ICustomerBI;

@Service
public class DAOCustomer extends BaseJbdiDao<ICustomerBI> {

	public DAOCustomer() {
		super(ICustomerBI.class);
	}
	
	public List<Customer> allCustomer(){
		return handler((Handle handle)->binder(handle).allCustomer());
	}

	public Customer byCustomerId(Long customerId) {
		return handler((Handle handle)->binder(handle).byCustomerId(customerId));
	}
}
