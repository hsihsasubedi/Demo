package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.utils.DemoLogger;
import com.example.demo.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/identity")
public class IdentityController {

    private SearchCriteria searchCriteria;
    private Role role;
    private List<Role> roleList;
    private User user;
    private List<User> userList;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model) {
        LocalDateTime currentTime = LocalDateTime.now();
        model.addAttribute("sysTime", currentTime);
        return "/admin/admindashboard";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        model.addAttribute("loginUser", new User());
        return "/identity/adminlogin";
    }

//    @GetMapping(value = "/logout")
//    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/login?logout";
//    }

    @GetMapping(value = {"/listrole"})
    @PreAuthorize("hasAuthority('Role_List')")
    public String retrieveAllRole(Model model) {
        model.addAttribute("roles",
                roleService.getSearchedRole(null));
        return "/identity/rolelist";
    }

    @GetMapping(value = {"/role"})
    @PreAuthorize("hasAuthority('Role_Add')")
    public String addRolePage(Model model) {
        this.role = new Role();
        model.addAttribute("role", this.role);
        return "/identity/addupdaterole";
    }

    @PostMapping(value = {"/role"})
    @PreAuthorize("hasAuthority('Role_Add')")
    public String addRole(Role proCat, RedirectAttributes redirectAttributes) {
        try {
            if(proCat.getId()==null) {
                roleService.saveRole(proCat);
                DemoLogger.info("Role Successfully Added.");
                redirectAttributes.addFlashAttribute("message", "Role Successfully Added.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                roleService.updateRole(proCat);
                DemoLogger.info("Role Successfully Updated.");
                redirectAttributes.addFlashAttribute("message", "Role Successfully Updated.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in Role Add / Update : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Role Add / Update.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/identity/listrole";
    }

    @GetMapping(value = {"/role/edit/{id}"})
    @PreAuthorize("hasAuthority('Role_Update')")
    public String editRole(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(id);
            roleList = roleService.getSearchedRole(searchCriteria);
            if(roleList!=null && !roleList.isEmpty()) {
                this.role = roleList.get(0);
                model.addAttribute("role", this.role);
            } else {
                DemoLogger.error("Role could not be retrieved !");
                redirectAttributes.addFlashAttribute("message", "Role could not be retrieved.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in Role Edit : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Role Edit.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/identity/addupdaterole";
    }

    @GetMapping(value = {"/role/delete/{id}"})
    @PreAuthorize("hasAuthority('Role_Delete')")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            roleService.deleteRole(id);
            DemoLogger.info("Role Successfully Deleted.");
            redirectAttributes.addFlashAttribute("message", "Role Successfully Deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch(Exception ex) {
            DemoLogger.error("Exception in Role Delete : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Role Delete.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/identity/listrole";
    }

    @GetMapping(value = {"/listuser"})
    @PreAuthorize("hasAuthority('User_List')")
    public String retrieveAllUser(Model model) {
        model.addAttribute("users",
                userService.getSearchedUser(null));
        return "/identity/userlist";
    }

    @GetMapping(value = {"/user"})
    @PreAuthorize("hasAuthority('User_Add')")
    public String addUserPage(Model model, RedirectAttributes redirectAttributes) {
        try {
            this.user = new User();
            this.user.setRoleList(roleService.getSearchedRole(null));
        } catch (Exception ex) {
            DemoLogger.error("Exception in Role Retrieval : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Role Retrieval.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        model.addAttribute("user", this.user);
        return "/identity/addupdateuser";
    }

    @PostMapping(value = {"/user"})
    @PreAuthorize("hasAuthority('User_Add')")
    public String addUser(User usr, RedirectAttributes redirectAttributes) {
        this.roleList = new ArrayList<>();
        usr.getRoleList().forEach(role -> {
            if(role.getSelected()) {
                this.roleList.add(role);
            }
        });
        usr.setUserRoles(new HashSet<>(this.roleList));
        try {
            usr.setPassword(passwordEncoder.encode(usr.getPassword()));
            if(usr.getId()==null) {
                userService.saveUser(usr);
                DemoLogger.info("User Successfully Added.");
                redirectAttributes.addFlashAttribute("message", "User Successfully Added.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                userService.updateUser(usr);
                DemoLogger.info("User Successfully Updated.");
                redirectAttributes.addFlashAttribute("message", "User Successfully Updated.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in User Add / Update : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in User Add / Update.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/identity/listuser";
    }

    @GetMapping(value = {"/user/edit/{id}"})
    @PreAuthorize("hasAuthority('User_Update')")
    @Transactional(readOnly = true)
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(id);
            this.userList = userService.getSearchedUser(searchCriteria);
            this.roleList = roleService.getSearchedRole(null);
            if(userList!=null && !userList.isEmpty()) {
                this.user = this.userList.get(0);
                this.user.setPassword("");
                this.user.setRoleList(this.roleList);

                this.user.getUserRoles().forEach(userRole -> {
                    user.getRoleList().forEach(role -> {
                        if(userRole.getId().equals(role.getId()))
                            role.setSelected(true);
                    });
                });

                model.addAttribute("user", this.user);
            } else {
                DemoLogger.error("User could not be retrieved !");
                redirectAttributes.addFlashAttribute("message", "User could not be retrieved.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in User Edit : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in User Edit.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/identity/addupdateuser";
    }

    @GetMapping(value = {"/user/delete/{id}"})
    @PreAuthorize("hasAuthority('User_Delete')")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            userService.deleteUser(id);
            DemoLogger.info("User Successfully Deleted.");
            redirectAttributes.addFlashAttribute("message", "User Successfully Deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch(Exception ex) {
            DemoLogger.error("Exception in User Delete : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in User Delete.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/identity/listuser";
    }

}
