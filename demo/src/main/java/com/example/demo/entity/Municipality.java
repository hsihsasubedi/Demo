package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "municipality")
public class Municipality implements Serializable {

    private static final long serialVersionUID = 201801060012304L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "municipality_type_id")
    private MunicipalityType municipalityType;

    @Column(name = "ward_count")
    private Integer wardCount;

    public Municipality() {
        super();
    }

    public Municipality(Long id, String name, Integer wardCount) {
        this.id = id;
        this.name = name;
        this.wardCount = wardCount;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public MunicipalityType getMunicipalityType() {
        return municipalityType;
    }

    public void setMunicipalityType(MunicipalityType municipalityType) {
        this.municipalityType = municipalityType;
    }

    public Integer getWardCount() {
        return wardCount;
    }

    public void setWardCount(Integer wardCount) {
        this.wardCount = wardCount;
    }
}
