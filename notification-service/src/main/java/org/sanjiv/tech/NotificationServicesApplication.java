package org.sanjiv.tech;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServicesApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        // send out Email Notification
        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }
}
