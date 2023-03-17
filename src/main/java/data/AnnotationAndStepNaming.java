package data;

public class AnnotationAndStepNaming {

    public static class DisplayName {

        public static final String createOrderInKeeper = " Создание заказа на кассе rkeeper.";
        public static final String isDishesCorrectInCashDeskAndTapperTable =
                "  Проверка что позиции на кассе совпадают с позициями в таппере.";
        public static final String isTotalPaySumAndScAndTipsCorrect = " Проверка суммы, чаевых, сервисного сбора.";

        public static final String resetTips = " Сброс чаевых";
        public static final String deactivateSc = " Отключение чекбокса сервисного сбора";
        public static final String chooseAllDishesByCheckboxes = " Выбор всех позиций по чекбоксам";

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


    }



}
