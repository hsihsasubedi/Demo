package com.example.demo.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public abstract class GenericDaoHibernateImpl<T, PK extends Serializable> {

    private Class<T> persistentClass;

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory has not been set on DAO before usage");
        }
        return sessionFactory;
    }

    protected Session currentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public GenericDaoHibernateImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Transactional(readOnly = false)
    public void save(T transientObject) {
        currentSession().saveOrUpdate(transientObject);
    }

    @Transactional(readOnly = false)
    public void saveAll(List<T> transientObjectList) {
        transientObjectList.forEach((transientObject) -> {
            save(transientObject);
        });
    }

    @Transactional(readOnly = false)
    public T saveWithReturn(T transientObject) {
        return (T) currentSession().get(transientObject.getClass(), currentSession().save(transientObject));
    }

    public T read(PK id) {
        return (T) currentSession().get(persistentClass, id);
    }

    @Transactional(readOnly = false)
    public void update(T persistentObject) {
        currentSession().update(persistentObject);
    }

    @Transactional(readOnly = false)
    public void delete(T persistentObject) {
        currentSession().delete(persistentObject);
    }

    @Transactional(readOnly = false)
    public void delete(PK id) {
        delete(read(id));
    }

    @Transactional(readOnly = false)
    public void deleteAll(List<T> persistentObjectList) {
        persistentObjectList.forEach((persistentObject) -> {
            delete(persistentObject);
        });
    }
}