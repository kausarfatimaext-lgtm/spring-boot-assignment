package com.testing.order_service.repositories;

import com.testing.order_service.entities.productOrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productOrderRepo extends CrudRepository<productOrderEntity, Integer> {
}
