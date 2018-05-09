package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private SearchCriteria searchCriteria;
    private Customer customer;
    private List<Customer> customerList;
    private List<WishList> wishListList;
    private List<ShoppingCart> shoppingCartList;
    private List<Product> productList;
    private Product product;
    private ShoppingCart shoppingCart;
    private WishList wishList;
    private CustomerAddress customerAddress;
    private List<CustomerAddress> customerAddressList;
    private String emailSubject;
    private String emailContent;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private WishListDao wishListDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionDataSet sessionDataSet;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;

    public List<Customer> getSearchedCustomer(SearchCriteria searchCriteria) {
        return customerDao.getSearchedCustomer(searchCriteria);
    }

    public Customer getCustomerDataDuringLogin(SearchCriteria searchCriteria) {
        customerList = customerDao.getSearchedCustomer(searchCriteria);
        customer = null;
        if(customerList!=null && !customerList.isEmpty()) {
            customer = customerList.get(0);
            this.searchCriteria = new SearchCriteria();
            this.searchCriteria.setCustomerId(customer.getId());

            // Set Customer Wishlist
            wishListList = wishListDao.getSearchedWishList(this.searchCriteria);
            sessionDataSet.setWishListList(wishListList);
            sessionDataSet.setWishListSize(wishListList.size());

            // Set Customer Cart
            shoppingCartList = shoppingCartDao.getSearchedShoppingCart(this.searchCriteria);
            sessionDataSet.setShoppingCart(shoppingCartList);
            sessionDataSet.setShoppingCartSize(shoppingCartList.size());

            //Set Customer 'PROCESSED' Order Count
            sessionDataSet.setProcessedOrderCount(orderService.getProcessedOrderCount(this.searchCriteria));
        }
        return customer;
    }

    @Transactional(readOnly = false)
    public void addToCart(Long productId) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setId(productId);
        productList = productDao.getSearchedProduct(searchCriteria);
        if(productList!=null && !productList.isEmpty()) {
            product = productList.get(0);
            shoppingCart = new ShoppingCart();
            shoppingCart.setProduct(product);
            shoppingCart.setCartAddedDate(LocalDateTime.now());
            shoppingCart.setCustomer(sessionDataSet.getCustomer());
            shoppingCartDao.save(shoppingCart);

            //Add to SessionDataSet
            sessionDataSet.getShoppingCart().add(shoppingCart);
            sessionDataSet.setShoppingCartSize(sessionDataSet.getShoppingCartSize() + 1);
        }
    }

    @Transactional(readOnly = false)
    public void addToWishList(Long productId) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setId(productId);
        productList = productDao.getSearchedProduct(searchCriteria);
        if(productList!=null && !productList.isEmpty()) {
            product = productList.get(0);
            wishList = new WishList();
            wishList.setProduct(product);
            wishList.setWishlistAddedDate(LocalDateTime.now());
            wishList.setWishlistAddedPrice(product.getUnitPrice());
            wishList.setCustomer(sessionDataSet.getCustomer());
            wishListDao.save(wishList);

            //Add to SessionDataSet
            sessionDataSet.getWishListList().add(wishList);
            sessionDataSet.setWishListSize(sessionDataSet.getWishListSize() + 1);
        }
    }

    @Transactional(readOnly = false)
    public void saveCustomerWithAddress(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getNewPassword()[0]));
        customer.setActive(false);
        customer.setPasswordToken(DemoConstants.UUID());
        customerDao.save(customer);
        emailSubject = "NIIT E-Commerce Account Activation";
        emailContent = "Hello " + customer.getName() + ",\n\n"
                + "Please click on the following link to activate your account."
                + "\n"+ DemoConstants.APPLICATION_URL+"customer/activate/"
                + customer.getPasswordToken()
                + "\n\n" + "Thank you,"
                + "\n" + "NIIT E-Commerce Team";
        emailUtil.sendSimpleMessage(customer.getEmailId(), emailSubject, emailContent);

        customerAddressList = new ArrayList<>();
        customer.getBillingAddress().setCustomerId(customer.getId());
        customerAddressList.add(customer.getBillingAddress());
        customer.getShippingAddress().setCustomerId(customer.getId());
        customerAddressList.add(customer.getShippingAddress());
        customerAddressDao.saveAll(customerAddressList);
    }

    @Transactional(readOnly = false)
    public int activateCustomer(String activationToken) {
        return customerDao.activateCustomer(activationToken);
    }

    public Customer getCustomerProfile(Customer customer) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setCustomerId(customer.getId());
        customerAddressList = customerAddressDao.getSearchedCustomerAddress(searchCriteria);
        if(customerAddressList!=null && !customerAddressList.isEmpty()) {
            customerAddressList.forEach(cusAdd -> {
                if(cusAdd.getAddressType().equals("BILLING")) {
                    customer.setBillingAddress(cusAdd);
                } else if(cusAdd.getAddressType().equals("SHIPPING")){
                    customer.setShippingAddress(cusAdd);
                }
            });
        }
        return customer;
    }

    @Transactional(readOnly = false)
    public void updateCustomerWithAddress(Customer customer) {
        if(!(customer.getNewPassword()[0].isEmpty() && customer.getNewPassword()[1].isEmpty())
                && !customer.getNewPassword()[0].equals(customer.getNewPassword()[1])) {
                customer.setPassword(passwordEncoder.encode(customer.getNewPassword()[0]));
        }
        customerDao.updateCustomerProfile(customer);

        customerAddressList = new ArrayList<>();
        customer.getBillingAddress().setCustomerId(customer.getId());
        customerAddressList.add(customer.getBillingAddress());
        customer.getShippingAddress().setCustomerId(customer.getId());
        customerAddressList.add(customer.getShippingAddress());
        customerAddressDao.saveAll(customerAddressList);
    }

    @Transactional(readOnly = false)
    public int updateCustomerPassword(Customer customer) {
        return customerDao.updateCustomerPassword(customer);
    }

    @Transactional(readOnly = false)
    public void forgotCustomerPassword(Customer customer) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setEmail(customer.getEmailId());
        customerList = customerDao.getSearchedCustomer(searchCriteria);

        if(customerList != null && !customerList.isEmpty()) {
            this.customer = customerList.get(0);
            this.customer.setPasswordToken(DemoConstants.UUID());
            emailSubject = "NIIT E-Commerce Password Reset";
            emailContent = "Hello " + this.customer.getName() + ",\n\n"
                    + "Please click on the following link to reset your password."
                    + "\n"+ DemoConstants.APPLICATION_URL+"customer/resetpassword/"
                    + this.customer.getPasswordToken()
                    + "\n\n" + "Thank you,"
                    + "\n" + "NIIT E-Commerce Team";
            emailUtil.sendSimpleMessage(customer.getEmailId(), emailSubject, emailContent);
        } else {
            DemoLogger.error("No Customer available for E-Mail ID : " + searchCriteria.getEmail());
        }
    }

    @Transactional(readOnly = false)
    public void saveCustomer(Customer customer) {
        customerDao.save(customer);
    }

    @Transactional(readOnly = false)
    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    @Transactional(readOnly = false)
    public void deleteCustomer(Long id) {
        customerDao.delete(id);
    }

}
