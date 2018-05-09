package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class UserDao extends GenericDaoHibernateImpl<User, Serializable> {

    public List<User> getSearchedUser(SearchCriteria searchCriteria) {
        String hql = "from User user where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and user.id=:userId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and user.name like :userName";
            }
            if(searchCriteria.getUserType()!=null && !searchCriteria.getUserType().isEmpty()) {
                hql+= " and user.userType=:userType";
            }
            if(searchCriteria.getEmail()!=null && !searchCriteria.getEmail().isEmpty()) {
                hql+= " and user.emailId=:userEmail";
            }
            if(searchCriteria.getPassword()!=null && !searchCriteria.getPassword().toString().isEmpty()) {
                hql+= " and user.password=:userPassword";
            }
            if(searchCriteria.getContactNo()!=null && !searchCriteria.getContactNo().isEmpty()) {
                hql+= " and user.contactNo like :userContact";
            }
            if(searchCriteria.getActive()!=null) {
                hql+= " and user.active=:userActive";
            }
        }
        hql += " order by user.name";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("userId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("userName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getUserType()!=null && !searchCriteria.getUserType().isEmpty()) {
                query.setParameter("userType", searchCriteria.getUserType());
            }
            if(searchCriteria.getEmail()!=null && !searchCriteria.getEmail().isEmpty()) {
                query.setParameter("userEmail", searchCriteria.getEmail());
            }
            if(searchCriteria.getPassword()!=null && !searchCriteria.getPassword().toString().isEmpty()) {
                query.setParameter("userPassword", searchCriteria.getPassword().toString());
            }
            if(searchCriteria.getContactNo()!=null && !searchCriteria.getContactNo().isEmpty()) {
                query.setParameter("userContact", searchCriteria.getContactNo() + "%");
            }
            if(searchCriteria.getActive()!=null) {
                query.setParameter("userActive", searchCriteria.getActive());
            }
        }
        return query.getResultList();
    }
}
