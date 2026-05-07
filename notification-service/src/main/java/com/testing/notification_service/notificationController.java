package com.testing.notification_service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class notificationController {
    private notificationService service;
    public notificationController(notificationService service){
        this.service = service;
    }

    @PostMapping()
    public notificationEntity createNotification(@RequestBody notificationEntity notification){
        return service.createNotification(notification);
    }
}
