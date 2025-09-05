package com.develop.job.db.dao.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.help.job.core.AbstractLocalRefCacheList;
import com.develop.job.db.dao.DAOProduct;
import com.develop.job.db.entity.Product;

@Service
public class ProductCache extends AbstractLocalRefCacheList<String, Long, Product>{

	@Autowired
	private DAOProduct daoProd;
	
	@Override
	protected List<Product> loadValues(String paramR) {
		return daoProd.getAllProductByCustomerId(getCustomerIdByRef(paramR));
	}

	@Override
	protected Long mapKey(Product paramV) {
		return paramV.getIdProduct();
	}

	@Override
	protected Product loadValue(Long paramK) {
		return daoProd.getByProductId(paramK);
	}

	@Override
	protected String getCacheName() {
		return getClass().getName();
	}
	
	public Product getProductById(Long id) {
		return getByKey(id);
	}
	
	public List<Product> getAllProductByCustomerId(Long customerId){
		return getByReference(getRefProductbyCustomerId(customerId));
	}

	private String getRefProductbyCustomerId(Long id) {
		return "listProductCustomer#"+id;
	}
	
	private Long getCustomerIdByRef(String ref) {
		String[] sp = ref.split("#");
		return Long.valueOf(sp[1]);
	}
}
