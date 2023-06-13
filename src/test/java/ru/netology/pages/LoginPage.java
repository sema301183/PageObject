package ru.netology.pages;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

public class LoginPage {

    private SelenideElement login = $("[data-test-id='login'] input");
    private SelenideElement password = $("[data-test-id='password'] input");
    private SelenideElement buttonContinue = $("[data-test-id='action-login']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        login.setValue(info.getLogin());
        password.setValue(info.getPassword());
        buttonContinue.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo original) {
        login.setValue(original.getLogin());
        password.setValue(original.getPassword());
        buttonContinue.click();
        errorNotification.shouldBe(visible);

    }
}