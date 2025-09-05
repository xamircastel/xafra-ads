package com.develop.job.db.dao.bi;

import java.util.List;

import com.develop.job.db.entity.Customer;
import com.develop.job.db.entity.Product;

public interface IProductAndCustomer {

	public Product getProductById(Long id);

	public List<Product> getAllProductByCustomerId(Long customerId);

	public List<Customer> getAllCustomer();

	public Customer getCustomerById(Long customerId);
}
