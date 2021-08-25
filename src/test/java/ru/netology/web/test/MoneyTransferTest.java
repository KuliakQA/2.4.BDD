package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.page.DashboardPage.firstCardButton;
import static ru.netology.web.page.DashboardPage.secondCardButton;

class MoneyTransferTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldReplenishedFirstCard() {
        var dashboardPage = new DashboardPage();
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 10_000;

        var transfer = firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart + amount;
        var secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldReplenishedSecondCard() {
        var dashboardPage = new DashboardPage();
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 10_000;

        var transfer = secondCardButton();
        transfer.transferFromCardToCard(amount, getFirstCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart - amount;
        var secondCardBalanceResult = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldNotTransferMoneyIfAmountMoreBalance() {
        var dashboardPage = new DashboardPage();
        int amount = 20_000;

        var transfer = firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        transfer.getErrorLimit();
    }
}