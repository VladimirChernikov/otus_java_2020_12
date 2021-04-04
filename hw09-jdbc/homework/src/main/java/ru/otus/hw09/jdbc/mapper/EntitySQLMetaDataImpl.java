package ru.otus.hw09.jdbc.mapper;

import static ru.otus.hw09.jdbc.mapper.TextUtils.getRefStr;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Создает SQL - запросы
 */
public class EntitySQLMetaDataImpl extends AbstractEntityMetaData implements EntitySQLMetaData {

    public EntitySQLMetaDataImpl( Class<?> clazz ) {
        super(clazz);
    }

	@Override
	public String getSelectAllSql() {
        final List<String> fields = getFieldNames( this.getSelectAllSqlFields() );
        StringBuilder sb = new StringBuilder();
        sb.append( "select " );
        sb.append( getRefStr( "", "\n", fields, "", ", ", "" ) );
        sb.append( " from " );
        sb.append( this.getName() );
		return sb.toString();
	}

	@Override
	public String getSelectByIdSql() {
        final List<String> fields = getFieldNames( this.getSelectByIdSqlFields() );
        StringBuilder sb = new StringBuilder();
        sb.append( "select " );
        sb.append( getRefStr( "", "\n", fields, "", ", ", "" ) );
        sb.append( " from " );
        sb.append( this.getName() );
        sb.append( " where " );
        sb.append( this.getIdField().getName() );
        sb.append( " = ?" );
		return sb.toString();
	}

	@Override
	public String getInsertSql() {
        final List<String> fields = getFieldNames( this.getInsertSqlFields() );
        StringBuilder sb = new StringBuilder();
        sb.append( "insert into " );
        sb.append( this.getName() );
        sb.append( getRefStr( "( ", "\n", fields, "", ", ", " )" ) );
        sb.append( " values " );
        sb.append( getRefStr( "( ", "\n", "?", fields.size(), "", ", ", " )" ) );
		return sb.toString();
	}

	@Override
	public String getUpdateSql() {
        final List<String> fields = getFieldNames( this.getUpdateSqlFields() );
        StringBuilder sb = new StringBuilder();
        sb.append( "update " );
        sb.append( this.getName() );
        sb.append( " set " );
        sb.append( getRefStr( "", "\n", fields, " = ?", ", ", "" ) );
        sb.append( " where " );
        sb.append( this.getIdField().getName() );
        sb.append( " = ?" );
		return sb.toString();
	}

	@Override
	public List<Field> getSelectAllSqlFields() {
        return this.getAllFields();
	}

	@Override
	public List<Field> getSelectByIdSqlFields() {
        return this.getAllFields();
	}

	@Override
	public List<Field> getInsertSqlFields() {
		return this.getFieldsWithoutId();
	}

	@Override
	public List<Field> getUpdateSqlFields() {
		return this.getFieldsWithoutId();
	}

    private List<String> getFieldNames( List<Field> fields ) {
        return fields.stream().map( Field::getName ).collect(Collectors.toList());
    }

}
