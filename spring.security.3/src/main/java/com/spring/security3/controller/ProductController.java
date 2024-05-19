package com.spring.security3.controller;

import com.spring.security3.dto.Product;
import com.spring.security3.entity.UserInfo;
import com.spring.security3.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  private ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome this endpoint is not secure";
  }


  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<Product> getAllTheProducts() {
    return service.getProducts();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public Product getProductById(@PathVariable int id) {
    return service.getProduct(id);
  }

  @PostMapping("/new")
  public String addNewUser(@RequestBody UserInfo userInfo) {
    return service.addNewUser(userInfo);
  }
}
