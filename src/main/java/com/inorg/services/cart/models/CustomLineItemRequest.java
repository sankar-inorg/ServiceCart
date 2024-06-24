package com.inorg.services.cart.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomLineItemRequest {
    @JsonProperty("name")
    @NotNull String name;

    @JsonProperty("key")
    @NotNull String key;

    @JsonProperty("price")
    @NotNull Long priceInCents;

    @JsonProperty("quantity")
    Long quantity = 1L;

}
