package com.inorg.services.cart.service;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.Order;
import com.inorg.services.cart.models.AddressRequest;
import com.inorg.services.cart.models.CartRequest;
import com.inorg.services.cart.models.CustomLineItemRequest;
import com.inorg.services.cart.models.LineItemRequest;
import com.inorg.services.cart.models.PaymentRequest;
import com.inorg.services.cart.models.ShippingMethodRequest;

public interface CartService {

    Cart createCart(CartRequest cartRequest);

    Cart addLineItem(LineItemRequest lineItemRequest, String cartId);

    Cart addCustomLineItem(CustomLineItemRequest customLineItemRequest, String cartId);

    Cart addShippingAddress(AddressRequest address, String cartId);

    Cart addShippingMethod(ShippingMethodRequest shippingMethodRequest, String cartId);

    Cart addPayment(PaymentRequest paymentRequest, String cartId);

    Order placeOrder(String cartId);
}
