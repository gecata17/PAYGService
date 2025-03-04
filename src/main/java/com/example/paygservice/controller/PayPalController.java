package com.example.paygservice.controller;

import com.example.paygservice.paypal.PayPalService;
import com.example.paygservice.requests.PaymentRequest;
import com.example.paygservice.requests.CaptureRequest;
import com.example.paygservice.requests.PaymentMethodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

/*For next time to integrate the API key and secret key:
 *   "PayPalSettings": {
    "ClientId": "AeMgBRx45hAazjh6X_170SZD59UTXO6hUAg2J13d0xJjC7n7q3ZZAbOQGfN4aBT9kxywJtzbC3jGtFJ3",
    "ClientSecret": "EEytSWdFxq2GleRcCSKy7xtuJElY_ck2xipWMp8AqrUI4wsG6rZG2rpT8w1EaqVqUAsoSfzbibkvveWF",
    "Mode": "sandbox"
  },
 */


@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    private final PayPalService payPalService;

    @Autowired
    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/create-order")
    public Mono<ResponseEntity<Object>> createOrder(@RequestBody PaymentRequest request) {
        return payPalService.createOrderWithPaymentMethod(
                BigDecimal.valueOf(request.getAmount()), 
                request.getCurrency(), 
                request.getPaymentMethodToken(), 
                request.getReturnUrl(), 
                request.getCancelUrl())
                .map(result -> {
                    if (request.getPaymentMethodToken() == null || request.getPaymentMethodToken().isEmpty()) {
                        return ResponseEntity.ok().body((Object) new ApprovalUrlResponse(result));
                    } else {
                        return ResponseEntity.ok().body((Object) new OrderIdResponse(result));
                    }
                })
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body((Object) new ErrorResponse(ex.getMessage()))));
    }

    @PostMapping("/capture-order")
    public Mono<ResponseEntity<Object>> captureOrder(@RequestBody CaptureRequest request) {
        return payPalService.captureOrder(request.getToken(), request.getPayerId())
                .map(result -> ResponseEntity.ok().body((Object) new CaptureResponse(result)))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body((Object) new ErrorResponse(ex.getMessage()))));
    }

    @PostMapping("/create-payment-method")
    public Mono<ResponseEntity<Object>> createPaymentMethod(@RequestBody PaymentMethodRequest request) {
        return payPalService.createPaymentMethod(
                request.getCardNumber(), 
                request.getExpiryDate(), 
                request.getCVV())
                .map(token -> ResponseEntity.ok().body((Object) new PaymentMethodTokenResponse(token)))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body((Object) new ErrorResponse(ex.getMessage()))));
    }

    // Response classes
    static class ApprovalUrlResponse {
        private String approvalUrl;

        public ApprovalUrlResponse(String approvalUrl) {
            this.approvalUrl = approvalUrl;
        }

        public String getApprovalUrl() {
            return approvalUrl;
        }

        public void setApprovalUrl(String approvalUrl) {
            this.approvalUrl = approvalUrl;
        }
    }

    static class OrderIdResponse {
        private String orderId;

        public OrderIdResponse(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }

    static class CaptureResponse {
        private String result;

        public CaptureResponse(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    static class PaymentMethodTokenResponse {
        private String paymentMethodToken;

        public PaymentMethodTokenResponse(String paymentMethodToken) {
            this.paymentMethodToken = paymentMethodToken;
        }

        public String getPaymentMethodToken() {
            return paymentMethodToken;
        }

        public void setPaymentMethodToken(String paymentMethodToken) {
            this.paymentMethodToken = paymentMethodToken;
        }
    }

    static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
