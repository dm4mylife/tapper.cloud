package tapper.tests.e2e;


import api.ApiRKeeper;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import pages.*;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;
import tests.BaseTest;


@Epic("E2E - тесты (полные)")


@Feature("keeper - 1 поз без скидки - чай+сбор - карта - отзыв")
@DisplayName("E2E - тесты (полные)")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class BasicTest extends BaseTest {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();



    @Test
    @Order(1)
    @Step("Создание заказа в r_keeper")
    @DisplayName("Создание заказа в r_keeper")
    @Description("Создаём заказ с одной позицией и без скидки")
    public void createAndFillOrder() {

        apiRKeeper.fillingOrder(apiRKeeper.createOrder());

    }

    @Test
    @Order(2)
    @Step("Переходим на стол tapper")
    @DisplayName("Переходим на стол tapper")
    @Description("Переходим по ссылке стола")
    public void openTapperLink() {

        rootPage.openTapperLink();
    }

    @Test
    @Order(3)
    @Step("Отображение логотипа\\анимации")
    @DisplayName("Отображение логотипа\\анимации")
    @Description("Анимация и лого устанавливаются в соответствии с админкой ресторана")
    public void isStartScreenShown() {

        rootPage.isStartScreenShown();

    }

    @Test
    @Order(4)
    @Step("Отображение списка позиций заказ")
    @DisplayName("Отображение списка позиций заказ")
    @Description("Список не пустой и отображается на странице. Проверяем корректны ли расчёты по суммам")
    public void menuNestedTests() {

        rootPage.isDishListVisible();
        rootPage.isPriceInWalletCorrectWithTotalPay();
        double totalSum = rootPage.countAllPricesInMenu();
        rootPage.checkIfTotalSumInDishesMatchWithTotalPay(totalSum);
        rootPage.checkIfTipsPercentCorrectWithTotalSumWithoutServiceCharge(totalSum);
        rootPage.checkIfTipsPercentCorrectWithTotalSumWithServiceCharge(totalSum);


    }


    @Test
    @Order(5)
    @Step("Отображение разделить счёт")
    @DisplayName("Отображение кнопки разделить счёт")
    @Description("В том случает если к меню нет позиций со скидками")
    public void isDivideCheckSliderWith1Dish() {

        rootPage.isDivideCheckSliderWith1Dish();

    }

    @Test
    @Order(6)
    @Step("Отображение кнопки 'Оплатить'' и 'Поделиться счётом'")
    @DisplayName("Отображение кнопки 'Оплатить'' и 'Поделиться счётом'")
    @Description("Кнопка Оплатить активна и синего цвета. Обе кнопки кликабельны")
    public void isPaymentAndShareBlockCorrect() {

        rootPage.isPaymentButtonShown();
        rootPage.isShareButtonShown();
    }

    @Test
    @Order(7)
    @Step("Клик по кнопке 'Оплатить'")
    @DisplayName("Клик по кнопке 'Оплатить'")
    @Description("Кнопка Оплатить активна и синего цвета. Обе кнопки кликабельны")
    public void clickOnPaymentButton() {

        rootPage.clickOnPaymentButton();
    }

    @Test
    @Order(8)
    @Step("Отображение прелоадера страницы")
    @DisplayName("Отображение прелоадера страницы")
    @Description("Экран слегка размывается, экран становится не активным, появится прелоадер загрузки эквайринга")
    public void isPageLoaderShown() {

        rootPage.isPageLoaderShown();
    }

    @Test
    @Order(9)
    @Step("Переход на тестовый best2pay")
    @DisplayName("Проверям что нас перекинуло на тестовую страницу эквайринга и форма оплаты присутствует")
    @Description("Корректный редирект на страницу эквариайнга")
    public void isTest() {

        best2PayPage.isPaymentContainerAndVpnShown();
        best2PayPage.isTestBest2PayUrl();
    }

    @Test
    @Order(10)
    @Step("Вводим тестовые данные для оплаты")
    @DisplayName("Вводим тестовые данные для оплаты")
    @Description("Вводим номер карты, дату, cvv, email")
    public void typeAllCreditCardData() {

        best2PayPage.checkPayMethodsAndTypeAllCreditCardData();
    }

    @Test
    @Order(11)
    @Step("Нажимаем на кнопку оплаты")
    @DisplayName("Нажимаем на кнопку оплаты")
    public void clickPayButton() {

        best2PayPage.clickPayButton();
    }

    @Test
    @Order(12)
    @Step("Отображение логотипа\\анимации")
    @DisplayName("Отображение логотипа\\анимации")
    @Description("Анимация и лого устанавливаются в соответствии с админкой ресторана")
    public void isStartScreenShownAfterPayment() {

        rootPage.isStartScreenShown();

    }

    @Test
    @Order(13)
    @Step("Отображение процесса оплаты")
    @DisplayName("Отображение процесса оплаты")
    @Description("Отображение двух контейнеров с 'Ожидается оплата' и 'Оплата успешно завершена!'")
    public void isPaymentProcessContainerShown() {

        reviewPage.isPaymentProcessContainerShown();

    }

    @Test
    @Order(14)
    @Step("Отображение блока отзыва")
    @DisplayName("Отображение блока отзыва")
    @Description("Присутствует статус оплаты, рейтинг звездами, поле с комментарием, кнопка отправить")
    public void isReviewBlockCorrect() {

        reviewPage.isReviewBlockCorrect();

    }

    @Test
    @Order(15)
    @Step("Ставим 5 звездочек")
    @DisplayName("Ставим 5 звездочек")
    @Description("После простановки звезда активная, другие нет и появляется выбор пожелания с иконками")
    public void rate5Stars() {

        reviewPage.rate5Stars();

    }

    @Test
    @Order(16)
    @Step("Выбираем рандмоное пожелание")
    @DisplayName("Выбираем рандмоное пожелание")
    @Description("Генерируем рандомное число, кликаем, становится активным иконка")
    public void chooseRandomWhatDoULike() {

        reviewPage.chooseRandomWhatDoULike();

    }

    @Test
    @Order(17)
    @Step("Оставляем комментарий")
    @DisplayName("Оставляем комментарий")
    @Description("В вводим значение, поле активно, сохраняет результат")
    public void typeReviewComment() {

        reviewPage.typeReviewComment();

    }

    @Test
    @Order(18)
    @Step("Жмём на кнопку завершить")
    @DisplayName("Жмём на кнопку завершить")
    public void clickOnFinishButton() {

        reviewPage.clickOnFinishButton();

    }

    @Test
    @Order(19)
    @Step("Отображение в ожидании заказа на столе")
    @DisplayName("Отображение в ожидании заказа на столе")
    public void isEmptyOrder() {

        rootPage.isEmptyOrder();

    }


}
