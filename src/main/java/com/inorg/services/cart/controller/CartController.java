package com.inorg.services.cart.controller;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.Order;
import com.inorg.services.cart.models.*;
import com.inorg.services.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping(value="/{cartID}/addlineitem")
    public Cart AddItem(@PathVariable String cartID, @RequestBody LineItemRequest lineItemRequest){
        return  cartService.addLineItem(lineItemRequest,cartID);
    }

    @PostMapping(value="/{cartID}/addcustomlineitem")
    public Cart AddCustomItem(@PathVariable String cartID, @RequestBody CustomLineItemRequest customLineItemRequest){
        return  cartService.addCustomLineItem(customLineItemRequest,cartID);
    }

    @PostMapping(value="/{cartID}/addshippingaddress")
    public Cart AddAddress(@PathVariable String cartID, @RequestBody AddressRequest addressRequest){
        return  cartService.addShippingAddress(addressRequest,cartID);
    }

    @PostMapping(value="/{cartID}/addshippingmethod")
    public Cart AddShippingMethod(@PathVariable String cartID, @RequestBody ShippingMethodRequest shippingMethodRequest){
        LOG.info("Shipping method : {}",shippingMethodRequest.getShippingMethod());
        return  cartService.addShippingMethod(shippingMethodRequest,cartID);
    }

    @PostMapping(value = "/{cartId}/payment")
    public Cart addPayment(@RequestBody(required = true) PaymentRequest paymentRequest, @PathVariable String cartId) {
        return cartService.addPayment(paymentRequest, cartId);
    }

    @PostMapping(value = "/{cartId}/place-order")
    public Order placeOrder(@PathVariable String cartId) {
        return cartService.placeOrder(cartId);
    }
}
