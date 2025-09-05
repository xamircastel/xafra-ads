package com.develop.job.jdbi;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.jdbi.v3.core.statement.Call;
import org.jdbi.v3.core.statement.SqlStatement;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizingAnnotation;

@SqlStatementCustomizingAnnotation(RegisterOutParameterId.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RegisterOutParameterId {
	public static final String RETURN_ID = "retId";

	int sqlType() default java.sql.Types.BIGINT;

	public class Factory implements SqlStatementCustomizerFactory {
		public SqlStatementCustomizer createForMethod(Annotation annotation, Class<?> sqlObjectType, Method method) {
			
			RegisterOutParameterId rop = (RegisterOutParameterId) annotation;
			final int sqlType = rop.sqlType();

			return new SqlStatementCustomizer() {

				@Override
				public void apply(SqlStatement<?> q) throws SQLException {
					Call c = (Call) q;
					c.registerOutParameter(RETURN_ID, sqlType);

				}
			};
		}
	}
}
