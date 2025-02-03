package com.localkart.services.store.api.users.service;

import com.localkart.services.store.api.common.exceptions.BusinessValidationException;
import com.localkart.services.store.api.users.entity.UserEntity;
import com.localkart.services.store.api.users.model.CreateUserRequest;
import com.localkart.services.store.api.users.model.OtpDetails;
import com.localkart.services.store.api.users.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private static final String CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 4;
    private static SecureRandom secureRandom = new SecureRandom();

    private final UserRepository userRepository;
    private final SmsService smsService;

    private final Map<String, OtpDetails> otpMap = new ConcurrentHashMap<>();

    public UserService(UserRepository userRepository, SmsService smsService) {
        this.userRepository = userRepository;
        this.smsService = smsService;
    }

    public void loginWithOtp(String phoneNumber) {

        var user = this.findUserByPhoneNumber(phoneNumber);

        if (user != null) {
            String otp = generateOTP();
            smsService.sendSMS(phoneNumber, otp);
            otpMap.put(phoneNumber, new OtpDetails(otp, Instant.now().plus(5, ChronoUnit.MINUTES)));
        }
    }

    public boolean validateUser(String phoneNumber, String otp) {

        var otpDetails = otpMap.get(phoneNumber);

        return Optional.ofNullable(otpDetails)
                .filter(otpDetail -> otpDetail.getOtp().equals(otp) && Instant.now().isBefore(otpDetail.getValidTill()))
                .isPresent();
    }

    public UserEntity findUserByPhoneNumber(String phoneNumber) {

        return userRepository.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessValidationException("User Not Found", HttpStatus.NOT_FOUND));
    }

    public void createUser(CreateUserRequest request) {
        UserEntity userEntity = mapToUserEntity(request);
        userRepository.getUserByPhoneNumber(userEntity.getPhoneNumber())
                        .ifPresent(user -> {
                            throw new BusinessValidationException("User already exists", HttpStatus.CONFLICT);
                        });
        userRepository.saveUser(userEntity);
    }

    public UserEntity deleteUser(String phoneNumber) {
        userRepository.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessValidationException("User Not Found", HttpStatus.NOT_FOUND));

        return userRepository.deleteUser(phoneNumber);
    }

    public List<UserEntity> getAllUsers() {

        return userRepository.getAllUsers();
    }

    private static String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }
        return otp.toString();
    }

    private UserEntity mapToUserEntity(CreateUserRequest request) {
        var userEntity = new UserEntity();
        userEntity.setPhoneNumber(request.phoneNumber());
        userEntity.setFirstName(request.firstName());
        userEntity.setLastName(request.lastName());
        userEntity.setEmailId(request.emailId());

        return userEntity;
    }
}
