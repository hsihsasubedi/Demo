package com.example.demo.dao;

import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import com.example.demo.entity.ProductCategory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ProductCategoryDao extends GenericDaoHibernateImpl<ProductCategory, Serializable> {

    public List<ProductCategory> getSearchedProductCategory(SearchCriteria searchCriteria) {
        String hql = "from ProductCategory productCategory where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and productCategory.id=:proCatId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and productCategory.categoryName like :proCatName";
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                hql+= " and productCategory.categoryDesc like :proCatDesc";
            }
        }
        hql += " order by productCategory.categoryName";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("proCatId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("proCatName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                query.setParameter("proCatDesc", searchCriteria.getDescription() + "%");
            }
        }
        return query.getResultList();
    }
}
