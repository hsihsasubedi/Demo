package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "municipality_type")
public class MunicipalityType implements Serializable {

    private static final long serialVersionUID = 201801200012246L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public MunicipalityType() {
        super();
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
}
