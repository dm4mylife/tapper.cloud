package tapper.tests.admin_personal_account.auth_and_registration;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import waiter_personal_account.Waiter;


import static data.Constants.*;
import static data.Constants.TestData.AdminPersonalAccount.PERSONAL_ACCOUNT_PROFILE_STAGE_URL;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация")
@DisplayName("Проверка что refreshToken обновляется после истечения")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RefreshTokenTest extends PersonalAccountTest {
    Waiter waiter = new Waiter();

    @Test
    @Order(1)
    @DisplayName("Проверка что refreshToken обновляется после истечения")
    void isRefreshCorrect() {

        Selenide.open(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

        Selenide.localStorage().setItem("userTokenData",EXPIRED_USER_TOKEN);
        Selenide.localStorage().setItem("refreshTokenData",EXPIRED_REFRESH_TOKEN);

        Selenide.open(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);
        waiter.isWaiterProfileCorrect();

        String newRefreshToken = Selenide.localStorage().getItem("refreshTokenData");

        Assertions.assertNotEquals(EXPIRED_REFRESH_TOKEN,newRefreshToken);

    }

}
