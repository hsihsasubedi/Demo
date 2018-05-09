package com.example.demo.dao;

import com.example.demo.entity.CustomerOrderDetail;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class OrderDetailDao extends GenericDaoHibernateImpl<CustomerOrderDetail, Serializable> {

    public List<CustomerOrderDetail> getSearchedOrderDetail(SearchCriteria searchCriteria) {
        String hql = "from OrderDetail orderDetail where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                hql+= " and orderDetail.id=:orderDetailId";
            }
            if(searchCriteria.getOrderId() != null) {
                hql+= " and orderDetail.order.id=:orderId";
            }
        }
        hql += " order by orderDetail.id";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                query.setParameter("orderDetailId", searchCriteria.getId());
            }
            if(searchCriteria.getOrderId() != null) {
                query.setParameter("orderId", searchCriteria.getOrderId());
            }
        }
        return query.getResultList();
    }
}
