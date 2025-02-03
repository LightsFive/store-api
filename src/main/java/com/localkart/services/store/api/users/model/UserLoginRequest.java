package com.localkart.services.store.api.users.model;


public class UserLoginRequest {

    private String phoneNumber;
    private String otp;

    public UserLoginRequest() {
    }

    public UserLoginRequest(String phoneNumber, String otp) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
