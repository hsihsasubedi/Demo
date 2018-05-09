package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_address")
public class CustomerAddress implements Serializable {

    private static final long serialVersionUID = 201801200012242L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "address_type")
    private String addressType;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state = new State();

    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality = new Municipality();

    @Column(name = "ward_no")
    private Byte wardNo;

    @Column(name = "street_name")
    private String streetName;

    public CustomerAddress() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Byte getWardNo() {
        return wardNo;
    }

    public void setWardNo(Byte wardNo) {
        this.wardNo = wardNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
