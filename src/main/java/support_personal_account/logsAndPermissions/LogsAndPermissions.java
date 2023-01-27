package support_personal_account.logsAndPermissions;


import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.logsContainer;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.pagePreloader;


public class LogsAndPermissions extends BaseActions {


    @Step("Переход в меню профиля")
    public void goToLogsAndPermissionsCategory() {

        click(logsAndPermissionsCategory);
        pageHeading.shouldHave(text("Логи/доступы"), Duration.ofSeconds(5));
        logsContainer.shouldBe(visible);
        isLogsAndPermissionsCategoryCorrect();

    }

    public void chooseRestaurant() {

        click(expandLeftMenuButton);
        isElementVisible(openedLeftMenuContainer);
        click(logsAndPermissionsCategoryDropdownButton);
        forceWait(2000);
        searchRestaurantInput.sendKeys("testrkeeper");
        forceWait(2000);
        searchResultList.first().click();

        pagePreloader.shouldNotHave(attributeMatching("style","background: transparent;")
                ,Duration.ofSeconds(10));

        click(collapseLeftMenuButton);
        isElementInvisible(openedLeftMenuContainer);
        forceWait(5000);

    }

    @Step("Проверка что все элементы в Логи/доступы корректны")
    public void isLogsAndPermissionsCategoryCorrect() {



    }


}
