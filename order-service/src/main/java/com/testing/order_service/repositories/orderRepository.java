package com.testing.order_service.repositories;

import com.testing.order_service.entities.orderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface orderRepository extends CrudRepository<orderEntity, Integer> {
}
