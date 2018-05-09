package com.example.demo.dao;

import com.example.demo.entity.WishList;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class WishListDao extends GenericDaoHibernateImpl<WishList, Serializable> {

    public List<WishList> getSearchedWishList(SearchCriteria searchCriteria) {
        String hql = "from WishList wishList where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                hql+= " and wishList.id=:wishListId";
            }
            if(searchCriteria.getCustomerId() != null) {
                hql+= " and wishList.customer.id=:customerId";
            }
        }
        hql += " order by wishList.wishlistAddedDate desc";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId() != null) {
                query.setParameter("wishListId", searchCriteria.getId());
            }
            if(searchCriteria.getCustomerId() != null) {
                query.setParameter("customerId", searchCriteria.getCustomerId());
            }
        }
        return query.getResultList();
    }
}
