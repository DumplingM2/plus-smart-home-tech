package ru.practicum.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.NoOrderFoundException;
import ru.practicum.feign_client.exception.order.NoOrderFoundException as FeignNoOrderFoundException;
import ru.practicum.feign_client.exception.shopping_cart.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.feign_client.exception.warehouse.ProductNotFoundInWarehouseException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoOrderFoundException(NoOrderFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Order not found",
                e.getMessage(),
                "NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FeignNoOrderFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeignNoOrderFoundException(FeignNoOrderFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Order not found",
                e.getMessage(),
                "NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProductNotFoundInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundInWarehouseException(ProductNotFoundInWarehouseException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Product not found in warehouse",
                e.getMessage(),
                "NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartLowQuantityInWarehouseException(ProductInShoppingCartLowQuantityInWarehouseException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Insufficient quantity in warehouse",
                e.getMessage(),
                "BAD_REQUEST"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                e.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    public record ErrorResponse(String error, String message, String status) {}
}