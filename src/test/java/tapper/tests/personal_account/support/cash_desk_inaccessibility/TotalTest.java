package tapper.tests.personal_account.support.cash_desk_inaccessibility;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.cash_desk_inaccessibility.CashDeskInaccessibility;
import support_personal_account.lock.Lock;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.FileNotFoundException;

import static data.Constants.TestData.SupportPersonalAccount.*;


@Epic("Личный кабинет техподдержки")
@Feature("Недоступность кассы")
@DisplayName("Проверка категории Недоступность кассы")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    CashDeskInaccessibility cashDeskInaccessibility = new CashDeskInaccessibility();
    Lock lock = new Lock();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию недоступность кассы, проверка всех элементов")
    void goToCashDeskInaccessibilityCategory() {

        cashDeskInaccessibility.goToCashDeskInaccessibility();
        cashDeskInaccessibility.isCashDeskInaccessibilityCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем определенный ресторан")
    void choseOnlyCertainRestaurants() {

        cashDeskInaccessibility.choseOnlyCertainRestaurants(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(4)
    @DisplayName("Выбираем дату для выгрузки отчёта")
    void choseDateAndDownloadTable() {

        cashDeskInaccessibility.choseDate();

    }

    @Test
    @Order(5)
    @DisplayName("Скачиваем таблицы")
    void isDownloadCorrect() throws FileNotFoundException {

        cashDeskInaccessibility.downloadFile();

    }

    @Test
    @Order(6)
    @DisplayName("Выбираем все рестораны")
    void choseAllRestaurantToLockOption() {

        lock.choseAllRestaurantToLockOption();
        
    }

    @Test
    @Order(7)
    @DisplayName("Выбираем дату для выгрузки отчёта")
    void choseDateAndDownloadTableForAllRestaurants() {

        choseDateAndDownloadTable();

    }

    @Test
    @Order(8)
    @DisplayName("Скачиваем таблицы")
    void isDownloadCorrectForAllRestaurants() throws FileNotFoundException {

      cashDeskInaccessibility.downloadFile();

    }

}
