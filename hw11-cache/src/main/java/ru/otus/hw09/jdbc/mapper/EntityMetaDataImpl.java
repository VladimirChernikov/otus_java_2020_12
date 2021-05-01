package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.otus.crm.model.annotation.Id;

/**
 * "Разбирает" объект на составные части
 */
public class EntityMetaDataImpl implements EntityMetaData {

    private static final Class<?> ID_FIELD_ANNOTATION_CLASS = Id.class;
    private final Class<?> clazz;
    private final String className;
    private final List<Field> allFields;
    private final List<Field> allFieldsWithoutId;
    private final Field idField;

    public EntityMetaDataImpl( Class<?> clazz ) {
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
        Field result = fields.stream().filter( f -> Stream.of(f.getAnnotations())
                                                             .filter( a -> a.annotationType()
                                                                            .equals( ID_FIELD_ANNOTATION_CLASS ) 
                                                                    ).findAny()
                                                                     .isPresent()
                                             ).findFirst().orElse(null);
        if ( result == null ) {
            throw new RuntimeException( String.format( "Could not find ID field within %s", fields.toString() ));
        }
        return result;
    }

}
