package com.example.demo.controller;

import com.example.demo.entity.ProductCategory;
import com.example.demo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WelcomeController {

    private List<ProductCategory> productCategoryList;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping(value = "/")
    public String welcomeMessage(Model model) {
        productCategoryList = productCategoryService.getProductCategoryForDashboard();
        model.addAttribute("proCatList", productCategoryList);
        return "/customer/dashboard";
    }
}
