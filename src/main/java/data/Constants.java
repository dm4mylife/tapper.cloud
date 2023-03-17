package data;

import common.BaseActions;

import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;

public class Constants {
    public static final Integer WAIT_FOR_FULL_LOAD_PAGE = 1500;

    public static final Integer WAIT_BETWEEN_SET_DISHES_CHECKBOXES = 700;
    public static final Integer WAIT_UNTIL_TRANSACTION_EXPIRED = 300000;
    public static final Integer WAIT_UNTIL_TRANSACTION_STILL_ALIVE = 200000;
    public static final int WAIT_FOR_FILE_TO_BE_DOWNLOADED = 10000;
    public static final int WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK = 10000;
    public static final int WAIT_MENU_FOR_FULL_LOAD = 2000;
    public static final Double SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED = 6.5;
    public static final int WAIT_FOR_PREPAYMENT_ON_CASH_DESK = 7000;
    public static final int WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE = 400;

    public static final int WAIT_FOR_IMAGE_IS_FULL_LOAD_ON_CONTAINER = 2000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_REVIEW = 10000;
    public static final int WAIT_FOR_SOCKETS_CHANGE_POSITION = 2000;
    public static final int WAIT_FOR_SOCKETS_RECEIVED_REQUEST = 4000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_CALL_WAITER = 5000;
    public static final int WAIT_FOR_TELEGRAM_SUPPORT_SENDING = 30000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY = 8000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY = 10000;
    public static final int ATTEMPT_FOR_PREPAYMENT_REQUEST = 3;
    public static final String PASTA_IMG_PATH = "src/main/resources/pasta.jpg";
    public static final String LOADER_GIF_PATH = "src/main/resources/loader.gif";
    public static final String OLD_LOADER_GIF_PATH = "src/main/resources/oldLoader.gif";
    public static final String ROBOCOP_IMG_PATH = "src/main/resources/robocop.jpeg";
    public static final String downloadFolderPath = "C:\\tapper.cloud\\build\\downloads\\qr";
    public static final String downloadFolderPathAdminSupport = "C:\\tapper.cloud\\build\\downloads\\qrAdminSupport";

    public static final String FORM_MUST_BE_FIELD_ERROR_TEXT = "Для сохранения необходимо заполнить поле";
    public static final String TYPE_CORRECT_LINK_ERROR_TEXT = "Введите корректную ссылку";

    public static class TestData {

        public static class TapperTable {

            public static final String STAGE_RKEEPER_TABLE_111 = "https://stage-ssr.zedform.ru/testrkeeper/1000046";
            public static final String STAGE_RKEEPER_TABLE_222 = "https://stage-ssr.zedform.ru/testrkeeper/1000397";
            public static final String STAGE_RKEEPER_TABLE_10 = "https://stage-ssr.zedform.ru/testrkeeper/1000044";
            public static final String STAGE_RKEEPER_TABLE_333 = "https://stage-ssr.zedform.ru/testrkeeper/1000398";
            public static final String STAGE_RKEEPER_TABLE_444 = "https://stage-ssr.zedform.ru/testrkeeper/1000423";
            public static final String STAGE_RKEEPER_TABLE_555 = "https://stage-ssr.zedform.ru/testrkeeper/1000437";
            public static final String STAGE_IIKO_TABLE_3 = "https://stage-ssr.zedform.ru/office/3";
            public static final String AUTO_API_URI = "https://apitapper.zedform.ru/api/";
            public static final String TEST_API_URI = "https://taper.zedform.ru/api/";


          /* public static final String STAGE_RKEEPER_TABLE_111 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000046";
            public static final String STAGE_RKEEPER_TABLE_222 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000397";
            public static final String STAGE_RKEEPER_TABLE_333 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000398";
            public static final String STAGE_RKEEPER_TABLE_444 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000423";
            public static final String STAGE_IIKO_TABLE_3 = "https://auto-ssr-tapper.zedform.ru/office/3";
            public static final String AUTO_API_URI = "https://auto-back-tapper.zedform.ru/api/";*/


            public static final Integer PAYMENT_BANKS_MAX_PRIORITY_BANKS = 3;
            public static final double SERVICE_CHARGE_MAX = 199.0;
            public static final String TEST_WAITER_COMMENT = "test\\тест";
            public static final String TEST_REVIEW_COMMENT = "test\\тест";
            public static final String TEST_REVIEW_COMMENT_NEGATIVE = "Вкусно, но не то чтобы по вкусу вкусно";
            public static final String TEST_REVIEW_COMMENT_POSITIVE= "Ни о чём не жалею, очень вкусно!";
            public static final String TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL = "Это сообщение отправлено всем";
            public static final String TEST_COMMENT_IN_SUPPORT_SENDING_TO_WAITERS = "Это сообщение отправлено официантам";
            public static final String TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS = "Это сообщение отправлено админам";
            public static final String UNKNOWN_WAITER = "Неизвестный официант";

            public static final String PAYMENT_ERROR_ORDER_EXPIRED = "Order expired";
            public static final String PAYMENT_ERROR_TEXT = "Оплата не прошла";

            public static final String SUM_CHANGED_ALERT_TEXT =
                    "Сумма оплаты изменилась, так как другой гость выбрал позицию, которую вы хотели оплатить.";

            public static final String DISH_STATUS_IS_PAYING = "Оплачивается";
            public static final String DISH_STATUS_PAYED = "Оплачено";

            public static final String TERM_OF_USE_HEADING_CONTENT =
                    "УСЛОВИЯ ИСПОЛЬЗОВАНИЯ И ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ TAPPER";
            public static final String CONF_LINK_HEADING_CONTENT =
                    "Политика конфиденциальности и обработки персональных данных";

            public static final String PAYMENT_BUTTON_DISABLED_PAYMENT = "Оплатить 0";

            public static final String NON_EXISTING_EMAIL = "test1test2@mail.ru";
            public static final String NON_EXISTING_PASSWORD = "1!2#asdf";
            public static final String SHORT_PASSWORD = "1234";
            public static final String EMPTY_LOGIN = " ";
            public static final String WRONG_EMAIL_PATTERN = "test";
            public static final Double SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM = 3.0;
            public static final Double SERVICE_CHARGE_PERCENT_FROM_TIPS = 5.0;
            public static final String TIPS_ERROR_MSG = " Минимальная сумма чаевых 49 ₽ ";
            public static final String MIN_SUM_FOR_TIPS_ERROR = "48";
            public static final String MIN_SUM_TIPS_ = "49";
            public static final String COOKIE_GUEST_FIRST_USER = "123456";
            public static final String COOKIE_SESSION_FIRST_USER = "1234";
            public static final String COOKIE_GUEST_SECOND_USER = "123457";
            public static final String COOKIE_SESSION_SECOND_USER = "1235";
        }
        public static class Best2Pay {
            public static final String TEST_BEST2PAY_URL = "https://test.best2pay.net/";
            public static final String BEST2PAY_NAME = "best2pay";
            public static final String TEST_PAYMENT_CARD_NUMBER = "4809388886227309";
            public static final String TEST_PAYMENT_CARD_EXPIRE_MONTH = "12";
            public static final String TEST_PAYMENT_CARD_EXPIRE_YEAR = "24";
            public static final String TEST_PAYMENT_CARD_CVV = "123";

        }
        public static class Yandex {
            public static final String TEST_YANDEX_LOGIN_EMAIL = "autotests@tapper.cloud";
            public static final String TEST_YANDEX_PASSWORD_MAIL = "V8JRPGwr";
            public static final String TEST_YANDEX2_LOGIN_EMAIL = "autotests_waiter@tapper.cloud";
            public static final String TEST_YANDEX2_PASSWORD_MAIL = "W1LrKR29xwp9";
            public static final String YANDEX_MAIL_URL =
                "https://passport.yandex.ru/auth?retpath=https%3A%2F%2Fmail.yandex.ru%2F&backpath=https%3A%2F%2Fmail.yandex.ru%2F%3Fnoretpath%3D1&from=mail&origin=hostroot_homer_auth_ru";
        }
        public static class AdminPersonalAccount {

            public static final String TELEGRAM_TOKEN = "5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI";
            public static final String TEST_WIFI_NETWORK_NAME = "auto_wifi";
            public static final String TEST_WIFI_NETWORK_PASSWORD = "12345678";
            public static final String TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT = "test\\тест";
            public static final String ROBOCOP_WAITER = "Robocop";
            public static final String TERMINATOR_WAITER = "Terminator";
            public static final String IRONMAN_WAITER = "Iron Man";
            public static final String OPTIMUS_PRIME_WAITER = "Optimus Prime";
            public static final String IRONHIDE_WAITER = "IronHide";
            public static final String ROBOCOP_WAITER_ID = "23";
            public static final String TERMINATOR_WAITER_ID = "233";
            public static final String IRONMAN_WAITER_ID = "2333";
            public static final String OPTIMUS_PRIME_WAITER_ID = "23333";
            public static final String IRONHIDE_WAITER_ID = "233333";
            public static final String ADMIN_TEST_PHONE = "+7(123) 456-78-90";
            public static final String ADMIN_AUTHORIZATION_STAGE_URL = "https://tapper.staging.zedform.ru/users";
            public static final String ADMIN_PROFILE_STAGE_URL = "https://tapper.staging.zedform.ru/profile";
            public static final String ROOT_TAPPER_STAGE_URL = "https://tapper.staging.zedform.ru/";
            public static final String ADMIN_REGISTRATION_STAGE_URL = "https://tapper.staging.zedform.ru/users/registration";
            public static final String ADMIN_RESTAURANT_LOGIN_EMAIL = "kirillk8888@yandex.ru";
            public static final String ADMIN_RESTAURANT_PASSWORD = "777777";
            public static final String ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String WAITER_LOGIN_EMAIL = "jagexo5827@khaxan.com";
            public static final String WAITER_PASSWORD = "123456";
            public static final String WAITER_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String ROBOCOP_WAITER_CHANGED_NAME = "Робокопище";
            public static final String TELEGRAM_AUTO_CHANNEL_LOGIN = "-1001764474981";
            public static final String TELEGRAM_AUTO_LOGIN = "user_unknown_nb";
            public static final String VERIFIED_WAITER_TEXT = "Официант верифицирован";
            public static final String IS_WAITING_WAITER_TEXT = "Ожидает приглашения";
            public static final String INVITED_IN_SERVICE_TEXT = "Приглаш(е|ё)н в систему";

            public static final String YANDEX_REVIEW_LINK =
                    "https://yandex.ru/maps/org/nalu_baloo/48679020937/reviews/?add-review=true&ll=37.559181%2C55.712556&z=13";
            public static final String TWOGIS_REVIEW_LINK =
                    "https://2gis.ru/moscow/firm/70000001060393218/tab/reviews?writeReview&m=37.58076%2C55.748187%2F16";
            public static final String GOOGLE_REVIEW_LINK =
                    "https://www.google.com/search?sxsrf=AJOqlzWmZL35-IjT_iu4nwWZOnSHikFmJw:1678769978813&q=%D0%BD%D0%B0%D0%BB%D1%83+%D0%B1%D0%B0%D0%BB%D1%83&spell=1&sa=X&ved=2ahUKEwi2pvzB0dr9AhXQX_EDHdFKB6wQBSgAegQICBAB&biw=1440&bih=680&dpr=2#lrd=0x46b54b4bbf99384f:0xfe6ee71d2bb7464a,3";


            public static final String OVER_LIMIT_CHARS_NAME_BY_GUEST_INPUT =
                    "В этом поле будет содержаться более шестидесяти одного символа, ага";
            public static final String LIMIT_CHARS_NAME_BY_GUEST_INPUT =
                    "В этом поле будет содержаться более шестидесяти одного символ";

            public static final String LIMIT_CHARS_NAME_BY_GUEST_COUNTER = "61 / 61";

            public static final String OVER_LIMIT_CHARS_DESCRIPTIONS_INPUT =
                    "Рост намеченных и разработке играет от формировании порядка, позиций, порядка, " +
                            "также оценить занимаемых соответствующий реализация условий. Рост и структура организации" +
                            " заданий активности образом также намеченных идейные анализа от сфера условий задач. " +
                            "Роль нашей и форм намеченных показывает, в наше время";
            public static final String LIMIT_CHARS_DESCRIPTIONS_INPUT =
                    "Рост намеченных и разработке играет от формировании порядка, позиций, порядка, также оценить" +
                            " занимаемых соответствующий реализация условий. Рост и структура организации заданий" +
                            " активности образом также намеченных идейные анализа от сфера условий задач. Роль нашей" +
                            " и форм намеченных показывает, в наше";

            public static final String LIMIT_CHARS_DESCRIPTIONS_COUNTER = "300 / 300";

            public static final String OVER_LIMIT_CHARS_INGREDIENTS_INPUT = "Играет особенности важную важную роль" +
                    " идейные отношении же оценить количественный важные и позволяет кадров формировании порядка," +
                    " постоянный финансовых модель эксперимент реализации также задач. А равным идейные постоянный" +
                    " нас формировании проверки прогрессии";
            public static final String LIMIT_CHARS_INGREDIENTS_INPUT = "Играет особенности важную важную роль идейные" +
                    " отношении же оценить количественный важные и позволяет кадров формировании порядка, постоянный" +
                    " финансовых модель эксперимент реализации также задач. А равным идейные постоянный нас формировании" +
                    " проверки прогре";

            public static final String LIMIT_CHARS_INGREDIENTS_COUNTER = "255 / 255";

            public static final String OVER_LIMIT_CHARS_AMOUNT_INPUT = "12345";
            public static final String LIMIT_CHARS_AMOUNT_INPUT = "1234";

            public static final String LIMIT_CHARS_AMOUNT_COUNTER = "4 / 4";


            public static final String OVER_LIMIT_CHARS_CALORIES_INPUT = "12345";



            public static final String LIMIT_CHARS_CALORIES_INPUT = "1234";

            public static final String LIMIT_CHARS_CALORIES_COUNTER = "4 / 4";


        }
        public static class SupportPersonalAccount {
            public static final String RESTAURANT_NAME = "testrkeeper";
            public static final String SUPPORT_LOGIN_EMAIL = "varlone_mag2@mail.ru";
            public static final String SUPPORT_PASSWORD = "111111";

        }
        public static class RegistrationData {

            public static final String NAME = "Робокоп";
            public static final String TELEPHONE_NUMBER = "9068723655";
            public static final String EMAIL = TEST_YANDEX_LOGIN_EMAIL;
            public static final String PASSWORD = "250491";
            public static final String CONFIRMATION_PASSWORD = "250491";
            public static final String RESTAURANT_NAME = "show me your love";
        }

    }
    public static class RegexPattern {

        public static class TelegramMessage {
            public static final String tableRegex = "(\\n|.)+Стол: ([\\d+]+)(\\n|.)*";
            public static final String tableRegexTelegramMessage = "(.*Стол\\s)(\\d{3})(.*)";
            public static final String sumInCheckRegex = "(\\n|.)*Сумма в чеке: (\\d*\\.?\\d*)(\\n|.)*";
            public static final String restToPayRegex = "(\\n|.)*Осталось оплатить: (\\d*\\.?\\d*)(\\n|.)*";
            public static final String tipsRegex = "(\\n|.)*Чаевые: (\\d*\\.?\\d*)(\\n|.)*";
            public static final String paySumRegex = "(\\n|.)*Сумма оплаты: (\\d*\\.?\\d*)(\\n|.)*";
            public static final String totalPaidRegex = "(\\n|.)*Всего оплачено: (\\d*\\.?\\d*)(\\n|.)*";
            public static final String markUpRegex = "(\\n|.)*Наценка: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
            public static final String discountRegex = "(\\n|.)*Скидка: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
            public static final String payStatusRegex = "(\\n|.)*Статус оплаты: ([а-яА-Я\\s]+)\\nСтатус заказа(\\n|.)*";
            public static final String orderStatusRegex =
                    "(\\n|.)*Статус заказа: ([а-яaА-Яa-zA-Z\\.\\:\\,\\s]+)\\n?Дата заказа(\\n|.)*";
            public static final String reasonError = "(\\n|.)*Причина: (.+)(\\n|.)*";
            public static final String dateOrderRegex = "(\\n|.)*Дата заказа: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
            public static final String waiterRegex = "(\\n|.)*Официант: ([а-яА-Яaa-zA-Z\\s]+)\\n(\\n|.)*";
            public static final String ratingCommentRegex = "(\\n|.)*Комментарий: ((\\s|.)*)Рейтинг(\\n|.)*";
            public static final String ratingRegex = "(\\n|.)*Рейтинг: ((\\d|\\s)*)Пожелания(\\n|.)*";
            public static final String suggestionRegex = "(\\n|.)*Пожелания: (.*)";
            public static final String tableReviewRegex = "(\\n|.)*Номер столика: ([\\d+]+)(\\n|.)*";
            public static final String callWaiterCommentRegex = "(\\n|.)*Комментарий: (.*)\\n?Время.*";
            public static final String restaurantNameRegex = "Ресторан #\\d+ \\((.+)\\)(\\n|.)*";

        }

        public static class TapperTable {

            public static final String totalPayRegex = "\\s₽";
            public static final String totalSumInWalletRegex = "\\s₽";
            public static final String dishPriceRegex = "\\s₽";
            public static final String tipsInCheckSumRegex = "\\s₽";
            public static final String markedDishesRegex = "\\s₽";
            public static final String discountInCheckRegex = "[^\\d\\.]+";
            public static final String anotherGuestSumInCheckRegex = "[^\\d\\.]+";
            public static final String serviceChargeRegex = "[^\\d\\.\\-]+";

            public static final String percentRegex = "\\D+";



        }


    }

}
