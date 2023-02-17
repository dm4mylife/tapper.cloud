package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attributeMatching;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.selectors.TapperTable.RootPage.Menu.menuDishPhotos;
import static data.selectors.TapperTable.RootPage.TapBar.appFooter;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(113)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Загрузка, удаление фотографии")
@DisplayName("Загрузка, удаление фотографии")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_3_uploadAndDeleteImageDishTest extends BaseTest {

    static int categoryIndex;
    static int dishIndex;
    static String imageUrl;

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу меню")
    public void goToMenu() {
        menu.goToMenuCategory();
    }

    @Test
    @DisplayName("1.3. Активируем и делаем видимым рандомную категорию блюда")
    public void activateRandomCategory() {

        categoryIndex = menu.getRandomActiveCategoryIndex();
        menu.makeVisibleMenuCategory(categoryIndex);
        baseActions.forceWait(1000);

    }

    @Test
    @DisplayName("1.4. Активируем и делаем видимым рандомное блюдо")
    public void activateRandomDish() {

        dishIndex = menu.getRandomActiveDishIndex();
        menu.makeVisibleMenuDish(dishIndex);


    }

    @Test
    @DisplayName("1.5. Загружаем фотографию в блюдо")
    public void uploadDishImage() {

        imageUrl = menu.uploadImageFile(dishIndex);
        System.out.println(imageUrl + " фотография в админке");

    }

    @Test
    @DisplayName("1.6. Открываем в новой вкладке стол")
    public void openTapperTable() {

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_333);
        Selenide.switchTo().window(1);
        baseActions.isElementVisibleDuringLongTime(appFooter,20);
        baseActions.forceWait(1000);
        baseActions.click(appFooterMenuIcon);

    }

    @Test
    @DisplayName("1.7. Проверяем что фотография есть на столе")
    public void isImageCorrect() {

        menuDishPhotos
                .filter(attributeMatching("src",".*" + imageUrl + ".*"))
                .shouldBe(size(1).because("На столе присутствуют битые фотографии"));

    }

    @Test
    @DisplayName("1.8. Удаляем изображение")
    public void deleteImage() {

        Selenide.switchTo().window(0);
        menu.deleteDishImage(dishIndex);

    }

    @Test
    @DisplayName("1.9. Проверяем что на столе удалилось изображение")
    public void isImageDeletedOnTable() {

        Selenide.switchTo().window(1);
        rootPage.refreshPage();

        baseActions.isElementVisibleDuringLongTime(appFooter, 20);
        baseActions.click(appFooterMenuIcon);

        menuDishPhotos
                .filter(attributeMatching("src",".*" + imageUrl + ".*"))
                .shouldBe(size(0));

    }

}
