package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {
        ru.practicum.feign_client.WarehouseClient.class,
        ru.practicum.feign_client.PaymentClient.class,
        ru.practicum.feign_client.DeliveryClient.class
})
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
