package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldReplenishedFirstCard() {
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 10_000;

        var transfer = dashboardPage.firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart + amount;
        var secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldReplenishedSecondCard() {
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 10_000;
        var transfer = dashboardPage.secondCardButton();
        transfer.transferFromCardToCard(amount, getFirstCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart - amount;
        var secondCardBalanceResult = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldNotTransferMoneyIfAmountMoreBalance() {
        int amount = 20_000;

        var transfer = dashboardPage.firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        transfer.getErrorLimit();
    }
}