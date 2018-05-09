package com.example.demo.service;

import com.example.demo.dao.OrderDao;
import com.example.demo.dao.ShoppingCartDao;
import com.example.demo.entity.CustomerOrder;
import com.example.demo.utils.SearchCriteria;
import com.example.demo.utils.SessionDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private SessionDataSet sessionDataSet;

    public List<CustomerOrder> getSearchedOrder(SearchCriteria searchCriteria) {
        return orderDao.getSearchedOrder(searchCriteria);
    }

    public List<CustomerOrder> getOrdersForCustomer(SearchCriteria searchCriteria) {
        return orderDao.getOrdersForCustomer(searchCriteria);
    }

    public int getProcessedOrderCount(SearchCriteria searchCriteria) {
        return orderDao.getProcessedOrderCount(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void saveOrder(CustomerOrder customerOrder) {
        orderDao.save(customerOrder);

        shoppingCartDao.deleteAll(sessionDataSet.getShoppingCart());

        //Add to SessionDataSet
        sessionDataSet.setProcessedOrderCount(sessionDataSet.getProcessedOrderCount()+1);
        sessionDataSet.setShoppingCart(new ArrayList<>());
        sessionDataSet.setShoppingCartSize(0);
    }

    @Transactional(readOnly = false)
    public int updateProcessedOrderToCompleted() {
        return orderDao.updateProcessedOrderToCompleted();
    }

    @Transactional(readOnly = false)
    public void updateOrder(CustomerOrder customerOrder) {
        orderDao.update(customerOrder);
    }

    @Transactional(readOnly = false)
    public void deleteOrder(Long id) {
        orderDao.delete(id);
    }

}
