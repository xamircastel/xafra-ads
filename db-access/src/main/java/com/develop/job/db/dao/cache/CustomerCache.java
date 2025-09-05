package com.develop.job.db.dao.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.help.job.core.AbstractLocalRefCacheList;
import com.develop.job.db.dao.DAOCustomer;
import com.develop.job.db.entity.Customer;

@Service
public class CustomerCache extends AbstractLocalRefCacheList<String, Long, Customer> {
	
	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(CustomerCache.class);
	
	@Autowired
	private DAOCustomer daoCustomer;

	@Override
	protected List<Customer> loadValues(String paramR) {
		return daoCustomer.allCustomer();
	}

	@Override
	protected Long mapKey(Customer paramV) {
		return paramV.getId();
	}

	@Override
	protected Customer loadValue(Long customerId) {
		return daoCustomer.byCustomerId(customerId);
	}

	@Override
	protected String getCacheName() {
		return getClass().getName();
	}

	public List<Customer> getAllCustomer() {
		return getByReference("listCustomer");
	}
	
	public Customer getByCustomerId(Long customerId) {
		return getByKey(customerId);
	}

}
