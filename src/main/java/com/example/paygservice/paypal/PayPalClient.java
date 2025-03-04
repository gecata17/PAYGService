package com.example.paygservice.paypal; 

public class PayPalClient {

    private String mode;
    private String clientId;
    private String clientSecret;

    public PayPalClient(String mode, String clientId, String clientSecret) {
        this.mode = mode;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    // Getter for clientId
    public String getClientId() {
        return clientId;
    }

    // Setter for clientId
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    // Getter for clientSecret
    public String getClientSecret() {
        return clientSecret;
    }

    // Setter for clientSecret
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    // Getter for mode
    public String getMode() {
        return mode;
    }

    // Setter for mode
    public void setMode(String mode) {
        this.mode = mode;
    }

    // Base URL property
    public String getBaseUrl() {
        return "sandbox".equals(mode) ? PayPalEndpoints.SANDBOX_BASE_URL : PayPalEndpoints.LIVE_BASE_URL;
    }
}
