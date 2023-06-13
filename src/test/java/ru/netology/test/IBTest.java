package ru.netology.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.generateValidAmount;


public class IBTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void cleanUp() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @Test
    void shouldTransferFromFirstCardToSecondCard() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getFirstCardBalance(0);
        var secondCardBalance = dashboardPage.getSecondCardBalance(1);
        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalanceAfterTransfer = firstCardBalance - amount;
        var expectedSecondCardBalanceAfterTransfer = secondCardBalance + amount;
        var moneyTransferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = moneyTransferPage.successfulTransfer(valueOf(amount), firstCardInfo);
        var actualFirstCardBalanceAfterTransfer = dashboardPage.getFirstCardBalance(0);
        var actualFSecondCardBalanceAfterTransfer = dashboardPage.getSecondCardBalance(1);
        assertEquals(expectedFirstCardBalanceAfterTransfer, actualFirstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalanceAfterTransfer, actualFSecondCardBalanceAfterTransfer);

    }

    @Test
    void shouldTransferFromSecondCardToFirst() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getFirstCardBalance(0);
        var secondCardBalance = dashboardPage.getSecondCardBalance(1);
        var amount = generateValidAmount(secondCardBalance);
        var firstCardBalanceAfterTransfer = firstCardBalance + amount;
        var secondCardBalanceAfterTransfer = secondCardBalance - amount;
        var moneyTransferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = moneyTransferPage.successfulTransfer(valueOf(amount), secondCardInfo);
        var actualFirstCardBalanceAfterTransfer = dashboardPage.getFirstCardBalance(0);
        var actualFSecondCardBalanceAfterTransfer = dashboardPage.getSecondCardBalance(1);
        assertEquals(firstCardBalanceAfterTransfer, actualFirstCardBalanceAfterTransfer);
        assertEquals(secondCardBalanceAfterTransfer, actualFSecondCardBalanceAfterTransfer);
    }

    @Test
    void transferIfAmountOverLimit() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getFirstCardBalance(0);
        var secondCardBalance = dashboardPage.getSecondCardBalance(1);
        var amount = generateInvalidAmount(40000);
        var moneyTransferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        moneyTransferPage.transfer(valueOf(amount), firstCardInfo);
        moneyTransferPage.findErrorNotification("Перевод невозможен. На карте недостаточно средств");
        var actualFirstCardBalanceAfterTransfer = dashboardPage.getFirstCardBalance(0);
        var actualFSecondCardBalanceAfterTransfer = dashboardPage.getSecondCardBalance(1);
        assertEquals(firstCardBalance, actualFirstCardBalanceAfterTransfer);
        assertEquals(secondCardBalance, actualFSecondCardBalanceAfterTransfer);


    }

}