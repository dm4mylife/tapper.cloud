package tapper.tests.admin_personal_account.company_requisites;

import admin_personal_account.company_requisites.CompanyRequisites;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;

@Order(160)
@Epic("Личный кабинет администратора ресторана")
@Feature("Реквизиты компании")
@DisplayName("Проверка что все элементы отображаются корректно")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _16_0_TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    CompanyRequisites companyRequisites = new CompanyRequisites();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу реквизитов компании, проверка всех элементов")
    public void goToCompanyRequisitesCategory() {

        companyRequisites.goToCompanyRequisitesCategory();
        companyRequisites.isCompanyRequisitesCategoryCorrect();



    }

}
