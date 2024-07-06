package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id = 'city'] input").val(validUser.getCity());
        $("[data-test-id = 'date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id = 'date'] input").val(firstMeetingDate);
        $("[data-test-id = 'name'] input").val(validUser.getName());
        $("[data-test-id = 'phone'] input").val(validUser.getPhone());
        $("[data-test-id = 'agreement']").click();
        $("button.button").click();
        $("[data-test-id = 'success-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id = 'date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id = 'date'] input").val(secondMeetingDate);
        $("button.button").click();
        $("[data-test-id = 'replan-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Необходимо подтверждение"));
        $("[data-test-id = 'replan-notification'] .button").click();
        $("[data-test-id = 'success-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Встреча успешно запланирована на " + secondMeetingDate));

    }
}