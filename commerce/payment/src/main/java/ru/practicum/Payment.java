package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {
        ru.practicum.feign_client.OrderClient.class,
        ru.practicum.feign_client.StoreClient.class
})
public class Payment {
    public static void main(String[] args) {
        SpringApplication.run(Payment.class, args);
    }
}
