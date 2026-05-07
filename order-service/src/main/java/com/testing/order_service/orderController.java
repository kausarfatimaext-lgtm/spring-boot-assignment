package com.testing.order_service;

import com.testing.order_service.entities.orderEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class orderController {
    private orderService service;
    public orderController(orderService service){
        this.service = service;
    }

    @PostMapping()
    public orderEntity createOrder(@RequestBody orderEntity order){
        return service.createOrder(order);
    }

    @GetMapping("/{id}")
    public Optional<orderEntity> getOrder(@PathVariable Integer Id){
        return service.getOrder(Id);
    }
}
