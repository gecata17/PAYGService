package com.example.paygservice.paypal; 

import reactor.core.publisher.Mono;
import java.math.BigDecimal;

public interface PayPalService {

    /**
     * Create an order with a specific payment method.
     *
     * @param amount            the amount of the order
     * @param currency          the currency to use
     * @param paymentMethodToken the token of the payment method
     * @param returnUrl         the URL to redirect after a successful payment
     * @param cancelUrl         the URL to redirect if payment is canceled
     * @return a Mono that will contain the order ID as a String
     */
    Mono<String> createOrderWithPaymentMethod(BigDecimal amount, String currency, String paymentMethodToken, String returnUrl, String cancelUrl);

    /**
     * Capture a previously created order.
     *
     * @param token   the token of the order to capture
     * @param payerId the ID of the payer
     * @return a Mono that will contain the captured order ID as a String
     */
    Mono<String> captureOrder(String token, String payerId);

    /**
     * Create a new payment method using card information.
     *
     * @param cardNumber the card number
     * @param expiryDate the expiration date of the card (in MM/YY format)
     * @param cvv        the card verification value (CVV)
     * @return a Mono that will contain the payment method token as a String
     */
    Mono<String> createPaymentMethod(String cardNumber, String expiryDate, String cvv);
}