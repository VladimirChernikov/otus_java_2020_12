package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.otus.crm.model.annotation.Id;

/**
 * "Разбирает" объект на составные части
 */
public abstract class AbstractEntityMetaData implements EntityMetaData {

    private final Class<?> ID_ANNOTATION_CLASS = Id.class;
    private final Class<?> clazz;
    private final String className;
    private final List<Field> allFields;
    private final List<Field> allFieldsWithoutId;
    private final Field idField;

    public AbstractEntityMetaData( Class<?> clazz ) {
        this.clazz = clazz;
        this.className = this.clazz.getSimpleName();
        this.allFields = Arrays.asList( clazz.getFields() );
        this.idField = this.findIdField( this.allFields );
        this.allFieldsWithoutId = this.allFields.stream().filter( f -> !f.equals(this.idField) ).collect(Collectors.toList());
    }

    @Override
    public Constructor<?> getConstructor() {
        Constructor<?> result = null;
        try {
            result = this.clazz.getConstructor();
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getName() {
        return this.className;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.allFieldsWithoutId;
    }

    private Field findIdField( List<Field> fields ) {
        for ( var field : fields )  {
            for ( var annotation : field.getAnnotations() )  {
                if ( annotation.annotationType().equals( ID_ANNOTATION_CLASS ) ) {
                    return field;
                }
            }
        }
        throw new RuntimeException( String.format( "Could not find ID field within %s", fields.toString() ));
    }

}
