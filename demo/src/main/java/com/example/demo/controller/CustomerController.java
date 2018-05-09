package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private SearchCriteria searchCriteria;
    private Customer customer;
    private List<Customer> customerList;
    private ProductCategory productCategory;
    private List<Product> productList;
    private ShoppingCart shoppingCart;
    private Optional<ShoppingCart> optionalShoppingCart;
    private Optional<WishList> optionalWishList;
    private CustomerAddress customerAddress;
    private List<Municipality> municipalityList;
    private List<ShoppingCart> shoppingCartList;
    private BigDecimal shoppingCartTotal;
    private CustomerOrder customerOrder;
    private List<CustomerOrder> customerOrderList;
    private CustomerOrderDetail customerOrderDetail;
    private List<CustomerOrderDetail> customerOrderDetailList;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SessionDataSet sessionDataSet;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private PoliticalDivisionService politicalDivisionService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/";
        }
        model.addAttribute("customer", new Customer());
        return "/customer/logincustomer";
    }

    @PostMapping(value = "/login")
    public String customerLogin(Customer cust, RedirectAttributes redirectAttributes) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setEmail(cust.getEmailId());
        try {
            customer = customerService.getCustomerDataDuringLogin(searchCriteria);
            if(customer != null) {
                if(!passwordEncoder.matches(cust.getPassword(),customer.getPassword())) {
                    DemoLogger.error("Incorrect Username and/or Password !");
                    redirectAttributes.addFlashAttribute("message", "Incorrect Username and/or Password !");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                    return "redirect:/customer/login";
                } else if(!this.customer.getActive()){
                    DemoLogger.error("Customer is inactive ! Please contact administrator !");
                    redirectAttributes.addFlashAttribute("message", "Customer is inactive ! Please contact administrator !");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                    return "redirect:/customer/login";
                } else {
                    sessionDataSet.setCustomer(this.customer);
                    sessionDataSet.setCustomerLoggedIn(true);
                }
            } else {
                DemoLogger.error("User not available !");
                redirectAttributes.addFlashAttribute("message", "User not available !");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                return "redirect:/customer/login";
            }
        } catch(Exception ex) {
            DemoLogger.error("Exception in Customer Retrieval : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Retrieval.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/logout")
    public String logoutPage () {
        sessionDataSet.setCustomer(null);
        sessionDataSet.setCustomerLoggedIn(false);
        sessionDataSet.setShoppingCart(new ArrayList<>());
        sessionDataSet.setShoppingCartSize(0);
        sessionDataSet.setWishListList(new ArrayList<>());
        sessionDataSet.setWishListSize(0);
        sessionDataSet.setProcessedOrderCount(0);
        return "redirect:/";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }
        customer = new Customer();
        customerAddress = new CustomerAddress();
        customer.setBillingAddress(customerAddress);
        customerAddress = new CustomerAddress();
        customer.setShippingAddress(customerAddress);
        model.addAttribute("customer", customer);
        try {
            model.addAttribute("states", politicalDivisionService.getSearchedState(null));
        } catch (Exception ex) {
            DemoLogger.error("Exception while loading Customer Registration Page : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "/customer/registercustomer";
    }

    @PostMapping(value = "/register")
    public String registerCustomer(Customer customer, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }
        try{
            if(!customer.getNewPassword()[0].equals(customer.getNewPassword()[1])) {
                redirectAttributes.addFlashAttribute("customer", customer);
                redirectAttributes.addFlashAttribute("message", "Password and Confirm Password do not match.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                return "redirect:/customer/register";
            } else {
                customerService.saveCustomerWithAddress(customer);
                redirectAttributes.addFlashAttribute("message", "Customer Registered Successfully. "
                        + " Please check your Email for Activation Steps.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }

        } catch (Exception ex) {
            DemoLogger.error("Exception in Customer Registration : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Registration.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/activate/{activationToken}")
    public String activateCustomer(@PathVariable("activationToken") String activationToken,
                                    Model model, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }
        try {
            customerList = customerService.getSearchedCustomer(searchCriteria);
            if(customerService.activateCustomer(activationToken)==1) {
                redirectAttributes.addFlashAttribute("message", "Customer Account Successfully Activated.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Invalid Activation Token.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex) {
            DemoLogger.warn("Exception in Customer Password Reset : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Activation.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/municipality/{stateId}")
    @ResponseBody
    public List<Municipality> municipalityByState(@PathVariable("stateId") Long stateId) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setStateId(stateId);
        return politicalDivisionService.getSearchedMunicipality(searchCriteria);
    }

    @GetMapping(value = "/ward/{municipalityId}")
    @ResponseBody
    public Integer wardCountByMunicipality(@PathVariable("municipalityId") Long municipalityId) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setId(municipalityId);
        try {
            municipalityList = politicalDivisionService.getSearchedMunicipality(searchCriteria);
            if(municipalityList!=null && !municipalityList.isEmpty()) {
                return municipalityList.get(0).getWardCount();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @GetMapping(value = "/updateprofile")
    public String updateProfilePage(@ModelAttribute("customer") Customer customer,
                                    Model model, RedirectAttributes redirectAttributes) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        try {
            if(customer.getId()!=null) {
                this.customer = customer;
            } else {
                this.customer = new Customer(sessionDataSet.getCustomer());
            }
            this.customer = customerService.getCustomerProfile(this.customer);
            model.addAttribute("customer", this.customer);
            model.addAttribute("states", politicalDivisionService.getSearchedState(null));
        } catch (Exception ex) {
            DemoLogger.error("Exception while loading Update Customer Profile Page : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception while loading Update Customer Profile Page.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/customer/profileupdate";
    }

    @PostMapping(value = "/updateprofile")
    public String updateCustomerProfile(Customer customer, RedirectAttributes redirectAttributes) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        try{
            if(!customer.getNewPassword()[0].equals(customer.getNewPassword()[1])) {
                redirectAttributes.addFlashAttribute("customer", customer);
                redirectAttributes.addFlashAttribute("message", "Password and Confirm Password do not match.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                return "redirect:/customer/updateprofile";
            } else {
                customerService.updateCustomerWithAddress(customer);
                redirectAttributes.addFlashAttribute("message", "Customer Profile Updated Successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in Customer Registration : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Profile Update.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/changepassword")
    public String changePasswordPage(Model model) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        customer = new Customer();
        customer.setId(sessionDataSet.getCustomer().getId());
        customer.setName(sessionDataSet.getCustomer().getName());
        model.addAttribute("customer", customer);
        return "/customer/passwordchange";
    }

    @PostMapping(value = "/changepassword")
    public String changeCustomerPassword(Customer customer, RedirectAttributes redirectAttributes) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        if(passwordEncoder.matches(customer.getPassword(), sessionDataSet.getCustomer().getPassword())) {
            if(customer.getNewPassword()[0].equals(customer.getNewPassword()[1])) {
                customer.setPassword(passwordEncoder.encode(customer.getNewPassword()[0]));
                try {
                    customerService.updateCustomerPassword(customer);
                    sessionDataSet.getCustomer().setPassword(customer.getPassword());
                    redirectAttributes.addFlashAttribute("message", "Password Successfully Updated.");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                } catch (Exception ex) {
                    DemoLogger.warn("Exception in Customer Password Update : " + ex.getMessage());
                    redirectAttributes.addFlashAttribute("message", "Exception in Customer Password Update.");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                    ex.printStackTrace();
                    return "redirect:/customer/changepassword";
                }
            } else {
                DemoLogger.warn("New Passwords to not Match.");
                redirectAttributes.addFlashAttribute("message", "New and Confirm Passwords do not Match.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
                return "redirect:/customer/changepassword";
            }
        } else {
            DemoLogger.error("Current Password entered is Incorrect.");
            redirectAttributes.addFlashAttribute("message", "Current Password entered is Incorrect.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/customer/changepassword";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/forgotpassword")
    public String forgotPasswordPage(Model model, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }
        customer = new Customer();
        model.addAttribute("customer", customer);
        return "/customer/passwordforgot";
    }

    @PostMapping(value = "/forgotpassword")
    public String forgotCustomerPassword(Customer customer, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/";
        }
        try {
            customerService.forgotCustomerPassword(customer);
            redirectAttributes.addFlashAttribute("message", "Password reset link sent to : "
                    + customer.getEmailId());
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            DemoLogger.error("Exception in Customer Password Update : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Password Reset.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/resetpassword/{passwordToken}")
    public String resetPasswordPage(@PathVariable("passwordToken") String passwordToken,
                                     Model model, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }
        searchCriteria = new SearchCriteria();
        searchCriteria.setPasswordToken(passwordToken);
        try {
            customerList = customerService.getSearchedCustomer(searchCriteria);
            if(customerList != null && !customerList.isEmpty()) {
                customer = customerList.get(0);
            } else {
                redirectAttributes.addFlashAttribute("message", "Invalid Password Token.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                return "redirect:/";
            }
        } catch(Exception ex) {
            DemoLogger.warn("Exception in Customer Password Reset : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Customer Password Reset.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
            return "redirect:/";
        }
        model.addAttribute("customer", customer);
        return "/customer/passwordreset";
    }

    @PostMapping(value = "/resetpassword")
    public String resetCustomerPassword(Customer customer, RedirectAttributes redirectAttributes) {
        if(sessionDataSet.isCustomerLoggedIn()) {
            redirectAttributes.addFlashAttribute("message", "Customer Already Logged-In into the System.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/";
        }

        if(customer.getNewPassword()[0].equals(customer.getNewPassword()[1])) {
            customer.setPassword(passwordEncoder.encode(customer.getNewPassword()[0]));
            try {
                customerService.updateCustomerPassword(customer);
                redirectAttributes.addFlashAttribute("message", "Password Successfully Reset.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } catch (Exception ex) {
                DemoLogger.warn("Exception in Customer Password Update : " + ex.getMessage());
                redirectAttributes.addFlashAttribute("message", "Exception in Customer Password Reset.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                ex.printStackTrace();
                return "redirect:/";
            }
        } else {
            DemoLogger.warn("New Passwords to not Match.");
            redirectAttributes.addFlashAttribute("message", "New and Confirm Passwords do not Match.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/customer/resetpassword/" + customer.getPasswordToken();
        }

        return "redirect:/";
    }

    @GetMapping(value = "/wish")
    public String customerWishlist() {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        return "/customer/wishlist";
    }

    @GetMapping(value = "/cart")
    public String customerShoppingCartPage(Model model) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        this.customer = sessionDataSet.getCustomer();
        this.customer.setTempShoppingCartList(sessionDataSet.getShoppingCart());
        model.addAttribute("customer", this.customer);
        return "/customer/shoppingcart";
    }

    @PostMapping(value = "/cart")
    public String customerShopingCart(Customer tempCust) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        shoppingCartList = tempCust.getTempShoppingCartList();
        sessionDataSet.getShoppingCart().forEach(sessionShoppingCart -> {
            shoppingCartList.parallelStream().forEach(shoppingCart -> {
                if(sessionShoppingCart.getId().equals(shoppingCart.getId())) {
                    sessionShoppingCart.setQuantity(shoppingCart.getQuantity());
                }
            });
        });
        shoppingCartService.updateShoppingCartQuantity(shoppingCartList);
        return "redirect:/customer/check-out";
    }

    @GetMapping(value = "/product/addtocart/{productId}")
    public String customerAddToCart(@PathVariable("productId") Long productId,
                                    Model model, RedirectAttributes redirectAttributes) {
        try {
            if(!sessionDataSet.isCustomerLoggedIn()) {
                return "redirect:/customer/login";
            }
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(productId);
            customerService.addToCart(productId);
        } catch (Exception ex) {
            DemoLogger.error("Exception while adding to Cart : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception while adding to Cart.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/product/addtowishlist/{productId}")
    public String customerAddToWishList(@PathVariable("productId") Long productId,
                                    Model model, RedirectAttributes redirectAttributes) {
        try {
            if(!sessionDataSet.isCustomerLoggedIn()) {
                return "redirect:/customer/login";
            }
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(productId);
            customerService.addToWishList(productId);
        } catch (Exception ex) {
            DemoLogger.error("Exception while adding to Wish-List : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception while adding to Wish-List.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/product/addtocartfromwishlist/{productId}")
    public String customerAddToCartFromWishList(@PathVariable("productId") Long productId,
                                    Model model, RedirectAttributes redirectAttributes) {
        try {
            if(!sessionDataSet.isCustomerLoggedIn()) {
                return "redirect:/customer/login";
            }
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(productId);
            customerService.addToCart(productId);
        } catch (Exception ex) {
            DemoLogger.error("Exception while adding to Cart from Wish-List : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception while adding to Cart from Wish-List.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/customer/wish";
    }

    @GetMapping(value = "/product/removefromcart/{productId}")
    public String customerRemoveFromCart(@PathVariable("productId") Long productId,
                                    Model model, RedirectAttributes redirectAttributes) {
        try {
            if(!sessionDataSet.getShoppingCart().isEmpty()) {
//                sessionDataSet.getShoppingCart().removeIf(cart -> cart.getProduct().getId().equals(productId));
                optionalShoppingCart = sessionDataSet.getShoppingCart()
                        .parallelStream()
                        .filter(cart -> cart.getProduct().getId().equals(productId))
                        .findFirst();
                sessionDataSet.getShoppingCart().remove(optionalShoppingCart.get());
                sessionDataSet.setShoppingCartSize(sessionDataSet.getShoppingCartSize() - 1);
                shoppingCartService.deleteShoppingCart(optionalShoppingCart.get().getId());
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception while Removing from Cart : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception while adding to Cart from Wish-List.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/customer/cart";
    }

    @GetMapping(value = "/product/removefromwishlist/{productId}")
    public String customerRemoveFromWishList(@PathVariable("productId") Long productId,
                                         Model model) {
        try {
            if(!sessionDataSet.getWishListList().isEmpty()) {
//                sessionDataSet.getWishListList().removeIf(wish -> wish.getProduct().getId().equals(productId));
                optionalWishList = sessionDataSet.getWishListList()
                        .parallelStream()
                        .filter(wish -> wish.getProduct().getId().equals(productId))
                        .findFirst();
                sessionDataSet.getWishListList().remove(optionalWishList.get());
                sessionDataSet.setWishListSize(sessionDataSet.getWishListSize() - 1);
                wishListService.deleteWishList(optionalWishList.get().getId());
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception while Removing from Wish List : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "redirect:/customer/wish";
    }

    @GetMapping(value = "/productcategory/{productCategoryId}")
    public String customerProductCategory(@PathVariable("productCategoryId") Long proCatId, Model model) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setProductCategoryId(proCatId);
        searchCriteria.setAvailable(true);
        try {
            productList = productService.getSearchedProduct(searchCriteria);
        } catch (Exception ex) {
            DemoLogger.error("Exception in Product Retrieval : " + ex.getMessage());
            ex.printStackTrace();
        }
        model.addAttribute("productList", productList);
        productCategory = productList.get(0).getProductCategory();
        model.addAttribute("productCategory", productCategory);
        return "/customer/productcategorydetail";
    }

    @GetMapping(value = "/productdetail/{productId}")
    public String customerProduct(@PathVariable("productId") Long proId, Model model) {
        searchCriteria = new SearchCriteria();
        searchCriteria.setId(proId);
        try {
            productList = productService.getSearchedProduct(searchCriteria);
        } catch (Exception ex) {
            DemoLogger.error("Exception in Product Retrieval : " + ex.getMessage());
            ex.printStackTrace();
        }
        model.addAttribute("product", productList.get(0));
        return "/customer/productdetail";
    }

    @GetMapping(value = "/check-out")
    public String checkoutPage() {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        return "/customer/checkout";
    }

    @GetMapping(value = "/order/payment")
    public String makePayment(RedirectAttributes redirectAttributes) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }

        this.shoppingCartList = sessionDataSet.getShoppingCart();
        this.shoppingCartTotal = BigDecimal.ZERO;
        this.customerOrder = new CustomerOrder();
        this.customerOrderDetailList = new ArrayList<>();

        shoppingCartList.forEach(shoppingCart -> {
            customerOrder.setCustomer(shoppingCart.getCustomer());

            customerOrderDetail = new CustomerOrderDetail();
            customerOrderDetail.setCustomerOrder(customerOrder);
            customerOrderDetail.setOrderQty(shoppingCart.getQuantity());
            customerOrderDetail.setProduct(shoppingCart.getProduct());
            customerOrderDetail.setPurchasePrice(shoppingCart.getProduct().getUnitPrice());
            customerOrderDetailList.add(customerOrderDetail);

            shoppingCartTotal = shoppingCartTotal.add(customerOrderDetail.getPurchasePrice().multiply(new BigDecimal(customerOrderDetail.getOrderQty())));
        });
        customerOrder.setCustomerOrderDetails(new HashSet<>(customerOrderDetailList));
        customerOrder.setOrderDate(LocalDateTime.now());
        customerOrder.setOrderPrice(shoppingCartTotal);
        customerOrder.setDeliveryDate(customerOrder.getOrderDate().plusDays(DemoConstants.DELIVERY_DAYS));
        customerOrder.setOrderStatus("PROCESSED");

        orderService.saveOrder(customerOrder);

        redirectAttributes.addFlashAttribute("message", "Order Successfully Processed."
                + " Order ID : " + customerOrder.getId());
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/";
    }

    @GetMapping(value = {"/orders"})
    public String retrieveProcessedOrder(Model model) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }
        searchCriteria = new SearchCriteria();
        searchCriteria.setCustomerId(sessionDataSet.getCustomer().getId());
        searchCriteria.setStatus("PROCESSED");
        model.addAttribute("processedorders", orderService.getOrdersForCustomer(searchCriteria));
        searchCriteria.setStatus("COMPLETED");
        model.addAttribute("completedorders", orderService.getOrdersForCustomer(searchCriteria));
        return "/customer/orderlist";
    }

    @GetMapping(value = {"/order-detail/{orderId}"})
    public String retrieveProcessedOrder(@PathVariable("orderId") Long orderId,
                                         Model model, RedirectAttributes redirectAttributes) {
        if(!sessionDataSet.isCustomerLoggedIn()) {
            return "redirect:/customer/login";
        }

        searchCriteria = new SearchCriteria();
        searchCriteria.setId(orderId);
        this.customerOrderList = orderService.getSearchedOrder(searchCriteria);
        if(customerOrderList !=null && !customerOrderList.isEmpty()) {
            this.customerOrder = customerOrderList.get(0);
            this.customerOrder.setCustomerOrderDetailList(new ArrayList<>(this.customerOrder.getCustomerOrderDetails()));
            model.addAttribute("order", this.customerOrder);
            return "/customer/orderdetail";
        } else {
            redirectAttributes.addFlashAttribute("message", "Order Details could not be retrieved.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/customer/orders";
        }
    }

    @GetMapping(value = {"/listcustomer"})
    public String retrieveAllCustomer(Model model) {
        model.addAttribute("customers",
                customerService.getSearchedCustomer(null));
        return "/customer/customerlist";
    }

    @GetMapping(value = {"/customer"})
    public String addCustomerPage(Model model) {
        try {
            this.customer = new Customer();
        } catch (Exception ex) {
            DemoLogger.error("Exception in Customer Retrieval : " + ex.getMessage());
            ex.printStackTrace();
        }
        model.addAttribute("customer", this.customer);
        return "/customer/addupdatecustomer";
    }

    @PostMapping(value = {"/customer"})
    public String addCustomer(Customer usr) {
        try {
            usr.setPassword(passwordEncoder.encode(usr.getPassword()));
            if(usr.getId()==null) {
                customerService.saveCustomer(usr);
                DemoLogger.info("Customer Successfully Added.");
            } else {
                customerService.updateCustomer(usr);
                DemoLogger.info("Customer Successfully Updated.");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in Customer Add : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "redirect:/customer/listcustomer";
    }

    @GetMapping(value = {"/customer/edit/{id}"})
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(id);
            this.customerList = customerService.getSearchedCustomer(searchCriteria);
            if(customerList!=null && !customerList.isEmpty()) {
                this.customer = this.customerList.get(0);
                this.customer.setPassword("");
                model.addAttribute("customer", this.customer);
            } else {
                DemoLogger.error("Customer could not be retrieved !");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in Customer Edit : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "/customer/addupdatecustomer";
    }

    @GetMapping(value = {"/customer/delete/{id}"})
    public String deleteCustomer(@PathVariable("id") Long id) {
        try{
            customerService.deleteCustomer(id);
            DemoLogger.info("Customer Successfully Deleted.");
        } catch(Exception ex) {
            DemoLogger.error("Exception in Customer Delete : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "redirect:/customer/listcustomer";
    }
}
