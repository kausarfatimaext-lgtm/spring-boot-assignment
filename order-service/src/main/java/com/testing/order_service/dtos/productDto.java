package com.testing.order_service.dtos;

import lombok.Getter;

@Getter
public class productDto {
    private int Id;
    private String name;
    private String type;
    private int quantity;
    private double price;
}
