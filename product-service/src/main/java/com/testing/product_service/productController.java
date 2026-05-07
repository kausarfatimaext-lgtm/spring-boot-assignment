package com.testing.product_service;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class productController {
    private productService service;
    public productController(productService service){
        this.service = service;
    }

    @PostMapping()
    public productEntity createProduct(@RequestBody productEntity product){
        return service.createProduct(product);
    }

    @GetMapping("/{Id}")
    public Optional<productEntity> getProduct(@PathVariable Integer Id){
        return service.getProduct(Id);
    }

    @GetMapping()
    public List<productEntity> getAllProducts(){
        return service.getAllProducts();
    }

    @GetMapping("/validateAndHandle")
    public boolean checkProduct(@RequestParam Set<Integer> Ids, @RequestParam List<Integer> quantities){
        return service.validateProducts(Ids, quantities);
    }
}
