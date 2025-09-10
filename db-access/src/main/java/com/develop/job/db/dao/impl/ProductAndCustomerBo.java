package com.develop.job.db.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.develop.job.db.dao.bi.IProductAndCustomer;
import com.develop.job.db.dao.cache.CustomerCache;
import com.develop.job.db.dao.cache.ProductCache;
import com.develop.job.db.entity.Customer;
import com.develop.job.db.entity.Product;

@Service
public class ProductAndCustomerBo implements IProductAndCustomer {

	@Autowired
	private ProductCache daoProd;

	@Autowired
	private CustomerCache daoCustomer;

	@Override
	public Product getProductById(Long id) {
		return daoProd.getProductById(id);
	}

	@Override
	public List<Product> getAllProductByCustomerId(Long customerId) {
		return daoProd.getAllProductByCustomerId(customerId);
	}

	@Override
	public List<Customer> getAllCustomer() {
		return daoCustomer.getAllCustomer();
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		return daoCustomer.getByCustomerId(customerId);
	}

	@Override
	public List<Product> getRandomProductsByCustomer(Long customerId) {
		return daoProd.getRandomProductsByCustomer(customerId);
	}

}
