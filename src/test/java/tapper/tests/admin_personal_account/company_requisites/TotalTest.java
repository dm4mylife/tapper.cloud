package tapper.tests.admin_personal_account.company_requisites;

import admin_personal_account.company_requisites.CompanyRequisites;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Личный кабинет администратора ресторана")
@Feature("Реквизиты компании")
@DisplayName("Проверка что все элементы отображаются корректно")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    CompanyRequisites companyRequisites = new CompanyRequisites();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу реквизитов компании, проверка всех элементов")
    void goToCompanyRequisitesCategory() {

        companyRequisites.goToCompanyRequisitesCategory();
        companyRequisites.isCompanyRequisitesCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Проверка что после обновления страницы мы остались на этом же табе")
    void isCorrectAfterRefresh() {

        companyRequisites.isCorrectAfterRefresh();

    }

}
