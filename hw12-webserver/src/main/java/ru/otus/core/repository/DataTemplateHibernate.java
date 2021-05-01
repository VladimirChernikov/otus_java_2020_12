package ru.otus.core.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }

    @Override
    public List<T> findByField(Session session, String fieldName, Object value) {
        List<T> result; 

        // get column name of field
        String[] fieldColumnNames = ((AbstractEntityPersister)((MetamodelImplementor)session.getSessionFactory().getMetamodel()).entityPersister(clazz)).getPropertyColumnNames(fieldName);

        if ( fieldColumnNames != null && fieldColumnNames.length > 0 ) {

            String columnName = fieldColumnNames[0];

            var entityManager = session.getEntityManagerFactory().createEntityManager();
            var builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(clazz);
            Root<T> root = criteria.from(clazz);
            criteria.select(root);
            criteria.where(builder.equal(root.get(columnName), value));

            result = entityManager.createQuery( criteria ).getResultList();
        }
        else {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }
}
