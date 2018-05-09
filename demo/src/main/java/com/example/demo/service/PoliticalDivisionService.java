package com.example.demo.service;

import com.example.demo.dao.MunicipalityDao;
import com.example.demo.dao.StateDao;
import com.example.demo.entity.Municipality;
import com.example.demo.entity.State;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PoliticalDivisionService {

    @Autowired
    private StateDao stateDao;

    @Autowired
    private MunicipalityDao municipalityDao;

    public List<State> getSearchedState(SearchCriteria searchCriteria) {
        return stateDao.getSearchedState(searchCriteria);
    }

    public List<Municipality> getSearchedMunicipality(SearchCriteria searchCriteria) {
        return municipalityDao.getSearchedMunicipality(searchCriteria);
    }
}
