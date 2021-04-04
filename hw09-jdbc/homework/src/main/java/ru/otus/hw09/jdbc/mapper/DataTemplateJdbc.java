package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        final String statementText     = entitySQLMetaData.getSelectByIdSql();
        final List<Field> resultFileds = entitySQLMetaData.getSelectByIdSqlFields();
        return dbExecutor.executeSelect(
                connection
                , statementText
                , List.of( id )
                , rs -> {
                    try {
                        if (rs.next()) {
                            return this.createObject( resultFileds, rs );
                        }
                        return null;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        final String statementText     = entitySQLMetaData.getSelectAllSql();
        final List<Field> resultFileds = entitySQLMetaData.getSelectAllSqlFields();
        return dbExecutor.executeSelect(
                connection
                , statementText
                , Collections.emptyList()
                , rs -> {
                    try {
                        List<T> result = new ArrayList<>();
                        while (rs.next()) {
                            result.add( this.createObject(resultFileds, rs) );
                        }
                        return result;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                }).get();
    }

    @Override
    public long insert(Connection connection, T client) {
        final String statementText     = entitySQLMetaData.getInsertSql();
        final List<Field> resultFileds = entitySQLMetaData.getInsertSqlFields();
        return dbExecutor.executeStatement(
                connection
                , statementText
                , resultFileds.stream().map( f -> this.getFieldValue(f, client) ).collect(Collectors.toList()) 
                );
    }

    @Override
    public void update(Connection connection, T client) {
        final String statementText     = entitySQLMetaData.getUpdateSql();
        final List<Field> resultFileds = entitySQLMetaData.getUpdateSqlFields();
        final List<Object> params = resultFileds.stream().map( f -> this.getFieldValue(f, client) ).collect(Collectors.toList());
        params.add( this.getFieldValue( entitySQLMetaData.getIdField(), client ) );
        dbExecutor.executeStatement(
                connection
                , statementText
                , params
                );
    }

    private T createObject( List<Field> fields, ResultSet rs ) {
        T client;
		try {
			client = (T)entitySQLMetaData.getConstructor().newInstance();
            for (int i = 0; i < fields.size(); i++) {
                this.setFieldValue( fields.get(i), client, rs.getObject(i+1) );
            }
        } catch(Exception e){
            throw new DataTemplateException(e);
        }
        return client;
    }

    private Object getFieldValue( Field field, T client ) {
        Object result = null;
        try {
            result = field.get( client );
        } catch(Exception e){
            throw new DataTemplateException(e);
        }
        return result;
    }

    private void setFieldValue( Field field, T client, Object value ) {
        try {
            field.set( client, value );
        } catch(Exception e){
            throw new DataTemplateException(e);
        }
    }

}
