package com.localkart.services.store.api.users.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private static final String OTP_MESSAGE_FORMAT = "Hello! Your localkart OTP for login is %s. Enter this code to access your account. If you didn't request this OTP, please reach out to our support team immediately.";
    private Twilio twilio;

    @PostConstruct
    public void init() {

        Twilio.init(
                System.getenv("TWILIO_ACCOUNT_SID"),
                System.getenv("TWILIO_AUTH_TOKEN"));
    }

    public void sendSMS(String phoneNumber, String otp) {
        var messageBody = String.format(OTP_MESSAGE_FORMAT, otp);
        Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber("+18457129133"),
                        messageBody)
                .create();
    }
}
