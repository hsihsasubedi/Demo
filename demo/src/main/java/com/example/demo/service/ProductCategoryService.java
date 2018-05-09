package com.example.demo.service;

import com.example.demo.dao.ProductDao;
import com.example.demo.utils.SearchCriteria;
import com.example.demo.dao.ProductCategoryDao;
import com.example.demo.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductCategoryService {

    private List<ProductCategory> productCategoryList;
    private SearchCriteria searchCriteria;

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    public List<ProductCategory> getSearchedProductCategory(SearchCriteria searchCriteria) {
        return productCategoryDao.getSearchedProductCategory(searchCriteria);
    }

    public List<ProductCategory> getProductCategoryForDashboard() {
        productCategoryList = productCategoryDao.getSearchedProductCategory(null);
        productCategoryList.parallelStream().forEach(proCat -> {
            searchCriteria = new SearchCriteria();
            searchCriteria.setProductCategoryId(proCat.getId());
            searchCriteria.setAvailable(true);
            searchCriteria.setResultSetSize(3);
            proCat.setProductList(productDao.getSearchedProduct(searchCriteria));

        });
        return productCategoryList;
    }

    @Transactional(readOnly = false)
    public void saveProductCategory(ProductCategory productCategory) {
        productCategoryDao.save(productCategory);
    }

    @Transactional(readOnly = false)
    public void updateProductCategory(ProductCategory productCategory) {
        productCategoryDao.update(productCategory);
    }

    @Transactional(readOnly = false)
    public void deleteProductCategory(Long id) {
        productCategoryDao.delete(id);
    }

}
