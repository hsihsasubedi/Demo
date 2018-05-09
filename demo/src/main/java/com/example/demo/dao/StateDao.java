package com.example.demo.dao;

import com.example.demo.entity.State;
import com.example.demo.utils.GenericDaoHibernateImpl;
import com.example.demo.utils.SearchCriteria;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class StateDao extends GenericDaoHibernateImpl<State, Serializable> {

    public List<State> getSearchedState(SearchCriteria searchCriteria) {
        String hql = "from State state where 1=1";
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                hql+= " and state.id=:stateId";
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                hql+= " and state.name like :stateName";
            }
        }
        hql += " order by state.name";
        Query query = currentSession().createQuery(hql);
        if (searchCriteria != null) {
            if(searchCriteria.getId()!=null) {
                query.setParameter("stateId", searchCriteria.getId());
            }
            if(searchCriteria.getName()!=null && !searchCriteria.getName().isEmpty()) {
                query.setParameter("stateName", searchCriteria.getName() + "%");
            }
        }
        return query.getResultList();
    }
}
