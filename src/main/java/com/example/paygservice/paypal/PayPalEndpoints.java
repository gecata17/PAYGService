package com.example.paygservice.paypal; 

public class PayPalEndpoints {

    public static final String SANDBOX_BASE_URL = "https://api-m.sandbox.paypal.com";
    public static final String LIVE_BASE_URL = "https://api-m.paypal.com";
    public static final String TOKEN_ENDPOINT = "/v1/oauth2/token";
    public static final String PAYMENT_ENDPOINT = "/v2/checkout/orders";
    public static final String PAYMENT_WITH_CARD_ENDPOINT = "/v2/vault/payment-tokens";

}
