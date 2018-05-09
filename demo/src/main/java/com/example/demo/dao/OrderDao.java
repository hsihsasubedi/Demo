package com.example.demo.dao;

import com.example.demo.entity.CustomerOrder;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class OrderDao extends GenericDaoHibernateImpl<CustomerOrder, Serializable> {

    public List<CustomerOrder> getSearchedOrder(SearchCriteria searchCriteria) {
        String hql = "from CustomerOrder ord where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                hql+= " and ord.id=:orderId";
            }
            if(searchCriteria.getCustomerId() != null) {
                hql+= " and ord.customer.id=:customerId";
            }
            if(searchCriteria.getStatus() != null) {
                hql+= " and ord.orderStatus=:orderStatus";
            }
        }
        hql += " order by ord.orderDate desc";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                query.setParameter("orderId", searchCriteria.getId());
            }
            if(searchCriteria.getCustomerId() != null) {
                query.setParameter("customerId", searchCriteria.getCustomerId());
            }
            if(searchCriteria.getStatus() != null) {
                query.setParameter("orderStatus", searchCriteria.getStatus());
            }
        }
        return query.getResultList();
    }

    public List<CustomerOrder> getOrdersForCustomer(SearchCriteria searchCriteria) {
        String hql = "select new CustomerOrder(id, orderPrice, orderDate, deliveryDate, orderStatus)"
                + " from CustomerOrder ord where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                hql+= " and ord.id=:orderId";
            }
            if(searchCriteria.getCustomerId() != null) {
                hql+= " and ord.customer.id=:customerId";
            }
            if(searchCriteria.getStatus() != null) {
                hql+= " and ord.orderStatus=:orderStatus";
            }
        }
        hql += " order by ord.orderDate desc";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                query.setParameter("orderId", searchCriteria.getId());
            }
            if(searchCriteria.getCustomerId() != null) {
                query.setParameter("customerId", searchCriteria.getCustomerId());
            }
            if(searchCriteria.getStatus() != null) {
                query.setParameter("orderStatus", searchCriteria.getStatus());
            }
        }
        return query.getResultList();
    }

    public int getProcessedOrderCount(SearchCriteria searchCriteria) {
        String hql = "select count(ord.id) from CustomerOrder ord"
                + " where ord.customer.id=:customerId"
                + " and ord.orderStatus='PROCESSED'";
        Query query = currentSession().createQuery(hql);
        query.setParameter("customerId", searchCriteria.getCustomerId());
        return ((Long)query.uniqueResult()).intValue();
    }

    public int updateProcessedOrderToCompleted() {
        String sql = "update customer_order set order_status='COMPLETED'" +
                " where delivery_date < NOW() and order_status='PROCESSED'";
        Query query = currentSession().createNativeQuery(sql);
        return query.executeUpdate();
    }

}
