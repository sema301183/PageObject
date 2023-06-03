package ru.netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
        private long balance;
    }

    public static CardInfo getFirstCardInfo(AuthInfo authInfo) {
        return new CardInfo("5559 0000 0000 0001", 10_000);
    }

    public static CardInfo getSecondCardInfo(AuthInfo authInfo) {
        return new CardInfo("5559 0000 0000 0002", 10_000);
    }

    @Value
    public static class MoneyTransferInfo {
        private String refillAmount;
        private String senderCardNumber;
    }

    public static MoneyTransferInfo getMoneyTransferInfo(String senderCardNumber, long refillAmount) {
        return new MoneyTransferInfo(Long.toString(refillAmount), senderCardNumber);
    }

    public static MoneyTransferInfo getInvalidMoneyTransferInfo(long refillAmount) {
        return new MoneyTransferInfo(Long.toString(refillAmount), "6559 0000 0000 0006");
    }

}