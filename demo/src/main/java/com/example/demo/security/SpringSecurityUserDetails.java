package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.DemoLogger;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class SpringSecurityUserDetails implements UserDetailsService {

    private StringBuilder userRoleNames;
    private SearchCriteria searchCriteria;
    private List<User> userList;
    private User user;
    private List<GrantedAuthority> grantedAuthorityList;
    private GrantedAuthority grantedAuthority;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        searchCriteria = new SearchCriteria();
        searchCriteria.setEmail(username);
        userRoleNames = new StringBuilder();

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        grantedAuthorityList = new ArrayList<>();
        try {
            userList = userService.getSearchedUser(searchCriteria);

            if (userList!= null && !userList.isEmpty()) {

                user = userList.get(0);
                user.getUserRoles().forEach(role -> {
                    grantedAuthority = new SimpleGrantedAuthority(role.getName());
                    grantedAuthorityList.add(grantedAuthority);
                });
                builder = org.springframework.security.core.userdetails.User.withUsername(username);
                builder.disabled(!user.getActive());
                builder.password(user.getPassword());
                builder.authorities(grantedAuthorityList);
            } else {
                throw new UsernameNotFoundException("User not found.");
            }
        } catch (Exception ex) {
            DemoLogger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return builder.build();
    }
}
