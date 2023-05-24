package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.AdminAccount;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import waiter_personal_account.Waiter;

import static data.Constants.EXPIRED_REFRESH_TOKEN;
import static data.Constants.EXPIRED_USER_TOKEN;
import static data.Constants.TestData.AdminPersonalAccount.PERSONAL_ACCOUNT_PROFILE_STAGE_URL;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Проверка что refreshToken обновляется после истечения")
@DisplayName("Проверка что refreshToken обновляется после истечения")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RefreshTokenTest extends PersonalAccountTest {
    Waiter waiter = new Waiter();
    RootPage rootPage = new RootPage();

    AdminAccount adminAccount = new AdminAccount();

    @Test
    @Order(1)
    @DisplayName("Проверка что refreshToken обновляется после истечения")
    void isRefreshCorrect() {

        Selenide.open(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);
        System.out.println(1);
        rootPage.forceWait(10000);

        Selenide.localStorage().setItem("userTokenData",EXPIRED_USER_TOKEN);
        Selenide.localStorage().setItem("refreshTokenData",EXPIRED_REFRESH_TOKEN);

        rootPage.forceWait(10000);
        System.out.println(2);
        Selenide.open(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

        rootPage.forceWait(10000);
        System.out.println(3);
        String newRefreshToken = Selenide.localStorage().getItem("refreshTokenData");
        Assertions.assertNotEquals(EXPIRED_REFRESH_TOKEN,newRefreshToken,"Токен не изменился");

        waiter.isWaiterProfileCorrect();

    }

    @Test
    @Order(2)
    @DisplayName("Выход и учётки")
    void logOut() {

       adminAccount.logOut();

    }

}
