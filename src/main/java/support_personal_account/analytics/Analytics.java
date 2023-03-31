package support_personal_account.analytics;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import data.selectors.SupportPersonalAccount;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import support_personal_account.lock.Lock;

import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.Constants.WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Analytics.*;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.*;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.choseDateButtonSelector;
import static data.selectors.SupportPersonalAccount.Common.analyticsCategory;
import static data.selectors.SupportPersonalAccount.Common.cashDeskInaccessibilityCategory;
import static data.selectors.SupportPersonalAccount.Lock.dropdownWhereToLockRestaurants;

public class Analytics extends BaseActions {

    @Step("Переход в категорию Аналитики")
    public void goToAnalyticsCategory() {

        click(analyticsCategory);
        pageHeading.shouldHave(text("Аналитика"), Duration.ofSeconds(5));
        analyticsContainer.shouldBe(visible);

    }

    @Step("Проверка отображения всех элементов в категории Аналитика")
    public void isAnalyticsCategoryCorrect() {

        isElementVisible(analyticsContainer);
        isElementVisible(conversionStatisticsButton);
        isElementVisible(dateRangeContainer);
        isElementVisible(downloadTable);

    }

    @Step("Выбираем дату и проверяем что таблица начинает загружаться")
    public void choseDate() {

        clickByJs(choseDateButtonSelector);
        click(firstDateOfPeriod);
        click(firstDateOfPeriod);

    }

    @Step("Загружаем таблицу")
    public void downloadFile() throws FileNotFoundException {

        Assertions.assertNotNull(downloadTableButton.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED),
                "Файл не может быть скачен");

    }

}
