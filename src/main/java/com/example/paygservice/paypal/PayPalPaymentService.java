package com.example.paygservice.paypal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PayPalPaymentService implements PayPalService {

    private final PayPalClient payPalClient;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Mono<String> getAccessTokenAsync() {
        String authToken = Base64.getEncoder().encodeToString(
                (payPalClient.getClientId() + ":" + payPalClient.getClientSecret()).getBytes(StandardCharsets.UTF_8)
        );

        return webClient.post()
                .uri(payPalClient.getBaseUrl() + PayPalEndpoints.TOKEN_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + authToken)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Failed to retrieve PayPal access token. Response: " + error))))
                .bodyToMono(JsonNode.class)
                .map(tokenResult -> tokenResult.get("access_token").asText());
    }

    @Override
    public Mono<String> createOrderWithPaymentMethod(BigDecimal amount, String currency, String paymentMethodToken, String returnUrl, String cancelUrl) {
        return getAccessTokenAsync()
                .flatMap(accessToken -> {
                    WebClient.RequestHeadersSpec<?> requestSpec = webClient.post()
                            .uri(payPalClient.getBaseUrl() + PayPalEndpoints.PAYMENT_ENDPOINT)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .bodyValue(createOrderPayload(amount, currency, paymentMethodToken, returnUrl, cancelUrl));

                    return requestSpec.retrieve()
                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(error -> Mono.error(new RuntimeException("Failed to create PayPal order. Response: " + error))))
                            .bodyToMono(JsonNode.class)
                            .map(orderResult -> {
                                if (paymentMethodToken == null || paymentMethodToken.isEmpty()) {
                                    return orderResult.get("links").elements()
                                            .next()
                                            .get("href").asText();
                                } else {
                                    return orderResult.get("id").asText();
                                }
                            });
                });
    }

    private Object createOrderPayload(BigDecimal amount, String currency, String paymentMethodToken, String returnUrl, String cancelUrl) {
        return new Object() {
            public final String intent = "CAPTURE";
            public final Object[] purchase_units = new Object[]{
                    new Object() {
                        public final Object amountDetails = new Object() {
                            public final String currency_code = currency;
                            public final String value = amount.setScale(2).toString();
                        };
                    }
            };
            public final Object payment_source = paymentMethodToken != null && !paymentMethodToken.isEmpty() ?
                    new Object() {
                        public final Object token = new Object() {
                            public final String id = paymentMethodToken;
                            public final String type = "PAYMENT_METHOD_TOKEN";
                        };
                    } : null;
            public final Object application_context = new Object() {
                public final String return_url = returnUrl;
                public final String cancel_url = cancelUrl;
            };
        };
    }

    @Override
    public Mono<String> captureOrder(String token, String payerId) {
        return getAccessTokenAsync()
                .flatMap(accessToken -> {
                    String captureEndpoint = payPalClient.getBaseUrl() + PayPalEndpoints.PAYMENT_ENDPOINT + "/" + token + "/capture";

                    return webClient.post()
                            .uri(captureEndpoint)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .body(BodyInserters.fromValue("{\"payer_id\": \"" + payerId + "\"}"))
                            .retrieve()
                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(error -> Mono.error(new RuntimeException("Failed to capture PayPal payment. Response: " + error))))
                            .bodyToMono(String.class);
                });
    }

    @Override
    public Mono<String> createPaymentMethod(String cardNumber, String expiryDate, String cvv) {
        return getAccessTokenAsync()
                .flatMap(accessToken -> {
                    Object payload = new Object() {
                        public final String type = "CARD";
                        public final Object source = new Object() {
                            public final Object card = new Object() {
                                public final String number = cardNumber;
                                public final String expiry = expiryDate;
                                public final String security_code = cvv;
                            };
                        };
                    };

                    return webClient.post()
                            .uri(payPalClient.getBaseUrl() + PayPalEndpoints.PAYMENT_WITH_CARD_ENDPOINT)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .bodyValue(payload)
                            .retrieve()
                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(error -> Mono.error(new RuntimeException("Failed to tokenize card. Response: " + error))))
                            .bodyToMono(JsonNode.class)
                            .map(result -> result.get("id").asText());
                });
    }
}
