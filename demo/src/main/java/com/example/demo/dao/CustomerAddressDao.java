package com.example.demo.dao;

import com.example.demo.entity.CustomerAddress;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class CustomerAddressDao extends GenericDaoHibernateImpl<CustomerAddress, Serializable> {

    public List<CustomerAddress> getSearchedCustomerAddress(SearchCriteria searchCriteria) {
        String hql = "from CustomerAddress customerAddress where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and customerAddress.id=:customerAddressId";
            }
            if(searchCriteria.getAddressType()!=null && !searchCriteria.getAddressType().isEmpty()) {
                hql+= " and customerAddress.addressType=:addressType";
            }
            if(searchCriteria.getCustomerId()!=null && !searchCriteria.getCustomerId().equals(0)) {
                hql+= " and customerAddress.customerId=:customerId";
            }
        }
        hql += " order by customerAddress.addressType";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("customerAddressId", searchCriteria.getId());
            }
            if(searchCriteria.getAddressType()!=null && !searchCriteria.getAddressType().isEmpty()) {
                query.setParameter("addressType", searchCriteria.getAddressType());
            }
            if(searchCriteria.getCustomerId()!=null && !searchCriteria.getCustomerId().equals(0)) {
                query.setParameter("customerId", searchCriteria.getCustomerId());
            }
        }
        return query.getResultList();
    }
}
