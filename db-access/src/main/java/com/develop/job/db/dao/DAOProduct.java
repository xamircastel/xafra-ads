package com.develop.job.db.dao;

import java.util.List;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.Product;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.IProductBI;

@Service
public class DAOProduct extends BaseJbdiDao<IProductBI> {

	public DAOProduct() {
		super(IProductBI.class);
	}

	public Product getByProductId(Long id) {
		return handler((Handle handle) -> binder(handle).byProductId(id));
	}

	public List<Product> getAllProductByCustomerId(Long idCustomer) {
		return handler((Handle handle) -> binder(handle).allProductsByCustomerId(idCustomer));
	}

	public List<Product> getRandomProductsByCustomer(Long customerId) {
		return handler((Handle handle) -> binder(handle).getRandomProductsByCustomer(customerId));
	}
}
