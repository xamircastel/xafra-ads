package com.develop.job.db.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

	private Long idProduct;
	private String reference;
	private String name;
	private String urlRedirectSuccess;
	private Boolean active;
	private Long idCustomer;
	private String urlRedirectPostBack;
	private String methodPostBack;
	private String bodyPostBack;
	
	// ===== NUEVOS CAMPOS: PAÍS Y OPERADOR =====
	private String country;      // País del producto
	private String operator;     // Operador móvil asociado
	private Boolean random;      // Si participa en redirección inteligente
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode())).append("@Product[id=");
		b.append(idProduct);
		b.append(", reference=").append(reference);
		b.append(",name=").append(name);
		b.append(", urlSuccess=").append(urlRedirectSuccess);
		b.append(", active=").append(active);
		b.append(",idCustomer=").append(idCustomer);
		b.append(", urlRedirectPostBack=").append(urlRedirectPostBack);
		b.append(", methodPostBack=").append(methodPostBack);
		b.append(", bodyPostBack=").append(bodyPostBack);
		b.append(", country=").append(country);
		b.append(", operator=").append(operator);
		b.append(", random=").append(random);
		b.append("]");
		return b.toString();
	}
}
