package com.example.demo.dao;

import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import com.example.demo.entity.Product;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ProductDao extends GenericDaoHibernateImpl<Product, Serializable> {

    public List<Product> getSearchedProduct(SearchCriteria searchCriteria) {
        String hql = "from Product product where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getProductCategoryId()!=null) {
                hql+= " and product.productCategory.id=:proCatId";
            }
            if(searchCriteria.getId()!=null) {
                hql+= " and product.id=:proId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and product.productName like :proName";
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                hql+= " and product.productDesc like :proDesc";
            }
            if(searchCriteria.getActive()!=null) {
                hql+= " and product.active=:active";
            }
            if(searchCriteria.getAvailable()!=null) {
                hql+= " and product.available=:available";
            }
        }
        hql += " order by product.productName";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getProductCategoryId()!=null) {
                query.setParameter("proCatId", searchCriteria.getProductCategoryId());
            }
            if(searchCriteria.getId()!=null) {
                query.setParameter("proId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("proName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                query.setParameter("proDesc", searchCriteria.getDescription() + "%");
            }
            if(searchCriteria.getActive()!=null) {
                query.setParameter("active", searchCriteria.getActive());
            }
            if(searchCriteria.getAvailable()!=null) {
                query.setParameter("available", searchCriteria.getAvailable());
            }
            if(searchCriteria.getResultSetSize()!=null) {
                query.setMaxResults(searchCriteria.getResultSetSize());
            }
        }
        return query.getResultList();
    }
}
