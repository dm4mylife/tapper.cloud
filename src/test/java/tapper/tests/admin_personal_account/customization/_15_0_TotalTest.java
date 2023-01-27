package tapper.tests.admin_personal_account.customization;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.customization.Customization;
import tapper_table.RootPage;
import tests.BaseTest;

import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.TestData.AdminPersonalAccount.TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;

@Order(150)
@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка переключения типу получателя, замена шаблона, проверка на столе")
@DisplayName("Проверка переключения типу получателя, замена шаблона, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _15_0_TotalTest extends BaseTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();


    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу кастомизации, проверка всех элементов")
    public void goToMenu() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }


    @Test
    @DisplayName("1.3. Проверяем что смена получателя в админке корректная")
    public void isChangingRecipientsCorrect() {

        customization.choseOnlyManager();
        customization.choseWaiterAndManager();


    }

    @Test
    @DisplayName("1.4. Изменение шаблона отправки текста в админке")
    public void setMsgAsTextPattern() {

        customization.setMsgAsTextPattern();

    }

    @Test
    @DisplayName("1.5. Проверка что шаблон корректен на столе")
    public void isChangedTextPatternCorrectOnTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_111);

        customization.isChangedTextPatternCorrectOnTable(TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT);

    }

}
