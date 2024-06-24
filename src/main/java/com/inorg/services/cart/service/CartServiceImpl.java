package com.inorg.services.cart.service;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.Order;
import com.inorg.services.cart.models.AddressRequest;
import com.inorg.services.cart.models.CartRequest;
import com.inorg.services.cart.models.CustomLineItemRequest;
import com.inorg.services.cart.models.LineItemRequest;
import com.inorg.services.cart.models.PaymentRequest;
import com.inorg.services.cart.models.ShippingMethodRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    private final ProjectApiRoot apiRoot;

    public CartServiceImpl(ProjectApiRoot apiRoot) {
        this.apiRoot = apiRoot;
    }
    @Override
    public Cart createCart(CartRequest cartRequest) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Cart addLineItem(LineItemRequest lineItemRequest, String cartId) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Cart addCustomLineItem(CustomLineItemRequest customLineItemRequest, String cartId) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Cart addShippingAddress(AddressRequest address, String cartId) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Cart addShippingMethod(ShippingMethodRequest shippingMethodRequest, String cartId) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Cart addPayment(PaymentRequest paymentRequest, String cartId) {
        //TODO: Implement this method
        return null;
    }

    @Override
    public Order placeOrder(String cartId) {
        //TODO: Implement this method
        return null;
    }
}
