package com.example.demo.dao;

import com.example.demo.entity.Customer;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class CustomerDao extends GenericDaoHibernateImpl<Customer, Serializable> {

    public List<Customer> getSearchedCustomer(SearchCriteria searchCriteria) {
        String hql = "from Customer customer where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and customer.id=:customerId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and customer.name like :customerName";
            }
            if(searchCriteria.getEmail()!=null && !searchCriteria.getEmail().isEmpty()) {
                hql+= " and customer.emailId=:customerEmail";
            }
            if(searchCriteria.getPassword()!=null && !searchCriteria.getPassword().toString().isEmpty()) {
                hql+= " and customer.password=:customerPassword";
            }
            if(searchCriteria.getContactNo()!=null && !searchCriteria.getContactNo().isEmpty()) {
                hql+= " and customer.contactNo like :customerContact";
            }
            if(searchCriteria.getActive()!=null) {
                hql+= " and customer.active=:customerActive";
            }
            if(searchCriteria.getPasswordToken()!=null && !searchCriteria.getPasswordToken().isEmpty()) {
                hql+= " and customer.passwordToken=:passwordToken";
            }
        }
        hql += " order by customer.name";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("customerId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("customerName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getEmail()!=null && !searchCriteria.getEmail().isEmpty()) {
                query.setParameter("customerEmail", searchCriteria.getEmail());
            }
            if(searchCriteria.getPassword()!=null && !searchCriteria.getPassword().toString().isEmpty()) {
                query.setParameter("customerPassword", searchCriteria.getPassword().toString());
            }
            if(searchCriteria.getContactNo()!=null && !searchCriteria.getContactNo().isEmpty()) {
                query.setParameter("customerContact", searchCriteria.getContactNo() + "%");
            }
            if(searchCriteria.getActive()!=null) {
                query.setParameter("customerActive", searchCriteria.getActive());
            }
            if(searchCriteria.getPasswordToken()!=null && !searchCriteria.getPasswordToken().isEmpty()) {
                query.setParameter("passwordToken", searchCriteria.getPasswordToken());
            }
        }
        return query.getResultList();
    }

    public int updateCustomerPassword(Customer customer) {
        String hql = "update Customer customer set customer.password=:password," +
                " customer.passwordToken=NULL" +
                " where customer.id=:customerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("password", customer.getPassword());
        query.setParameter("customerId", customer.getId());
        return query.executeUpdate();
    }

    public int updateCustomerProfile(Customer customer) {
        String hql = "update Customer customer set customer.name=:name," +
                " customer.emailId=:emailId, customer.contactNo=:contactNo" +
                " where customer.id=:customerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("name", customer.getName());
        query.setParameter("emailId", customer.getEmailId());
        query.setParameter("contactNo", customer.getContactNo());
        query.setParameter("customerId", customer.getId());
        return query.executeUpdate();
    }

    public int updateCustomerPaswordToken(Customer customer) {
        String hql = "update Customer customer set customer.passwordToken=:passwordToken"
                + " where customer.id=:customerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("passwordToken", customer.getPasswordToken());
        query.setParameter("customerId", customer.getId());
        return query.executeUpdate();
    }

    public int activateCustomer(String activationToken) {
        String hql = "update Customer customer set customer.active=true,"
                + " customer.passwordToken=NULL"
                + " where customer.passwordToken=:activationToken";
        Query query = currentSession().createQuery(hql);
        query.setParameter("activationToken", activationToken);
        return query.executeUpdate();
    }
}
