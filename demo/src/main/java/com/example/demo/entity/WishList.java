package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish_list")
public class WishList implements Serializable {

    private static final long serialVersionUID = 201802290010810L;

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

    @Column(name = "wishlist_added_price")
    private BigDecimal wishlistAddedPrice;

    @Column(name = "wishlist_added_date")
    private LocalDateTime wishlistAddedDate;

    public WishList() {
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

    public BigDecimal getWishlistAddedPrice() {
        return wishlistAddedPrice;
    }

    public void setWishlistAddedPrice(BigDecimal wishlistAddedPrice) {
        this.wishlistAddedPrice = wishlistAddedPrice;
    }

    public LocalDateTime getWishlistAddedDate() {
        return wishlistAddedDate;
    }

    public void setWishlistAddedDate(LocalDateTime wishlistAddedDate) {
        this.wishlistAddedDate = wishlistAddedDate;
    }

}
