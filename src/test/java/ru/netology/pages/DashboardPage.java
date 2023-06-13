package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

  private final ElementsCollection cards = $$(".list__item div");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";
  private final SelenideElement heading = $("[data-test-id='dashboard']");

  public DashboardPage() {

    heading.shouldBe(visible);
  }
  public int getCardBalance(DataHelper.CardInfo cardInfo) {
    var text = cards.findBy(text(cardInfo.getCardNumber().substring(15))).getText();
    return extractBalance(text);
  }

  public int getFirstCardBalance(int index) {
    val text = cards.first().text();
    return extractBalance(text);
  }

  public int getSecondCardBalance(int index) {
    val text = cards.get(1).text();
    return extractBalance(text);
  }

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }
  public MoneyTransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
    cards.findBy(attribute("data-test-id",cardInfo.getId())).$("button").click();
    ;
    return new MoneyTransferPage();
  }
}