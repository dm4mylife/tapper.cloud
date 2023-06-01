package support_personal_account.analytics;

import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static data.Constants.WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Analytics.*;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.choseDateButtonSelector;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.*;
import static data.selectors.SupportPersonalAccount.Common.analyticsCategory;

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

        downloadTableButton.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED,withExtension("png"));


        //Assertions.assertNotNull(downloadTableButton.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED,withExtension("png")),
               // "Файл не может быть скачен");

    }

}
