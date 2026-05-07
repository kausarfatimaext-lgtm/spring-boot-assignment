package com.testing.product_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class productService {
    private productRepository repository;
    private productEntity product;

    public productService(productRepository repository){
        this.repository = repository;
    }

    private void decreaseStock(int Id, int quantity){
        Optional<productEntity> product = getProduct(Id);
        log.info("Here is the id and quantity {} {}", Id, quantity);
        if(product.isEmpty()){
            throw new RuntimeException("Product not found");
        }

        productEntity p = product.get();

        if(p.getQuantity() < quantity){
            throw new RuntimeException("Insufficient stock");
        }

        p.setQuantity(product.get().getQuantity() - quantity);
        repository.save(p);
    }

    public productEntity createProduct(productEntity product){
        try {
            this.product = product;
            this.product = repository.save(this.product);
            log.info("{} is added successfully ", this.product.getName());
        } catch (Exception e){
            log.info("On creating product following error is occured: {}",e.getMessage());
        }
        return this.product;
    }

    public Optional<productEntity> getProduct(int Id){
        Optional<productEntity> product = Optional.empty();
        try {
            product = repository.findById(Id);
            log.info("here are the products {}", repository.findAll());
        } catch (Exception e){
            log.info("On getting product of {} following error is occured: {}", Id, e.getMessage());
        }
        if(product.isEmpty()){
            log.info("Product of Id {} does not exist", Id);
        }
        return product;
    }

    public List<productEntity> getAllProducts(){
        List<productEntity> products = new ArrayList<>();
        try {
            for(productEntity prod: repository.findAll()){
                products.add(prod);
            }
        } catch (Exception e){
            log.info("On getting products following error is occured: {}", e.getMessage());
        }
        if(products.isEmpty()){
            log.info("Products list is empty");
        }
        return products;
    }

    private boolean checkProductQuantity(int Id, int quantity){
        try {
            Optional<productEntity> product = repository.findById(Id);
            if(product.isEmpty()){
                log.info("Product {} does not exist", Id);
                return false;
            }
            double quan = product.get().getQuantity();
            if(quan < quantity){
                log.info("Insufficient stock against product of Id {} ", Id);
                return false;
            }
            return true;
        } catch (Exception e){
            log.info("Following error occurred on checking ProductQuantity: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean validateProducts(Set<Integer> Ids, List<Integer> quantities){
        int index = 0;
        for(Integer id: Ids){
            Integer quantity = quantities.get(index);
            if(!checkProductQuantity(id, quantity)){
                return false;
            }
            decreaseStock(id, quantity);
            index++;
        }
        return true;
    }
}
