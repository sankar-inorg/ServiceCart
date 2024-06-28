package com.inorg.services.cart.service;

import com.commercetools.api.models.order.Order;

public interface OrderService {
    Order updateOrderNumber(String orderNumber, String orderId);

    Order updateOrderLineItemQty(String lineItemId, Long quantity, String orderId);
}