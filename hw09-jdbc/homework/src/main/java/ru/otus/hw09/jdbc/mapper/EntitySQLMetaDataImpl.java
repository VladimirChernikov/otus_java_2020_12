package ru.otus.hw09.jdbc.mapper;

import static ru.otus.hw09.jdbc.mapper.TextUtils.getRefStr;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Создает SQL - запросы
 */
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    public EntitySQLMetaDataImpl() {
    }

	@Override
	public String getSelectAllSql( EntityMetaData entityMetaData ) {
        final List<String> fields = getFieldNames( this.getSelectAllSqlFields( entityMetaData ) );
        StringBuilder sb = new StringBuilder();
        sb.append( "select " );
        sb.append( getRefStr( "", "\n", fields, "", ", ", "" ) );
        sb.append( " from " );
        sb.append( entityMetaData.getName() );
		return sb.toString();
	}

	@Override
	public String getSelectByIdSql( EntityMetaData entityMetaData ) {
        final List<String> fields = getFieldNames( this.getSelectByIdSqlFields( entityMetaData ) );
        StringBuilder sb = new StringBuilder();
        sb.append( "select " );
        sb.append( getRefStr( "", "\n", fields, "", ", ", "" ) );
        sb.append( " from " );
        sb.append( entityMetaData.getName() );
        sb.append( " where " );
        sb.append( entityMetaData.getIdField().getName() );
        sb.append( " = ?" );
		return sb.toString();
	}

	@Override
	public String getInsertSql( EntityMetaData entityMetaData ) {
        final List<String> fields = getFieldNames( this.getInsertSqlFields( entityMetaData ) );
        StringBuilder sb = new StringBuilder();
        sb.append( "insert into " );
        sb.append( entityMetaData.getName() );
        sb.append( getRefStr( "( ", "\n", fields, "", ", ", " )" ) );
        sb.append( " values " );
        sb.append( getRefStr( "( ", "\n", "?", fields.size(), "", ", ", " )" ) );
		return sb.toString();
	}

	@Override
	public String getUpdateSql( EntityMetaData entityMetaData ) {
        final List<String> fields = getFieldNames( this.getUpdateSqlFields( entityMetaData ) );
        StringBuilder sb = new StringBuilder();
        sb.append( "update " );
        sb.append( entityMetaData.getName() );
        sb.append( " set " );
        sb.append( getRefStr( "", "\n", fields, " = ?", ", ", "" ) );
        sb.append( " where " );
        sb.append( entityMetaData.getIdField().getName() );
        sb.append( " = ?" );
		return sb.toString();
	}

	@Override
	public List<Field> getSelectAllSqlFields( EntityMetaData entityMetaData ) {
        return entityMetaData.getAllFields();
	}

	@Override
	public List<Field> getSelectByIdSqlFields( EntityMetaData entityMetaData ) {
        return entityMetaData.getAllFields();
	}

	@Override
	public List<Field> getInsertSqlFields( EntityMetaData entityMetaData ) {
		return entityMetaData.getFieldsWithoutId();
	}

	@Override
	public List<Field> getUpdateSqlFields( EntityMetaData entityMetaData ) {
		return entityMetaData.getFieldsWithoutId();
	}

    private List<String> getFieldNames( List<Field> fields ) {
        return fields.stream().map( Field::getName ).collect(Collectors.toList());
    }

}
