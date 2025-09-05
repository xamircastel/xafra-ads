package com.develop.job.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class BuyModel implements Serializable {		
	private static final long serialVersionUID = -6706557856596193595L;	
	private Integer id;
	private String nombre;
	private boolean status;
	
	public BuyModel() {}
	public BuyModel(Integer id, String nombre, boolean status) {
		this.id= id;
		this.nombre= nombre;
		this.status=status;
	}
	
	@Override
	public String toString() {
		StringBuilder b= new StringBuilder();
		b.append("BuyModel [id=").append(id).append(", nombre=")
		.append(nombre).append(", status=").append(status).append("]");
		return b.toString();
	}
}
