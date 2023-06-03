package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

public class MoneyTransferTest {

    private LoginPage loginPage = new LoginPage();
    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private VerificationPage verificationPage = loginPage.validLogin(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);

    private DashboardPage dashboardPage = verificationPage.validVerify(authInfo, verificationCode);
    private long totalAmount = dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum();

    @Test
    void transferMoneyOneFromTwo() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getSecondCardNumber(),
                dashboardPage.getSecondCardSum() / 10);
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();

        Assertions.assertEquals(moneyTransferPage.getRecipientAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(moneyTransferPage.getSenderAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }


    @Test
    void transferMoneyTwoFromOne() {
        val moneyTransferPage = dashboardPage.refillTwoFromOne();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getFirstCardNumber(),
                dashboardPage.getFirstCardSum() / 10);
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();

        Assertions.assertEquals(moneyTransferPage.getRecipientAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(moneyTransferPage.getSenderAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }

    @Test
    void transferMoneyOneFromInvalidNumber() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getInvalidMoneyTransferInfo(dashboardPage.getSecondCardSum() / 10);
        moneyTransferPage.refillActionError(moneyTransferInfo);
        dashboardPage.isVisible();
    }

    @Test
    void zeroTransferMoneyTwoFromOne() {
        val moneyTransferPage = dashboardPage.refillTwoFromOne();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getFirstCardNumber(),
                dashboardPage.getFirstCardSum());
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();

        Assertions.assertEquals(totalAmount, dashboardPage.getSecondCardSum());
        Assertions.assertEquals(0, dashboardPage.getFirstCardSum());
    }

    @Test
    void cancelTransferMoney() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getSecondCardNumber(),
                dashboardPage.getSecondCardSum() / 10);
        moneyTransferPage.cancelAction(moneyTransferInfo);
        dashboardPage.updateAmounts();

        Assertions.assertEquals(moneyTransferPage.getStartRecipientAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(moneyTransferPage.getStartSenderAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }

    @Test
    void transferMoneyExcessAmount() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getSecondCardNumber(),
                dashboardPage.getSecondCardSum() + 10);
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();

        boolean testAmounts = dashboardPage.getSecondCardSum() >= 0
                && dashboardPage.getSecondCardSum() <= totalAmount
                && dashboardPage.getFirstCardSum() >= 0
                && dashboardPage.getFirstCardSum() <= totalAmount;

        Assertions.assertTrue(testAmounts);
    }


}