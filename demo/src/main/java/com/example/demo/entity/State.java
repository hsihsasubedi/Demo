package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "state")
public class State implements Serializable {

    private static final long serialVersionUID = 201801200012245L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public State() {
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
