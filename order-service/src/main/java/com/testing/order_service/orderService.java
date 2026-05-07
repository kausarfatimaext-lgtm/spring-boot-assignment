package com.testing.order_service;

import com.testing.order_service.dtos.notificationDto;
import com.testing.order_service.dtos.productDto;
import com.testing.order_service.entities.orderEntity;
import com.testing.order_service.entities.productOrderEntity;
import jakarta.persistence.SecondaryTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.testing.order_service.repositories.orderRepository;
import com.testing.order_service.repositories.productOrderRepo;

import java.util.*;

@Service
@Slf4j
public class orderService {
    private final WebClient productClient;
    private final WebClient notificationClient;
    private orderRepository repository;
    private productOrderRepo repo;
    private orderEntity order;
    private productOrderEntity entity;

    public Mono<Boolean> getProductsStatus(Set<Integer> Ids, List<Integer> quantities) {
        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products/validateAndHandle")
                        .queryParam("Ids", Ids)
                        .queryParam("quantities", quantities)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public void notifyNotification(notificationDto dto){
        notificationClient.post()
                .uri("/notifications")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public orderService(WebClient productClient, WebClient notificationClient, orderRepository repository, productOrderRepo repo){
        this.productClient = productClient;
        this.notificationClient = notificationClient;
        this.repository = repository;
        this.repo = repo;
    }

    // call product-service to validate if product exist or not
    @Transactional
    public orderEntity createOrder(orderEntity order){
        Set<Integer> Ids = new HashSet<>();
        List<Integer> quantities = new ArrayList<>();
        productOrderEntity productOrderEntity;
        try {
            log.info("Here is the complete product list: {}", order.getProductsList());
            for(Integer productId : order.getProductsList().keySet()){
                Integer qty = order.getProductsList().get(productId);
                Ids.add(productId);
                quantities.add(qty);
            }
            if(Boolean.TRUE.equals(getProductsStatus(Ids, quantities).block())){
                this.order = order;
                repository.save(this.order);

                for(Integer productId : Ids){
                    productOrderEntity = new productOrderEntity();
                    productOrderEntity.setOrder_id(order.getId());
                    productOrderEntity.setProduct_id(productId);
                    repo.save(productOrderEntity);
                }
                log.info("{} is added successfully ", this.order.getName());
                notifyService(this.order, Ids);
            }
        } catch (Exception e) {
            log.info("On placing order following error is occured: {}",e.getMessage());
            throw new RuntimeException(e);
        }
        return this.order;
    }

    private void notifyService(orderEntity order, Set<Integer> Ids){
        try {
            notificationDto not = new notificationDto();
            not.setOrder_id(order.getId());
            not.setMessage("Order " + order.getName() + " with products list of " + Ids + " has been created");
            log.info("Here is the notification dto: {} {}", not.getOrder_id(), not.getMessage());
            notifyNotification(not);
        } catch (Exception e) {
            log.info("On sending notification following error is occured: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<orderEntity> getOrder(int Id){
        Optional<orderEntity> order = Optional.empty();
        try {
            order = repository.findById(Id);
        } catch (Exception e) {
            log.info("On getting order of {} following error is occurred: {}", Id, e.getMessage());
        }
        if(order.isEmpty()){
            log.info("Order of Id {} does not exist", Id);
        }
        return order;
    }

}
