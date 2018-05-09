package com.example.demo.utils;

import com.example.demo.entity.Customer;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.entity.WishList;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SessionDataSet implements Serializable {

    private static final long serialVersionUID = 201802200012235L;

    private List<ShoppingCart> shoppingCart = new ArrayList<>();
    private List<WishList> wishListList = new ArrayList<>();
    private Customer customer;
    private boolean customerLoggedIn;
    private int shoppingCartSize;
    private int wishListSize;
    private int processedOrderCount;

    public SessionDataSet() {
        super();
    }

    public List<ShoppingCart> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<ShoppingCart> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<WishList> getWishListList() {
        return wishListList;
    }

    public void setWishListList(List<WishList> wishListList) {
        this.wishListList = wishListList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isCustomerLoggedIn() {
        return customerLoggedIn;
    }

    public void setCustomerLoggedIn(boolean customerLoggedIn) {
        this.customerLoggedIn = customerLoggedIn;
    }

    public int getShoppingCartSize() {
        return shoppingCartSize;
    }

    public void setShoppingCartSize(int shoppingCartSize) {
        this.shoppingCartSize = shoppingCartSize;
    }

    public int getWishListSize() {
        return wishListSize;
    }

    public void setWishListSize(int wishListSize) {
        this.wishListSize = wishListSize;
    }

    public int getProcessedOrderCount() {
        return processedOrderCount;
    }

    public void setProcessedOrderCount(int processedOrderCount) {
        this.processedOrderCount = processedOrderCount;
    }
}
