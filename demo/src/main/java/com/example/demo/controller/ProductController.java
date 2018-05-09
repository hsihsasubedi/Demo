package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductCategory;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductCategory productCategory;
    private SearchCriteria searchCriteria;
    private List<ProductCategory> productCategoryList;
    private Product product;
    private List<Product> productList;
    private ClassLoader classLoader = getClass().getClassLoader();

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @GetMapping(value = {"/listproductcategory"})
    @PreAuthorize("hasAuthority('Product_Category_List')")
    public String retrieveAllProductCategory(Model model) {
        model.addAttribute("productcategories",
                productCategoryService.getSearchedProductCategory(null));
        return "/product/productcategorylist";
    }

    @GetMapping(value = {"/productcategory"})
    @PreAuthorize("hasAuthority('Product_Category_Add')")
    public String addProductCategoryPage(Model model) {
        this.productCategory = new ProductCategory();
        model.addAttribute("productCategory", this.productCategory);
        return "/product/addupdateproductcategory";
    }

    @PostMapping(value = {"/productcategory"})
    @PreAuthorize("hasAuthority('Product_Category_Add')")
    public String addProductCategory(ProductCategory proCat, RedirectAttributes redirectAttributes) {
        try {
            if(proCat.getId()==null) {
                productCategoryService.saveProductCategory(proCat);
                DemoLogger.info("Product Category Successfully Added.");
                redirectAttributes.addFlashAttribute("message", "Product Category Successfully Added.");
        		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                productCategoryService.updateProductCategory(proCat);
                DemoLogger.info("Product Category Successfully Updated.");
                redirectAttributes.addFlashAttribute("message", "Product Category Successfully Updated.");
        		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in Product Category Add / Update : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Category Add / Update.");
        	redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }

        return "redirect:/product/listproductcategory";
    }

    @GetMapping(value = {"/productcategory/edit/{id}"})
    @PreAuthorize("hasAuthority('Product_Category_Update')")
    public String editProductCategory(@PathVariable("id") Long id, Model model,
                                      RedirectAttributes redirectAttributes) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(id);
            productCategoryList = productCategoryService.getSearchedProductCategory(searchCriteria);
            if(productCategoryList!=null && !productCategoryList.isEmpty()) {
                this.productCategory = productCategoryList.get(0);
                model.addAttribute("productCategory", this.productCategory);
            } else {
                DemoLogger.error("Product Category could not be retrieved !");
                redirectAttributes.addFlashAttribute("message", "Product Category could not be retrieved.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in Product Category Edit : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Category Edit.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/product/addupdateproductcategory";
    }

    @GetMapping(value = {"/productcategory/delete/{id}"})
    @PreAuthorize("hasAuthority('Product_Category_Delete')")
    public String deleteProductCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            productCategoryService.deleteProductCategory(id);
            DemoLogger.info("Product Category Successfully Deleted.");
            redirectAttributes.addFlashAttribute("message", "Product Category Successfully Deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch(Exception ex) {
            DemoLogger.error("Exception in Product Category Delete : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Category Delete.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/product/listproductcategory";
    }

    @GetMapping(value = {"/listproduct"})
    @PreAuthorize("hasAuthority('Product_List')")
    public String retrieveAllProduct(Model model) {
        model.addAttribute("products",
                productService.getSearchedProduct(null));
        return "/product/productlist";
    }

    @GetMapping(value = {"/product"})
    @PreAuthorize("hasAuthority('Product_Add')")
    public String addProductPage(Model model) {
        this.product = new Product();
        model.addAttribute("product", this.product);
        model.addAttribute("productcategories",
                productCategoryService.getSearchedProductCategory(null));
        return "/product/addupdateproduct";
    }

    @PostMapping(value = {"/product"})
    @PreAuthorize("hasAuthority('Product_Add')")
    public String addProduct(Product pro, RedirectAttributes redirectAttributes) {
        try {

            if(pro.getMultipartFile()!=null && !pro.getMultipartFile().isEmpty()){
                pro.setImageUrl(FileUtil.saveFile(DemoConstants.PRODUCT_REPO,
                        pro.getMultipartFile().getBytes(),
                        MimeTypeToExtension.MIME_TYPE_TO_EXTENSION.get(pro.getMultipartFile().getContentType())));
            }

            if(pro.getId()==null) {
                productService.saveProduct(pro);
                DemoLogger.info("Product Successfully Added.");
                redirectAttributes.addFlashAttribute("message", "Product Successfully Added.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                productService.updateProduct(pro);
                DemoLogger.info("Product Successfully Updated.");
                redirectAttributes.addFlashAttribute("message", "Product Successfully Updated.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception ex) {
            DemoLogger.error("Exception in Product Add / Update : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Add / Update.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
            return "";
        }
        return "redirect:/product/listproduct";
    }

    @GetMapping(value = {"/product/edit/{id}"})
    @PreAuthorize("hasAuthority('Product_Update')")
    public String editProduct(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(id);
            this.productList = productService.getSearchedProduct(searchCriteria);
            if(productList!=null && !productList.isEmpty()) {
                this.product = this.productList.get(0);
                model.addAttribute("product", this.product);
                model.addAttribute("productcategories",
                        productCategoryService.getSearchedProductCategory(null));
            } else {
                DemoLogger.error("Product could not be retrieved !");
                redirectAttributes.addFlashAttribute("message", "Product could not be retrieved.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in Product Edit : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Edit.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/product/addupdateproduct";
    }

    @GetMapping(value = {"/product/delete/{id}"})
    @PreAuthorize("hasAuthority('Product_Delete')")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            productService.deleteProduct(id);
            DemoLogger.info("Product Successfully Deleted.");
            redirectAttributes.addFlashAttribute("message", "Product Successfully Deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch(Exception ex) {
            DemoLogger.error("Exception in Product Delete : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Delete.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "redirect:/product/listproduct";
    }

    @GetMapping(value = "/productimage/{fileName}")
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @PathVariable("fileName") String fileName) throws IOException, URISyntaxException {
        if (fileName != null) {
            response.setContentType("image/jpeg, image/png");
            Path path = Paths.get(DemoConstants.PRODUCT_REPO+ File.separator+fileName);
            if(!path.toFile().exists()) {
                path = Paths.get(classLoader.getResource("public/img/image-not-available.png").toURI());
            }
            response.getOutputStream()
                    .write(Files.readAllBytes(path));
        }
        response.getOutputStream().close();
    }

    @GetMapping(value = { "/productimage/delete/{productId}" })
    public String deleteProductImage(@PathVariable("productId") Long productId,
                                   Model model, RedirectAttributes redirectAttributes) {
        try{
            searchCriteria = new SearchCriteria();
            searchCriteria.setId(productId);
            this.productList = productService.getSearchedProduct(searchCriteria);
            if(productList!=null && !productList.isEmpty()) {
                this.product = this.productList.get(0);
                this.product.setImageUrl(null);
                model.addAttribute("product", this.product);
                model.addAttribute("productcategories",
                        productCategoryService.getSearchedProductCategory(null));
            } else {
                DemoLogger.error("Product could not be retrieved !");
                redirectAttributes.addFlashAttribute("message", "Product could not be retrieved.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        } catch(Exception ex){
            DemoLogger.error("Exception in Product Edit : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Exception in Product Edit.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            ex.printStackTrace();
        }
        return "/product/addupdateproduct";
    }
}
