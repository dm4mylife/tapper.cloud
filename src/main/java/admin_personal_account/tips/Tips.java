package admin_personal_account.tips;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.RootPage;

import java.io.File;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.PASTA_IMG_PATH;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.Menu.*;
import static data.selectors.TapperTable.RootPage.DishList.orderMenuContainer;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

public class Tips extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в категорию чаевые")
    public void goToMenuCategory() {

        click(tipsCategory);
        pageHeading.shouldHave(text("Чаевые"));

    }

    @Step("Проверка что все элементы в чаевых корректны и отображаются")
    public void isTipsCategoryCorrect() {



    }


}
