package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {

    private final SelenideElement amountField = $("[data-test-id='amount'] .input__control");
    private final SelenideElement cardNumberFrom = $("[data-test-id='from'] .input__control");
    private final SelenideElement debitButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement headTransferPage = $(byText("Пополнение карты"));
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");


    public MoneyTransferPage() {
        headTransferPage.shouldBe(visible);
    }

    public void fillFields(String amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amount);
        cardNumberFrom.setValue(cardInfo.getCardNumber());
    }

    public DashboardPage transfer(String amount, DataHelper.CardInfo cardInfo) {
        fillFields(amount, cardInfo);
        debitButton.click();
        return null;
    }

    public DashboardPage successfulTransfer(String amount, DataHelper.CardInfo cardInfo) {
        transfer(amount, cardInfo);
        return new DashboardPage();
    }

    public DashboardPage cancelTransfer(String amount, DataHelper.CardInfo cardInfo) {
        fillFields(amount, cardInfo);
        cancelButton.click();
        return new DashboardPage();
    }

    public void findErrorNotification(String error) {
        errorNotification.shouldHave(exactText(error), Duration.ofSeconds(15)).shouldBe(visible);
    }
}