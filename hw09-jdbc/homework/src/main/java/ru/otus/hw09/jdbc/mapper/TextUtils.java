package ru.otus.hw09.jdbc.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {

    public static String getRefStr( String beforeAll, String prefix, List<String> fields, String postfix, String delimeter, String afterAll ) {
        return beforeAll + ( fields.stream().map(str -> prefix+str+postfix).collect(Collectors.joining(delimeter)) ) + afterAll;
    }

    public static String getRefStr( String beforeAll, String prefix, String field, int nCopiesOfField, String postfix, String delimeter, String afterAll ) {
        return getRefStr( beforeAll, prefix, Collections.nCopies(nCopiesOfField, field), postfix, delimeter, afterAll );
    }

}
