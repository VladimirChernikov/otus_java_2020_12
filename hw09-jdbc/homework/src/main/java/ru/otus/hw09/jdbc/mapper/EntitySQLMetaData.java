package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData extends EntityMetaData {
    String getSelectAllSql();
    List<Field> getSelectAllSqlFields();

    String getSelectByIdSql();
    List<Field> getSelectByIdSqlFields();

    String getInsertSql();
    List<Field> getInsertSqlFields();

    String getUpdateSql();
    List<Field> getUpdateSqlFields();
}
