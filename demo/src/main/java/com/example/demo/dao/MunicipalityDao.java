package com.example.demo.dao;

import com.example.demo.entity.Municipality;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class MunicipalityDao extends GenericDaoHibernateImpl<Municipality, Serializable> {

    public List<Municipality> getSearchedMunicipality(SearchCriteria searchCriteria) {
        String hql = "select new Municipality(id, name, wardCount)" +
                " from Municipality municipality where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and municipality.id=:municipalityId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and municipality.name like :municipalityName";
            }
            if(searchCriteria.getStateId()!=null && !searchCriteria.getStateId().equals(0)) {
                hql+= " and municipality.state.id=:stateId";
            }
        }
        hql += " order by municipality.name";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("municipalityId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("municipalityName", searchCriteria.getName() + "%");
            }
            if(searchCriteria.getStateId()!=null && !searchCriteria.getStateId().equals(0)) {
                query.setParameter("stateId", searchCriteria.getStateId());
            }
        }
        return query.getResultList();
    }
}
