package com.example.demo.service;

import com.example.demo.dao.OrderDetailDao;
import com.example.demo.entity.CustomerOrderDetail;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    public List<CustomerOrderDetail> getSearchedOrderDetail(SearchCriteria searchCriteria) {
        return orderDetailDao.getSearchedOrderDetail(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void saveOrderDetail(CustomerOrderDetail customerOrderDetail) {
        orderDetailDao.save(customerOrderDetail);
    }

    @Transactional(readOnly = false)
    public void updateOrderDetail(CustomerOrderDetail customerOrderDetail) {
        orderDetailDao.update(customerOrderDetail);
    }

    @Transactional(readOnly = false)
    public void deleteOrderDetail(Long id) {
        orderDetailDao.delete(id);
    }

}
