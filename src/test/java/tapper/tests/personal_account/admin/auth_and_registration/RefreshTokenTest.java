package tapper.tests.personal_account.admin.auth_and_registration;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.EXPIRED_REFRESH_TOKEN;
import static data.Constants.EXPIRED_USER_TOKEN;
import static data.Constants.TestData.AdminPersonalAccount.PERSONAL_ACCOUNT_PROFILE_STAGE_URL;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Проверка что refreshToken обновляется после истечения")
@DisplayName("Проверка что refreshToken обновляется после истечения")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RefreshTokenTest extends PersonalAccountTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    @Test
    @Order(1)
    @DisplayName("Проверка что refreshToken обновляется после истечения")
    void isRefreshCorrect() {

        Selenide.open(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

        Selenide.localStorage().setItem("userTokenData",EXPIRED_USER_TOKEN);
        Selenide.localStorage().setItem("refreshTokenData",EXPIRED_REFRESH_TOKEN);

        rootPage.openPage(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);
        authorizationPage.isFormContainerCorrect();

    }

}
