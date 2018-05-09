package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 201802090010811L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer = new Customer();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product = new Product();

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "cart_added_date")
    private LocalDateTime cartAddedDate;

    public ShoppingCart() {
        super();
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCartAddedDate() {
        return cartAddedDate;
    }

    public void setCartAddedDate(LocalDateTime cartAddedDate) {
        this.cartAddedDate = cartAddedDate;
    }
}
