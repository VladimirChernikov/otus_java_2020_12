package ru.otus.core.repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

public interface DataTemplate<T> {
    Optional<T> findById(Session session, long id);

    List<T> findAll(Session session);

    List<T> findByField(Session session, Field field, Object value);

    void insert(Session session, T object);

    void update(Session session, T object);
}
