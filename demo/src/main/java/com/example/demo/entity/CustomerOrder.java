package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer_order")
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 201801200012248L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "order_status")
    private String orderStatus;

    @OneToMany(mappedBy = "customerOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CustomerOrderDetail> customerOrderDetails = new HashSet<>();

    @Transient
    private List<CustomerOrderDetail> customerOrderDetailList = new ArrayList<>();

    public CustomerOrder() {
        super();
    }

    public CustomerOrder(Long id, BigDecimal orderPrice, LocalDateTime orderDate, LocalDateTime deliveryDate, String orderStatus) {
        this.id = id;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<CustomerOrderDetail> getCustomerOrderDetails() {
        return customerOrderDetails;
    }

    public void setCustomerOrderDetails(Set<CustomerOrderDetail> customerOrderDetails) {
        this.customerOrderDetails = customerOrderDetails;
    }

    public List<CustomerOrderDetail> getCustomerOrderDetailList() {
        return customerOrderDetailList;
    }

    public void setCustomerOrderDetailList(List<CustomerOrderDetail> customerOrderDetailList) {
        this.customerOrderDetailList = customerOrderDetailList;
    }
}
