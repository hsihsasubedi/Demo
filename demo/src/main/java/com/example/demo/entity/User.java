package com.example.demo.entity;

import com.example.demo.utils.DemoConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user",
        uniqueConstraints = { @UniqueConstraint(columnNames = "email_id") })
public class User implements Serializable {

    private static final long serialVersionUID = 201801210011541L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email_id")
    private String emailId;

    @Column(name="password")
    private String password;

    @Column(name="user_type")
    private String userType;

    @Column(name="name")
    private String name;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "password_token")
    private String passwordToken;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns =
        @JoinColumn(name = "user_id"), inverseJoinColumns =
        @JoinColumn(name = "role_id"))
    private Set<Role> userRoles = new HashSet<>();

    @Transient
    private List<String> userTypes = Arrays.asList(DemoConstants.USER_TYPE);

    @Transient
    private List<Role> roleList = new ArrayList<>();

    @Transient
    private List<Long> selectedRoles;

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }

    public List<Role> getRoleList() {
        if(roleList==null) {
            roleList = new ArrayList<>();
        }
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Long> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Long> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
