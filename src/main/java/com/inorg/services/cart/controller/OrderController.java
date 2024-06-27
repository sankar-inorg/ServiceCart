package com.inorg.services.cart.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.Order;
import com.inorg.services.cart.models.AddressRequest;
import com.inorg.services.cart.models.CartRequest;
import com.inorg.services.cart.models.CustomLineItemRequest;
import com.inorg.services.cart.models.LineItemRequest;
import com.inorg.services.cart.models.PaymentRequest;
import com.inorg.services.cart.models.ShippingMethodRequest;
import com.inorg.services.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = "/order", produces = APPLICATION_JSON_VALUE)
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private final CartService cartService;

    public OrderController(CartService cartService) {
        this.cartService = cartService;
    }


    @PutMapping(value = "/{orderId}/order-number/{orderNumber}")
    public Order updateOrderNumber(@PathVariable String orderNumber, @PathVariable String orderId) {
        return cartService.updateOrderNumber(orderNumber, orderId);
    }

    @PutMapping(value = "/{orderId}/line-item/{lineItemId}")
    public Order updateOrderLineItemQty(@PathVariable String lineItemId, @PathVariable String orderId, @RequestParam Long quantity) {
        return cartService.updateOrderLineItemQty(lineItemId, quantity, orderId);
    }

}
