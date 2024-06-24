package com.inorg.services.cart.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    String email;
    String firstName;
    String lastName;
    String phone;
    String company;
    String address1;
    String address2;
    String city;
    String state;
    String zip;
    String country;
}