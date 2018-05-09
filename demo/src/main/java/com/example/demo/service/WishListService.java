package com.example.demo.service;

import com.example.demo.dao.WishListDao;
import com.example.demo.entity.WishList;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WishListService {

    @Autowired
    private WishListDao wishListDao;

    public List<WishList> getSearchedWishList(SearchCriteria searchCriteria) {
        return wishListDao.getSearchedWishList(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void saveWishList(WishList wishList) {
        wishListDao.save(wishList);
    }

    @Transactional(readOnly = false)
    public void updateWishList(WishList wishList) {
        wishListDao.update(wishList);
    }

    @Transactional(readOnly = false)
    public void deleteWishList(Long id) {
        wishListDao.delete(id);
    }

}
