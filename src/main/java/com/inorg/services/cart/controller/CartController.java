package com.inorg.services.cart.controller;

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

    @PostMapping(value = "/{cartId}/line-item")
    public Cart addLineItem(@RequestBody(required = true) LineItemRequest lineItemRequest, @PathVariable String cartId) {
        return cartService.addLineItem(lineItemRequest, cartId);
    }

    @PostMapping(value = "/{cartId}/custom-line-item")
    public Cart addCustomLineItem(@RequestBody(required = true) CustomLineItemRequest customLineItemRequest, @PathVariable String cartId) {
        return cartService.addCustomLineItem(customLineItemRequest, cartId);
    }

    @PostMapping(value = "/{cartId}/shipping-address")
    public Cart addShippingAddress(@RequestBody(required = true) AddressRequest address, @PathVariable String cartId) {
        return cartService.addShippingAddress(address, cartId);
    }

    @PostMapping(value = "/{cartId}/shipping-method")
    public Cart addShippingMethod(@RequestBody(required = true) ShippingMethodRequest shippingMethodRequest, @PathVariable String cartId) {
        return cartService.addShippingMethod(shippingMethodRequest, cartId);
    }

    @PostMapping(value = "/{cartId}/payment")
    public Cart addPayment(@RequestBody(required = true) PaymentRequest paymentRequest, @PathVariable String cartId) {
        return cartService.addPayment(paymentRequest, cartId);
    }

    @PostMapping(value = "/{cartId}/place-order")
    public Order placeOrder(@PathVariable String cartId) {
        return cartService.placeOrder(cartId);
    }

    @PutMapping(value = "/{cartId}/tax-mode")
    public Cart updateCart(@PathVariable String cartId) {
        return cartService.updateCartTaxMode(cartId);
    }

    @PostMapping(value = "/{cartId}/apply-discount/{discountCode}")
    public Cart updateShippingMethod(@PathVariable String discountCode, @PathVariable String cartId) {
        return cartService.applyCartDiscount(discountCode, cartId);
    }

}

/* Cart Service Flow:
    - Create Cart
    - Add Line Item
    - Add Custom Line Item - Optional
    - Add Shipping Address
    - Add Shipping Method
    - Add Payment
    - Place Order */