package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 201802130011354L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "password_token")
    private String passwordToken;

    @Transient
    private CustomerAddress billingAddress = new CustomerAddress();

    @Transient
    private CustomerAddress shippingAddress= new CustomerAddress();

    @Transient
    private String[] newPassword = new String[2];

    @Transient
    private List<ShoppingCart> tempShoppingCartList = new ArrayList<>();

    public Customer() {
        super();
    }

    public Customer(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.contactNo = customer.getContactNo();
        this.emailId = customer.getEmailId();
        this.password = customer.getPassword();
        this.active = customer.getActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(String passwordToken) {
        this.passwordToken = passwordToken;
    }

    public CustomerAddress getBillingAddress() {
        billingAddress.setAddressType("BILLING");
        return billingAddress;
    }

    public void setBillingAddress(CustomerAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public CustomerAddress getShippingAddress() {
        shippingAddress.setAddressType("SHIPPING");
        return shippingAddress;
    }

    public void setShippingAddress(CustomerAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String[] getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String[] newPassword) {
        this.newPassword = newPassword;
    }

    public List<ShoppingCart> getTempShoppingCartList() {
        return tempShoppingCartList;
    }

    public void setTempShoppingCartList(List<ShoppingCart> tempShoppingCartList) {
        this.tempShoppingCartList = tempShoppingCartList;
    }
}
