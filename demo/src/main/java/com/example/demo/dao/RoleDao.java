package com.example.demo.dao;

import com.example.demo.entity.Role;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class RoleDao extends GenericDaoHibernateImpl<Role, Serializable> {

    public List<Role> getSearchedRole(SearchCriteria searchCriteria) {
        String hql = "from Role role where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and role.id=:roleId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and role.name like :roleName";
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                hql+= " and role.description like :roleDesc";
            }
        }
        hql += " order by role.name";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("roleId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("roleName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getDescription()!=null && !searchCriteria.getDescription().isEmpty()) {
                query.setParameter("roleDesc", searchCriteria.getDescription() + "%");
            }
        }
        return query.getResultList();
    }
}
