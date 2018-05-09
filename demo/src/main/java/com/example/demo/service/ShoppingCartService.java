package com.example.demo.service;

import com.example.demo.dao.ShoppingCartDao;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    public List<ShoppingCart> getSearchedShoppingCart(SearchCriteria searchCriteria) {
        return shoppingCartDao.getSearchedShoppingCart(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void updateShoppingCartQuantity(List<ShoppingCart> shoppingCartList) {
        shoppingCartDao.updateShoppingCartQuantity(shoppingCartList);
    }

    @Transactional(readOnly = false)
    public void saveShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartDao.save(shoppingCart);
    }

    @Transactional(readOnly = false)
    public void updateShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartDao.update(shoppingCart);
    }

    @Transactional(readOnly = false)
    public void deleteShoppingCart(Long id) {
        shoppingCartDao.delete(id);
    }

}
