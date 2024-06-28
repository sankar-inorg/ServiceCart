package com.inorg.services.cart.service;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartAddCustomLineItemAction;
import com.commercetools.api.models.cart.CartAddCustomLineItemActionBuilder;
import com.commercetools.api.models.cart.CartAddLineItemAction;
import com.commercetools.api.models.cart.CartAddLineItemActionBuilder;
import com.commercetools.api.models.cart.CartAddPaymentAction;
import com.commercetools.api.models.cart.CartAddPaymentActionBuilder;
import com.commercetools.api.models.cart.CartDraft;
import com.commercetools.api.models.cart.CartDraftBuilder;
import com.commercetools.api.models.cart.CartOrigin;
import com.commercetools.api.models.cart.CartResourceIdentifierBuilder;
import com.commercetools.api.models.cart.CartSetShippingAddressAction;
import com.commercetools.api.models.cart.CartSetShippingAddressActionBuilder;
import com.commercetools.api.models.cart.CartSetShippingMethodAction;
import com.commercetools.api.models.cart.CartSetShippingMethodActionBuilder;
import com.commercetools.api.models.cart.CartUpdateAction;
import com.commercetools.api.models.cart.CartUpdateBuilder;
import com.commercetools.api.models.cart.InventoryMode;
import com.commercetools.api.models.cart.TaxMode;
import com.commercetools.api.models.common.BaseAddressBuilder;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.common.MoneyBuilder;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraftBuilder;
import com.commercetools.api.models.payment.Payment;
import com.commercetools.api.models.payment.PaymentDraft;
import com.commercetools.api.models.payment.PaymentDraftBuilder;
import com.commercetools.api.models.payment.PaymentMethodInfoBuilder;
import com.commercetools.api.models.payment.PaymentResourceIdentifierBuilder;
import com.commercetools.api.models.shipping_method.ShippingMethodResourceIdentifierBuilder;
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

    private Cart getCartById(String cartId) {
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

    private Payment createPayment(PaymentRequest paymentRequest) {
        PaymentDraft paymentDraft = PaymentDraftBuilder.of()
                .interfaceId(paymentRequest.getPaymentInterface())
                .amountPlanned(MoneyBuilder.of().currencyCode("USD").centAmount(paymentRequest.getAmountInCents()).build())
                .paymentMethodInfo(PaymentMethodInfoBuilder.of()
                        .name(LocalizedStringBuilder.of().addValue("en-US", paymentRequest.getName()).build())
                        .paymentInterface(paymentRequest.getPaymentInterface())
                        .method("CREDIT_CARD")
                        .build())
                .build();
        return apiRoot.payments()
                .post(paymentDraft)
                .executeBlocking()
                .getBody();
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
        Cart cart = getCartById(cartId);
        CartSetShippingAddressAction cartSetShippingAddressAction = CartSetShippingAddressActionBuilder.of()
                .address(BaseAddressBuilder.of()
                        .country(address.getCountry())
                        .build())
                .build();
        return executeUpdateActions(cart, cartSetShippingAddressAction);
    }

    @Override
    public Cart addShippingMethod(ShippingMethodRequest shippingMethodRequest, String cartId) {
        Cart cart = getCartById(cartId);
        CartSetShippingMethodAction cartSetShippingMethodAction = CartSetShippingMethodActionBuilder.of()
                .shippingMethod(ShippingMethodResourceIdentifierBuilder.of()
                        .key(shippingMethodRequest.getShippingMethod())
                        .build())
                .build();
        return executeUpdateActions(cart, cartSetShippingMethodAction);
    }

    @Override
    public Cart addPayment(PaymentRequest paymentRequest, String cartId) {
        Cart cart = getCartById(cartId);
        Payment payment = createPayment(paymentRequest);
        CartAddPaymentAction cartAddPaymentAction = CartAddPaymentActionBuilder.of()
                .payment(PaymentResourceIdentifierBuilder.of()
                        .id(payment.getId())
                        .build())
                .build();
        return executeUpdateActions(cart, cartAddPaymentAction);
    }

    @Override
    public Order placeOrder(String cartId) {
        Cart cart = getCartById(cartId);
        return apiRoot.orders()
                .post(OrderFromCartDraftBuilder.of()
                        .cart(CartResourceIdentifierBuilder.of()
                                .id(cartId)
                                .build())
                        .version(cart.getVersion())
                        .build())
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart updateCartTaxMode(String cartId) {
        return null;
    }

    @Override
    public Cart applyCartDiscount(String discountCode, String cartId) {
        return null;
    }

    @Override
    public Order updateOrderNumber(String orderNumber, String orderId) {
        return null;
    }

    @Override
    public Order updateOrderLineItemQty(String lineItemId, Long quantity, String orderId) {
        return null;
    }
}