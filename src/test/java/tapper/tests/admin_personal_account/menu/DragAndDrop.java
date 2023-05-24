package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static com.codeborne.selenide.Selenide.$;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Disabled
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка drag and drop")
@DisplayName("Проверка drag and drop")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DragAndDrop extends PersonalAccountTest {

    int adminTab = 0;
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        menu.goToMenuCategory();
        menu.isMenuCategoryCorrect();

        rootPage.forceWait(3000);


        SelenideElement a = $(".vAdminMenuAside__category:nth-child(1)");
        WebElement b = WebDriverRunner.getWebDriver().findElement(By.cssSelector(".vAdminMenuAside__category:nth-child(3)"));

        /*int x = a.getLocation().x - 125;
        int y = a.getLocation().y;

        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        actions.moveToElement(a)
                .pause(Duration.ofSeconds(1))
                .clickAndHold(a)
                .pause(Duration.ofSeconds(1))
                .moveToElement(b,x,y)
                .pause(Duration.ofSeconds(1))
                .release().build().perform();*/

        a.dragAndDropTo(".vAdminMenuAside__category:nth-child(4)", DragAndDropOptions.usingActions());


        rootPage.forceWait(3000);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу меню")
    void goToMenu() {



    }


}
