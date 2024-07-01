package com.inorg.services.cart.service;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.*;
import com.commercetools.api.models.order.*;
import com.commercetools.api.models.order_edit.*;
import com.commercetools.api.models.payment.*;
import com.commercetools.api.models.shipping_method.ShippingMethodResourceIdentifier;
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

import java.util.List;

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
                .customerId(cartRequest.getCustomerId())
                .taxMode(TaxMode.DISABLED)
                .inventoryMode(InventoryMode.NONE)
                .build();

        return apiRoot.carts().create(cartDraft).executeBlocking().getBody();
    }

    @Override
    public Cart addLineItem(LineItemRequest lineItemRequest, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();
        CartUpdateAction addlineitem = CartAddLineItemActionBuilder.of()
                .sku(lineItemRequest.getSku())
                .quantity(lineItemRequest.getQuantity())
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(addlineitem))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart addCustomLineItem(CustomLineItemRequest customLineItemRequest, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        CartUpdateAction addcustomlineitem = CartAddCustomLineItemActionBuilder.of()
                .name(LocalizedStringBuilder.of().addValue("en-US",customLineItemRequest.getName()).build())
                .quantity(customLineItemRequest.getQuantity())
                .money(MoneyBuilder.of()
                        .currencyCode("INR")
                        .centAmount(customLineItemRequest.getPriceInCents()).build())
                .slug(customLineItemRequest.getKey())
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(addcustomlineitem))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart addShippingAddress(AddressRequest address, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        CartSetShippingAddressAction cartSetShippingAddressAction = CartSetShippingAddressActionBuilder.of()
                .address(BaseAddressBuilder.of()
                        .country(address.getCountry())
                        .build())
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(cartSetShippingAddressAction))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }
    //c9aa13cc-8f75-4532-9ea5-b664b279d673
    @Override
    public Cart addShippingMethod(ShippingMethodRequest shippingMethodRequest, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        CartSetShippingMethodAction cartSetShippingMethodAction = CartSetShippingMethodActionBuilder.of()
                .shippingMethod(ShippingMethodResourceIdentifierBuilder.of()
                        .id(shippingMethodRequest.getShippingMethod())
                        .build())
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(cartSetShippingMethodAction))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart addPayment(PaymentRequest paymentRequest, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        PaymentDraft paymentDraft = PaymentDraftBuilder.of()
                .interfaceId(paymentRequest.getPaymentInterface())
                .amountPlanned(MoneyBuilder.of().currencyCode("INR").centAmount(paymentRequest.getAmountInCents()).build())
                .paymentMethodInfo(PaymentMethodInfoBuilder.of()
                        .name(LocalizedStringBuilder.of().addValue("en-US", paymentRequest.getName()).build())
                        .paymentInterface(paymentRequest.getPaymentInterface())
                        .method("CREDIT_CARD")
                        .build())
                .build();

        Payment payment =apiRoot.payments()
                .post(paymentDraft)
                .executeBlocking()
                .getBody();

        CartAddPaymentAction cartAddPaymentAction = CartAddPaymentActionBuilder.of()
                .payment(PaymentResourceIdentifierBuilder.of()
                        .id(payment.getId())
                        .build())
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(cartAddPaymentAction))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Order placeOrder(String cartId) {

        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

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
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        CartChangeTaxModeAction changeTaxModeAction = CartChangeTaxModeActionBuilder.of()
                .taxMode(TaxMode.PLATFORM)
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(changeTaxModeAction))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Cart applyCartDiscount(String discountCode, String cartId) {
        Cart cart = apiRoot.carts().withId(cartId).get().executeBlocking().getBody();

        CartAddDiscountCodeAction cartAddDiscountCodeAction = CartAddDiscountCodeActionBuilder.of()
                .code(discountCode)
                .build();

        CartUpdate updates = CartUpdateBuilder.of()
                .version(cart.getVersion())
                .actions(List.of(cartAddDiscountCodeAction))
                .build();

        return apiRoot.carts()
                .withId(cartId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Order updateOrderNumber(String orderNumber, String orderId) {
        Order order = apiRoot.orders().withId(orderId).get().executeBlocking().getBody();

        OrderSetOrderNumberAction orderSetOrderNumberAction = OrderSetOrderNumberActionBuilder.of()
                .orderNumber(orderNumber)
                .build();

        OrderUpdate updates = OrderUpdateBuilder.of()
                .version(order.getVersion())
                .actions(List.of(orderSetOrderNumberAction))
                .build();

        return apiRoot.orders()
                .withId(orderId)
                .post(updates)
                .executeBlocking()
                .getBody();
    }

    @Override
    public Order updateOrderLineItemQty(String lineItemId, Long quantity, String orderId) {
        Order order = apiRoot.orders().withId(orderId).get().executeBlocking().getBody();

        StagedOrderChangeLineItemQuantityAction orderSetOrderNumberAction = StagedOrderChangeLineItemQuantityActionBuilder.of()
                .lineItemId(lineItemId)
                .quantity(quantity)
                .build();

        OrderEditAddStagedActionAction orderEditAddStagedActionAction = OrderEditAddStagedActionActionBuilder.of()
                .stagedAction(orderSetOrderNumberAction)
                .build();

        OrderEditUpdateAction orderEditUpdateAction = OrderEditUpdateActionBuilder.of()
                .addStagedActionBuilder().stagedAction()

        OrderEditUpdate orderEditUpdate = OrderEditUpdateBuilder.of()
                .version(1L)
                .actions(orderEdit)
                .build();

        return apiRoot.orders().edits()
                .withId(orderId)
                .post(orderEdit)
                .executeBlocking()
                .getBody();
    }
}
