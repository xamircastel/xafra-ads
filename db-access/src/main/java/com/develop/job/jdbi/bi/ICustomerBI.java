package com.develop.job.jdbi.bi;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.develop.job.db.entity.Customer;

public interface ICustomerBI {

	@SqlQuery("SELECT * FROM customers")
	@RegisterFieldMapper(Customer.class)
	public List<Customer> allCustomer();
	
	@SqlQuery("SELECT * FROM customers where id_customer=:customerId")
	@RegisterFieldMapper(Customer.class)
	public Customer byCustomerId(@Bind("customerId") Long customerId);
	
	// ===== NUEVOS MÉTODOS: CONSULTAS POR PAÍS Y OPERADOR =====
	
	@SqlQuery("SELECT * FROM customers where country=:country")
	@RegisterFieldMapper(Customer.class)
	public List<Customer> allCustomersByCountry(@Bind("country") String country);
	
	@SqlQuery("SELECT * FROM customers where operator=:operator")
	@RegisterFieldMapper(Customer.class)
	public List<Customer> allCustomersByOperator(@Bind("operator") String operator);
	
	@SqlQuery("SELECT * FROM customers where country=:country and operator=:operator")
	@RegisterFieldMapper(Customer.class)
	public List<Customer> allCustomersByCountryAndOperator(@Bind("country") String country, @Bind("operator") String operator);
}
