package com.ecommerce.zedshop.controller;

import com.ecommerce.zedshop.model.Category;
import com.ecommerce.zedshop.model.Product;
import com.ecommerce.zedshop.model.User;
import com.ecommerce.zedshop.model.dto.CategoryDto;
import com.ecommerce.zedshop.model.dto.CustomerDto;
import com.ecommerce.zedshop.model.dto.ProductCountDTO;
import com.ecommerce.zedshop.model.dto.ProductDto;
import com.ecommerce.zedshop.repository.ProductRepository;
import com.ecommerce.zedshop.service.CategoryService;
import com.ecommerce.zedshop.service.ProductService;
import com.ecommerce.zedshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ProductsController {
    @Autowired
    private ProductService productService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    LocalDate currentDate = LocalDate.now();

    @GetMapping("/upload-products")
    public String addProducts(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "products";
    }

    @GetMapping("/seller-products")
    public String sellerViewProducts(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<Product>products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "seller-product-view";
    }


    @PostMapping("/process_product")
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("pname") String name,
                              @RequestParam("price") double price,
                              @RequestParam("desc") String desc,
                              @RequestParam("quantity") int currentQuantity,
                              @RequestParam("categories")Category category,
                              Principal principal)

    {
        String username = principal.getName();
        productService.saveProductToDB(file, name, desc, price,currentQuantity,category, username);
        return "redirect:/listProducts.html";
    }



    @GetMapping("/listProducts.html")
    public String showExampleView(Model model)
    {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "seller-dashboard";
    }

    @GetMapping("/view")
    public String viewProduct( Model model){
        List<Product>products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "dashboard";
    }



    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(@PathVariable("id") Long categoryId ,Model model){
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getProductsInCategory(categoryId);
        model.addAttribute("category",category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "products-in-category";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Update products");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update_product";
    }


    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                RedirectAttributes attributes
    ) {
        try {
            productService.update(productDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/view";
    }



    /*@RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:/view";
    }*/
   /* @PostMapping("/changeName")
    public String changeName(@RequestParam("id")Long id,@RequestParam("newName")String name){
     productService.changeProductName(id, name);
     return "redirect:/listProducts.html";
    }

    @PostMapping("/changeDescription")
    public String changeDescription(@RequestParam("id")Long id,@RequestParam("newDescription")
                                    String description){
        productService.changeDescription(id, description);
        return "redirect:/listProducts.html";
    }
    @PostMapping("/changePrice")
    public String changePrice(@RequestParam("id")Long id,@RequestParam("newPrice")
        double price){
        productService.changePrice(id, price);
        return "redirect:/listProducts.html";
    }
*/
    @GetMapping("/product_detail/{productId}")
    public String showProductDetails(@PathVariable("productId")Long id,
                                     Model model){
        Product product=productService.getProductById(id);
        model.addAttribute("product",product);
        return "product_detail";
    }


    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT , RequestMethod.GET})
    public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes) {
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:/view";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id)
    {

        productService.deleteProductById(id);
        return "redirect:/listProducts.html";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(value = "keyword") String keyword,
                                Model model
    ) {
        List<ProductDto> products = productService.searchProducts( keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);

        return "dashboard";

    }

    @GetMapping("/customer-search/{pageNo}")
    public String customerSearch(@PathVariable("pageNo") int pageNo,
                                @RequestParam(value = "keyword") String keyword,
                                Model model
    ) {
        List<ProductDto> products = productService.searchProducts( keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);

        return "search-results";

    }

    @GetMapping("/frequently-bought")
    public String fastMovingProducts(){
        return"fast-moving-items";
    }
    @GetMapping(value = "/chartdata/daily")
    @ResponseBody
    public List<ProductCountDTO> dailyChartData(){
        return productRepository.fastMovingProductsDaily(currentDate, currentDate.plusDays(1));
    }

    @GetMapping(value = "/chartdata/weekly")
    @ResponseBody
    public List<ProductCountDTO> weekChartData(){
        return productRepository.fastMovingProductsWeekly(currentDate, currentDate.plusDays(7));
    }

    @GetMapping(value = "/chartdata/monthly")
    @ResponseBody
    public List<ProductCountDTO> monthlyChartData() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return productRepository.fastMovingProductsMonthly(firstDayOfMonth, lastDayOfMonth);
    }

    @GetMapping(value = "/chartdata/annually")
    @ResponseBody
    public List<ProductCountDTO> annuallyChartData() {
        LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate lastDayOfYear = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());

        return productRepository.fastMovingProductsAnnually(firstDayOfYear, lastDayOfYear);
    }

}
