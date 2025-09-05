package com.develop.job.jdbi.bi;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.develop.job.db.entity.Product;

public interface IProductBI {

	@SqlQuery("Select * from products where id_product=:product_id and active=1")
	@RegisterFieldMapper(Product.class)
	public Product byProductId(@Bind("product_id") Long id);
	
	@SqlQuery("Select * from products where id_customer=:customer_id")
	@RegisterFieldMapper(Product.class)
	public List<Product> allProductsByCustomerId(@Bind("customer_id") Long cusromerId);
}
