package com.inorg.services.cart.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.commercetools.api.models.order.Order;
import com.inorg.services.cart.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = "/order", produces = APPLICATION_JSON_VALUE)
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping(value = "/{orderId}/order-number/{orderNumber}")
    public Order updateOrderNumber(@PathVariable String orderNumber, @PathVariable String orderId) {
        return orderService.updateOrderNumber(orderNumber, orderId);
    }

    @PutMapping(value = "/{orderId}/line-item/{lineItemId}")
    public Order updateOrderLineItemQty(@PathVariable String lineItemId, @PathVariable String orderId, @RequestParam Long quantity) {
        return orderService.updateOrderLineItemQty(lineItemId, quantity, orderId);
    }
}