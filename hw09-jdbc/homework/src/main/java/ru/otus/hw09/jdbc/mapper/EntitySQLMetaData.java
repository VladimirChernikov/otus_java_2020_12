package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData {
    String getSelectAllSql( EntityMetaData entityMetaData );
    List<Field> getSelectAllSqlFields( EntityMetaData entityMetaData );

    String getSelectByIdSql( EntityMetaData entityMetaData );
    List<Field> getSelectByIdSqlFields( EntityMetaData entityMetaData );

    String getInsertSql( EntityMetaData entityMetaData );
    List<Field> getInsertSqlFields( EntityMetaData entityMetaData );

    String getUpdateSql( EntityMetaData entityMetaData );
    List<Field> getUpdateSqlFields( EntityMetaData entityMetaData );
}
