package com.develop.job.db.entity;

import java.util.Date;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {

	@ColumnName("id_auth")
	private Long id;
	@ColumnName("user_name")
	private String name;
	@ColumnName("shared_key")
	private String sharedKey;
	@ColumnName("api_key")
	private String apiKey;
	private Boolean active;
	@ColumnName("creation_date")
	private Date creationDate;

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode()));
		b.append("@Users[id=").append(id);
		b.append(", name=").append(name);
		b.append(", shared_key=").append(sharedKey);
		b.append(", api_key=").append(apiKey);
		b.append(", active=").append(active);
		b.append(", creation_date=").append(creationDate);
		b.append("]");
		return b.toString();
	}
}
