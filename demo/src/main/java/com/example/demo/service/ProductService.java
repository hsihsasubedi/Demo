package com.example.demo.service;

import com.example.demo.utils.SearchCriteria;
import com.example.demo.dao.ProductDao;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getSearchedProduct(SearchCriteria searchCriteria) {
        return productDao.getSearchedProduct(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void saveProduct(Product product) {
        productDao.save(product);
    }

    @Transactional(readOnly = false)
    public void updateProduct(Product product) {
        productDao.update(product);
    }

    @Transactional(readOnly = false)
    public void deleteProduct(Long id) {
        productDao.delete(id);
    }

}
