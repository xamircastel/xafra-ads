package com.develop.job.db.entity;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

	@ColumnName("id_customer")
	private Long id;
	private String name;
	@ColumnName("short_name")
	private String shortName;
	private String mail;
	private String phone;
	
	// ===== NUEVOS CAMPOS: PAÍS Y OPERADOR =====
	private String country;      // País principal del customer
	private String operator;     // Operador móvil principal
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode()));
		b.append("@Customer[id=").append(id).append(", name=").append(name);
		b.append(", short_name=").append(shortName).append(", mail=").append(mail);
		b.append(". phone=").append(phone);
		b.append(", country=").append(country);
		b.append(", operator=").append(operator);
		b.append("]");
		return b.toString();
	}
}
