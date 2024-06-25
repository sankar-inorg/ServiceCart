package com.inorg.services.cart.service;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartAddCustomLineItemAction;
import com.commercetools.api.models.cart.CartAddCustomLineItemActionBuilder;
import com.commercetools.api.models.cart.CartAddLineItemAction;
import com.commercetools.api.models.cart.CartAddLineItemActionBuilder;
import com.commercetools.api.models.cart.CartDraft;
import com.commercetools.api.models.cart.CartDraftBuilder;
import com.commercetools.api.models.cart.CartOrigin;
import com.commercetools.api.models.cart.CartUpdateAction;
import com.commercetools.api.models.cart.CartUpdateBuilder;
import com.commercetools.api.models.cart.InventoryMode;
import com.commercetools.api.models.cart.TaxMode;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.common.MoneyBuilder;
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

        CartDraft cartDraft = CartDraftBuilder.of()
                .locale("en-US")
                .currency(cartRequest.getCurrency())
                .country(cartRequest.getCountry())
                .origin(CartOrigin.CUSTOMER)
                .customerId(cartRequest.getCustomerId())
                .taxMode(TaxMode.DISABLED)
                .inventoryMode(InventoryMode.NONE)
                .build();

        return apiRoot.carts()
                .post(cartDraft)
                .executeBlocking()
                .getBody();

    }

    Cart getCartById(String cartId) {
        return apiRoot.carts()
                .withId(cartId)
                .get()
                .executeBlocking()
                .getBody();

    }

    private Cart executeUpdateActions(Cart cart, CartUpdateAction cartUpdateAction) {
        return apiRoot.carts()
                .withId(cart.getId())
                .post(CartUpdateBuilder.of()
                        .version(cart.getVersion())
                        .actions(cartUpdateAction)
                        .build())
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart addLineItem(LineItemRequest lineItemRequest, String cartId) {

        Cart cart = getCartById(cartId);
        CartAddLineItemAction cartAddLineItemAction = CartAddLineItemActionBuilder.of()
                .sku(lineItemRequest.getSku())
                .quantity(lineItemRequest.getQuantity())
                .build();

        return executeUpdateActions(cart, cartAddLineItemAction);
    }



    @Override
    public Cart addCustomLineItem(CustomLineItemRequest customLineItemRequest, String cartId) {
        Cart cart = getCartById(cartId);

        CartAddCustomLineItemAction cartAddCustomLineItemAction = CartAddCustomLineItemActionBuilder.of()
                .name(LocalizedStringBuilder.of().addValue("en-US", customLineItemRequest.getName()).build())
                .quantity(customLineItemRequest.getQuantity())
                .money(MoneyBuilder.of().currencyCode("USD").centAmount(customLineItemRequest.getPriceInCents()).build())
                .slug(customLineItemRequest.getName())
                .build();

        return executeUpdateActions(cart, cartAddCustomLineItemAction);
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
