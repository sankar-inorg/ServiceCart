package com.inorg.services.cart.controller;

import com.commercetools.api.models.cart.Cart;
import com.inorg.services.cart.models.CartRequest;
import com.inorg.services.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping(path = "/cart", produces = APPLICATION_JSON_VALUE)
public class CartController {
    private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping(value = "/new")
    public Cart createCart(@RequestBody(required = true) CartRequest cartRequest) {
        return cartService.createCart(cartRequest);
    }
}
