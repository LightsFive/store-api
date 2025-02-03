package com.localkart.services.store.api.users.model;

import java.time.Instant;

public class OtpDetails {

    private String otp;
    private Instant validTill;

    public OtpDetails() {
    }

    public OtpDetails(String otp, Instant validTill) {
        this.otp = otp;
        this.validTill = validTill;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Instant getValidTill() {
        return validTill;
    }

    public void setValidTill(Instant validTill) {
        this.validTill = validTill;
    }
}
