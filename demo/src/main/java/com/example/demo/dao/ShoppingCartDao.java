package com.example.demo.dao;

import com.example.demo.entity.ShoppingCart;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ShoppingCartDao extends GenericDaoHibernateImpl<ShoppingCart, Serializable> {

    public List<ShoppingCart> getSearchedShoppingCart(SearchCriteria searchCriteria) {
        String hql = "from ShoppingCart shoppingCart where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                hql+= " and shoppingCart.id=:shoppingCartId";
            }
            if(searchCriteria.getCustomerId() != null) {
                hql+= " and shoppingCart.customer.id=:customerId";
            }
        }
        hql += " order by shoppingCart.cartAddedDate desc";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                query.setParameter("shoppingCartId", searchCriteria.getId());
            }
            if(searchCriteria.getCustomerId() != null) {
                query.setParameter("customerId", searchCriteria.getCustomerId());
            }
        }
        return query.getResultList();
    }

    public void updateShoppingCartQuantity(List<ShoppingCart> shoppingCartList) {
        String hql = "update ShoppingCart shoppingCart set shoppingCart.quantity=:newQuantity"
                + " where shoppingCart.id=:shoppingCartId";
        Query query = currentSession().createQuery(hql);
        shoppingCartList.forEach(shoppingCart -> {
            query.setParameter("newQuantity", shoppingCart.getQuantity());
            query.setParameter("shoppingCartId", shoppingCart.getId());
            query.executeUpdate();
        });
    }
}
