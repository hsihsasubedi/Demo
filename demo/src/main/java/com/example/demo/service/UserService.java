package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> getSearchedUser(SearchCriteria searchCriteria) {
        return userDao.getSearchedUser(searchCriteria);
    }

    @Transactional(readOnly = false)
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Transactional(readOnly = false)
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Transactional(readOnly = false)
    public void deleteUser(Long id) {
        userDao.delete(id);
    }

}
