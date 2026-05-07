package com.testing.order_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class notificationDto {
    int Id;
    int order_id;
    String message;
}
