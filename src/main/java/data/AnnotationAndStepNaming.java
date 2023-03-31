package data;

public class AnnotationAndStepNaming {

    public static class DisplayName {

        public static class TapperTable {

            public static final String createOrderInKeeper = " Создание заказа на кассе rkeeper.";
            public static final String isDishesCorrectInCashDeskAndTapperTable =
                    "  Проверка что позиции на кассе совпадают с позициями в таппере.";
            public static final String openEmptyTapperTable = " Открываем пустой стол.";
            public static final String isEmptyTableCorrect = " Проверяем что стол освободился.";
            public static final String checkEmptyTable = " Проверка заголовка, описания, кнопки обновить.";
            public static final String refreshPage = " Обновление страницы.";
            public static final String isStartScreenRestaurantLogoCorrect =
                    " Проверяем анимацию,картинку при загрузке стола.";
            public static final String isTableHasOrder = " Проверяем что стол не пустой и содержит заказ.";
            public static final String callWaiterAndSendMessage = " Отправка сообщения в вызов официанта.";
            public static final String closedOrder = " Закрываем заказ.";
            public static final String isDivideCheckButtonCorrect =
                    " Проверяем что кнопка разделить счёт есть и работает корректно.";
            public static final String isTipsContainerCorrect =
                    " Проверяем что все элементы в блоке чаевых отображаются корректно.";
            public static final String isCheckContainerCorrect =
                    " Проверяем что все элементы в блоке чека отображаются корректно.";
            public static final String isPaymentButtonCorrect = " Проверяем что кнопка оплатить есть и работает корректно.";
            public static final String isShareButtonCorrect =
                    " Проверяем что кнопка поделиться счётом есть и работает корректно.";
            public static final String isServiceChargeCorrect =
                    " Проверяем что чекбокс сервисного сбора есть и работает корректно.";
            public static final String isPrivateAndConfPolicyCorrect =
                    " Проверяем что чекбокс условиями пользования и политики конфиденциальности, есть и работает корректно.";
            public static final String isAppFooterCorrect =
                    " Проверяем что нижнее навигационное меню есть, корректно работает меню, переходы по табам.";
            public static final String isCallWaiterCorrect = " Проверяем функционал вызова официанта.";
            public static final String isPaymentOptionsCorrect = " Проверяем функционал выбора способа оплаты.";
            public static final String saveDataGoToAcquiringTypeDataAndPay = " Оплачиваем заказ.";
            public static final String checkPaymentProcess = " Проверяем оплату.";
            public static final String isReviewCorrect = " Оставляем отзыв.";
            public static final String isMenuCorrect = " Проверяем меню.";
            public static final String isChatHistorySavedCorrectAfterCloseBrowser =
                    " Проверка что история с вызовом официанта сохранились при закрытии браузера.";
            public static final String isTotalPaySumCorrectTipsSc = " Проверка суммы, чаевых, сервисного сбора.";
            public static final String isTotalPaySumCorrectTipsNoSc = " Проверка суммы, чаевых, без сервисного сбора.";
            public static final String isTotalPaySumCorrectNoTipsSc = " Проверка суммы, без чаевых, с сервисным сбором.";
            public static final String isTotalPaySumCorrectNoTipsNoSc = " Проверка суммы, без чаевых, без сервисного сбора.";
            public static final String resetTips = " Сброс чаевых";
            public static final String choseRandomDishesAncCheckSums = " Выбираем рандомно блюда, проверяем все суммы и условия";
            public static final String deactivateSc = " Отключение чекбокса сервисного сбора";
            public static final String setRandomTips = " Выбор рандомной опции чаевых (кроме 0)";
            public static final String savePaymentData =
                    " Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п.";

            public static final String goToAcquiringAndPayOrder =
                    " Переходим на эквайринг, вводим данные, оплачиваем заказ.";
            public static final String isPaymentCorrect =
                    " Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате.";
            public static final String isTelegramMessageCorrect = " Проверка сообщения в телеграмме.";
            public static final String isDefaultTipsLogicCorrect =
                    " Проверяем что логика чаевых по сумме корректна к минимальным чаевым.";
            public static final String addOneMoreDishToOrder = " Добавляем еще одно блюдо в заказ.";
            public static final String divideOrderToChoseDishes = " Разделяем счёт чтобы выбрать позиции.";
            public static final String isFirstTipsOptionCorrect = " Проверяем чаевые по умолчанию если менее 196.";
            public static final String isSecondTipsOptionCorrect = " Проверяем чаевые по умолчанию если от 196 до 245.";
            public static final String isThirdTTipsOptionCorrect = " Проверяем чаевые по умолчанию если от 246 до 326.";
            public static final String isFourthTipsOptionCorrect = " Проверяем чаевые по умолчанию если от 327 до 489.";
            public static final String isFifthTipsOptionCorrect = " Проверяем чаевые по умолчанию если более 490";
            public static final String setCustomTips = " Установка кастомных чаевых и проверка суммы";
            public static final String isDiscountCorrect = " Проверка скидки на столе ";
            public static final String setPositiveReview = " Оставляем позитивный отзыв";
            public static final String chooseAllDishesByCheckboxes = " Выбор всех позиций по чекбоксам";

        }

        public static class AuthorizationAndRegistrationAdminPage {


            public static final String authorizationPage = "Страница авторизации";
            public static final String restorePasswordPage = "Страница восстановления пароля";
            public static final String registrationPage = "Страница регистрации";


        }

        public static class AdminPersonalAccount {

            public static final String menu = "Меню";
            public static final String disabledMenu = "Отключенное меню на столе";
            public static final String profilePageMobile = "Главная страница с меню";
            public static final String profilePage = "Профиль";
            public static final String leftMenu = "Левое навигационное меню";
            public static final String integrations = "Интеграции";
            public static final String waiters = "Официанты";
            public static final String waitersSearch = "Поиск официанта";
            public static final String waitersSearchNegative = "Отрицательный поиск официанта";
            public static final String detailWaiterCard = "Детальная карточка официанта";
            public static final String customization = "Кастомизация";
            public static final String wiFiInformation = "Информация о Wi-Fi";
            public static final String reviews = "Отзывы на внешних сервисах";
            public static final String tablesAndQRCodes = "Столики и QR-коды";
            public static final String tablesAndQRCodesSearch = "Поиск по столам";
            public static final String tableDetailCard = "Детальная карточка стола";
            public static final String historyOperations = "История операции";
            public static final String historyOperationsWeekFilter = "История операции c недельным фильтром";
            public static final String historyOperationsMonthFilter = "История операции c месячным фильтром";
            public static final String requisites = "Реквизиты";
            public static final String configNotifications = "Настройка уведомлений";
        }

        public static class SupportPersonalAccount {

            public static final String searchRestaurant = "Поиск ресторана";
            public static final String logsAndPermissions = "Логи/доступы";

        }
    }


}
