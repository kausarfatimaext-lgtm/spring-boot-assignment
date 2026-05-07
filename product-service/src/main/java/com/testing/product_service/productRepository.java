package com.testing.product_service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productRepository extends CrudRepository<productEntity, Integer> {
    productEntity findByName(String name);
}
