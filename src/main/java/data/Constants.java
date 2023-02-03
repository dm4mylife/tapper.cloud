package data;

public class Constants {
    public static final Integer TIME_WAIT_FOR_FULL_LOAD = 1500;
    public static final int TIME_WAIT_FOR_FILE_TO_BE_DOWNLOADED = 10000;
    public static final Double SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED = 6.5;
    public static final int WAIT_FOR_PREPAYMENT_ON_CASH_DESK = 7000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_REVIEW = 10000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY = 10000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY = 16000;
    public static final int ATTEMPT_FOR_PREPAYMENT_REQUEST = 3;
    public static final String PASTA_IMG_PATH = "src/main/resources/pasta.jpg";
    public static final String LOADER_GIF_PATH = "src/main/resources/loader.gif";
    public static final String OLD_LOADER_GIF_PATH = "src/main/resources/oldLoader.gif";
    public static final String ROBOCOP_IMG_PATH = "src/main/resources/robocop.jpeg";
    public static final String downloadFolderPath = "C:\\tapper.cloud\\build\\downloads\\qr";

    public static class TestData {
        public static class TapperTable {

            public static final String STAGE_RKEEPER_TABLE_111 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000046";

            public static final String STAGE_IIKO_TABLE_3 = "https://auto-ssr-tapper.zedform.ru/office/3";
            public static final String STAGE_RKEEPER_TABLE_222 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000397";
            public static final String STAGE_RKEEPER_TABLE_333 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000398";
            public static final String STAGE_RKEEPER_TABLE_10 = "https://tapper.staging.zedform.ru/testrkeeper/1000044";
            public static final String TEST_API_URI = "https://taper.zedform.ru/api/";
            public static final String AUTO_API_URI = "https://auto-back-tapper.zedform.ru/api/";
            public static final Integer PAYMENT_BANKS_MAX_PRIORITY_BANKS = 3;
            public static final String TEST_WAITER_COMMENT = "test\\тест";
            public static final String TEST_REVIEW_COMMENT = "test\\тест";
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
            public static final String BEST2PAY_NAME = "https://test.best2pay.net/";
            public static final String TEST_PAYMENT_CARD_NUMBER = "4809388886227309";
            public static final String TEST_PAYMENT_CARD_EXPIRE_MONTH = "12";
            public static final String TEST_PAYMENT_CARD_EXPIRE_YEAR = "24";
            public static final String TEST_PAYMENT_CARD_CVV = "123";


        }
        public static class Yandex {
            public static final String TEST_YANDEX_LOGIN_EMAIL = "autotests@tapper.cloud";
            public static final String TEST_YANDEX_PASSWORD_MAIL = "V8JRPGwr";
            public static final String TEST_YANDEX2_LOGIN_EMAIL = "autotests_waiter@tapper.cloud";
            public static final String TEST_YANDEX2_PASSWORD_MAIL = "autotests2023!";

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
            public static final String ADMIN_RESTAURANT_LOGIN_EMAIL = "kirillk8888@yandex.ru";
            public static final String ADMIN_RESTAURANT_PASSWORD = "777777";
            public static final String ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String WAITER_LOGIN_EMAIL = "jagexo5827@khaxan.com";
            public static final String WAITER_PASSWORD = "123456";
            public static final String WAITER_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String ROBOCOP_WAITER_CHANGED_NAME = "Робокопище";
            public static final String TELEGRAM_AUTO_CHANNEL_LOGIN = "-1001764474981";
        }
        public static class SupportPersonalAccount {

            public static final String RESTAURANT_NAME = "testrkeeper";
            public static final String SUPPORT_LOGIN_EMAIL = "varlone_mag2@mail.ru";
            public static final String SUPPORT_PASSWORD = "123123";

        }
    }
    public static class RegexPattern {

        public static final String tableRegex = "(\\n|.)?Стол: ([\\d+]+)(\\n|.)*";
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
        public static final String ratingCommentRegex = "(\\n|.)*Комментарий: ((\\n|.)*)Рейтинг(\\n|.)*";
        public static final String ratingRegex = "(\\n|.)*Рейтинг: ((\\d|\\n)*)Пожелания(\\n|.)*";
        public static final String suggestionRegex = "(\\n|.)*Пожелания: (.*)";
        public static final String tableReviewRegex = "(\\n|.)*Номер столика: ([\\d+]+)(\\n|.)*";
        public static final String callWaiterCommentRegex = "(\\n|.)*Комментарий: ((\\n|.)*)Время.*";
        public static final String restaurantNameRegex = "Ресторан #\\d+ \\((.+)\\)(\\n|.)*";

    }

}
