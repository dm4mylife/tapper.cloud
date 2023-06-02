package data;

import com.github.javafaker.Faker;

public class Constants {
    public static final Integer WAIT_FOR_FULL_LOAD_PAGE = 1000;
    public static final Integer PAGE_LOAD_TIMEOUT = 360000;
    public static final Integer WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN = 200;
    public static final Integer WAIT_UNTIL_TRANSACTION_EXPIRED = 250000;
    public static final Integer WAIT_UNTIL_TRANSACTION_STILL_ALIVE = 200000;
    public static final int WAIT_FOR_FILE_TO_BE_DOWNLOADED = 15000;
    public static final int WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK = 10000;
    public static final Double SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED = 6.5;
    public static final Double SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED_IN_SUPPORT = 25.0;
    public static final int WAIT_TILL_OPERATION_HISTORY_LIST_IS_UPDATED = 500;
    public static final int WAIT_FOR_TELEGRAM_SUPPORT_SENDING = 30000;
    public static final int WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK = 15000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY = 15000;
    public static final int WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY = 20000;
    public static final int WAIT_FOR_OPERATION_APPEAR = 10000;
    public static final int MOBILE_IMAGE_PIXEL_SIZE = 2962440;
    public static final int DESKTOP_IMAGE_PIXEL_SIZE = 1907178;
    public static final double DIFF_PERCENT_IMAGE = 0.4;
    public static final String DESKTOP_SCREENSHOTS_COMPARISON_ACTUAL_PATH = "screenComparison/desktop/actual/";
    public static final String DESKTOP_SCREENSHOTS_COMPARISON_ORIGINAL_PATH = "screenComparison/desktop/original/";
    public static final String DESKTOP_SCREENSHOTS_COMPARISON_DIFF_PATH = "screenComparison/desktop/diff/";
    public static final String MOBILE_SCREENSHOTS_COMPARISON_ACTUAL_PATH = "screenComparison/mobile/actual/";
    public static final String MOBILE_SCREENSHOTS_COMPARISON_ORIGINAL_PATH = "screenComparison/mobile/original/";
    public static final String MOBILE_SCREENSHOTS_COMPARISON_DIFF_PATH = "screenComparison/mobile/diff/";
    public static final String JAVAX_PROPERTIES_PATH = "src/main/resources/mail.properties";
    public static final String ADMIN_REGISTRATION_EMAIL = "Вы успешно зарегистрировались в нашем сервисе.";
    public static final String WAITER_REGISTRATION_EMAIL = "Вам отправили приглашение на верификацию";
    public static final String RESTORE_PASSWORD_REGISTRATION_EMAIL =
            "Вы отправили запрос на восстановление пароля от личного кабинета";
    public static final String PASTA_IMG_PATH = "src/main/resources/pasta.jpg";
    public static final String LOADER_GIF_PATH = "src/main/resources/loader.gif";
    public static final String HOOKAH_AVATAR_JPG = "src/main/resources/hookahAvatar.jpg";
    public static final String OLD_LOADER_GIF_PATH = "src/main/resources/oldLoader.gif";
    public static final String ROBOCOP_IMG_PATH = "src/main/resources/robocop.jpeg";
    public static final String downloadFolderPath = "build/downloads/qr";
    public static final String downloadFolderPathAdminSupport = "build/downloads/qrAdminSupport";
    public static final String FORM_MUST_BE_FIELD_ERROR_TEXT = "Для сохранения необходимо заполнить поле";
    public static final String TYPE_CORRECT_LINK_ERROR_TEXT = "Введите корректную ссылку";
    public static final String DESKTOP_BROWSER_SIZE = "1920x1080";
    public static final String MOBILE_BROWSER_SIZE = "400x1020";
    public static final String DESKTOP_BROWSER_POSITION = "0x0";
    public static final String MOBILE_BROWSER_POSITION = "600x20";
    public static final String SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN = "dd-MM-yyyy";
    public static final String SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN = "dd.MM.yyyy";
    public static final String SUPPORT_HISTORY_OPERATIONS_CUSTOM_DATE_PATTERN = "MM/d/yyyy";

    public static final String EXPIRED_USER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmbGVldCIsImlhdC" +
            "I6MTY4MDg0NzQzNCwiZXhwIjoxNjgwODUxMDM0LCJzdWIiOjU4Mywicm9sZXNfaWQiOjR9.0wRDY-wr-GAcs1h7lfDowi6BDg0cLQ0P" +
            "7lQ1-kdZcWY";

    public static final String EXPIRED_REFRESH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmbGVldCIsIml" +
            "hdCI6MTY4MDg0NzQzNCwiZXhwIjoxNjgzNDM5NDM0LCJzdWIiOjU4Mywicm9sZXNfaWQiOjR9.xJto6ERVyBac_xouQM7zgPDhtvF2f" +
            "mA9iEMqb2r1M5k";

    public static class TestData {

        public static class TapperTable {

           public static final String STAGE_RKEEPER_TABLE_111 = "https://stage-ssr.zedform.ru/testrkeeper/1000046";
            public static final String STAGE_IIKO_TABLE_111 = "https://stage-ssr.zedform.ru/office/111";
            public static final String STAGE_IIKO_TABLE_222 = "https://stage-ssr.zedform.ru/office/222";
            public static final String STAGE_IIKO_TABLE_333 = "https://stage-ssr.zedform.ru/office/333";
            public static final String STAGE_RKEEPER_TABLE_222 = "https://stage-ssr.zedform.ru/testrkeeper/1000397";
            public static final String STAGE_RKEEPER_TABLE_333 = "https://stage-ssr.zedform.ru/testrkeeper/1000398";
            public static final String STAGE_RKEEPER_TABLE_444 = "https://stage-ssr.zedform.ru/testrkeeper/1000423";
            public static final String STAGE_RKEEPER_TABLE_555 = "https://stage-ssr.zedform.ru/testrkeeper/1000437";
            public static final String STAGE_RKEEPER_TABLE_666 = "https://stage-ssr.zedform.ru/testrkeeper/1000438";
            public static final String STAGE_IIKO_TABLE_3 = "https://stage-ssr.zedform.ru/office/3";
            public static final String AUTO_API_URI = "https://apitapper.zedform.ru/api/";


           /*  public static final String STAGE_RKEEPER_TABLE_111 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000046";
            public static final String STAGE_IIKO_TABLE_111 = "https://auto-ssr-tapper.zedform.ru/office/111";
            public static final String STAGE_RKEEPER_TABLE_222 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000397";
            public static final String STAGE_RKEEPER_TABLE_333 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000398";
            public static final String STAGE_RKEEPER_TABLE_444 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000423";
            public static final String STAGE_RKEEPER_TABLE_555 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000437";
            public static final String STAGE_RKEEPER_TABLE_666 = "https://auto-ssr-tapper.zedform.ru/testrkeeper/1000438";
            public static final String STAGE_IIKO_TABLE_3 = "https://auto-ssr-tapper.zedform.ru/office/3";
            public static final String AUTO_API_URI = "https://auto-back-tapper.zedform.ru/api/";*/

            public static final Integer PAYMENT_BANKS_MAX_PRIORITY_BANKS = 3;
            public static final String TEST_WAITER_COMMENT = "test\\тест";
            public static final String TEST_REVIEW_COMMENT = "test\\тест";
            public static final String TEST_REVIEW_COMMENT_NEGATIVE = "Вкусно, но не то чтобы по вкусу вкусно";
            public static final String TEST_REVIEW_COMMENT_POSITIVE = "Ни о чём не жалею, очень вкусно!";
            public static final String TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL = "Это сообщение отправлено всем";
            public static final String TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS = "Это сообщение отправлено админам";
            public static final String UNKNOWN_WAITER = "Неизвестный официант";
            public static final String REFRESH_TABLE_BUTTON_TEXT = "Обновлено, но заказ ещё не создан";
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
            public static final String SEARCH_BANK = "Альфа-Банк";
            public static final String WRONG_SEARCH_BANK = "123";

            public static final String EMPTY_LOGIN = " ";
            public static final String WRONG_EMAIL_PATTERN = "test";
            public static final Double SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM = 3.5;
            public static final Double SERVICE_CHARGE_PERCENT_FROM_TIPS = 5.0;
            public static final double SERVICE_CHARGE_MAX = 299.0;
            public static final String TIPS_ERROR_MSG = " Минимальная сумма чаевых 49 ₽ ";
            public static final String MIN_SUM_FOR_TIPS_ERROR = "48";
            public static final String MIN_SUM_TIPS = "49";
            public static final String COOKIE_GUEST_SECOND_USER = "123457";
            public static final String COOKIE_SESSION_SECOND_USER = "1235";
            public static final String PART_PAY_STATUS_TEXT = " Статус заказа: Частично оплачен ";
            public static final String FULL_PAY_STATUS_TEXT = " Статус заказа: Полностью оплачен ";

            public static final String ACTIVE_ROLE_BUTTON_BORDER_COLOR = "rgb(103, 100, 255)";
            public static final String SERVICE_CHARGE_TEXT_WHEN_DEACTIVATED =
                    "Я хочу взять на себя транзакционные издержки (\\(.*₽\\)), чтобы сотрудник получил полную сумму чая";





        }

        public static class Best2Pay {
            public static final String BEST2PAY_NAME = "best2pay";
            public static final String TEST_PAYMENT_CARD_NUMBER = "4809388886227309";
            public static final String TEST_PAYMENT_CARD_EXPIRE_MONTH = "12";
            public static final String TEST_PAYMENT_CARD_EXPIRE_YEAR = "24";
            public static final String TEST_PAYMENT_CARD_CVV = "123";

        }

        public static class Yandex {
            public static final String TEST_YANDEX_LOGIN_EMAIL = "autoteststapper@yandex.ru";
            public static final String TEST_YANDEX_PASSWORD_MAIL = "V8JRPGwr";
            public static final String ADMIN_RESTAURANT_TEST_LOGIN_EMAIL = "autotests_admin_restaurant@tapper.cloud";
            public static final String ADMIN_RESTAURANT_TEST_PASSWORD_MAIL = "SxyvGCseCS*a";
            public static final String WAITER_LOGIN_EMAIL = "autotests_waiter@tapper.cloud";
            public static final String EXISTING_ADMIN_RESTAURANT_MAIL = "veydedumli@gufum.com";
            public static final String WAITER_PASSWORD_MAIL = "W1LrKR29xwp9";
            public static final String YANDEX_MAIL_URL =
                    "https://passport.yandex.ru/auth?retpath=https%3A%2F%2Fmail.yandex.ru%2F&backpath=https%3A%2F%2F" +
                            "mail.yandex.ru%2F%3Fnoretpath%3D1&from=mail&origin=hostroot_homer_auth_ru";
        }

        public static class AdminPersonalAccount {

            public static final int MAX_TELEGRAM_LOGIN_SIZE = 16;
            public static final String TEST_WIFI_NETWORK_NAME = "auto_wifi";
            public static final String TEST_WIFI_NETWORK_PASSWORD = "12345678";
            public static final String TEST_WIFI_NETWORK_PASSWORD_MAX_LENGTH = "1234567890123456789012345";
            public static final String ROBOCOP_WAITER = "Robocop";
            public static final String MEGATRON_WAITER = "Megatron";
            public static final String MEGATRON_WAITER_ID = "1000081";
            public static final String OPTIMUS_PRIME_WAITER = "Optimus Prime";
            public static final String TERMINATOR_WAITER = "Terminator";
            public static final String IRONHIDE_WAITER = "IronHide";
            public static final String IRON_MAN_WAITER = "Iron Man";
            public static final String NON_EXIST_WAITER = "Ингеборга Эдмундовна Дапкунайте";
            public static final String SEARCH_WAITER_ERROR_TEXT = "Нет результатов. Попробуйте ввести данные ещё раз";
            public static final String ADMIN_TEST_PHONE = "+7(123) 456-78-90";
            public static final String PERSONAL_ACCOUNT_AUTHORIZATION_STAGE_URL =
                    "https://tapper.staging.zedform.ru/users";
            public static final String PERSONAL_ACCOUNT_PROFILE_STAGE_URL = "https://tapper.staging.zedform.ru/profile";
            public static final String ROOT_TAPPER_STAGE_URL = "https://tapper.staging.zedform.ru";
            public static final String RESTORE_PASSWORD_STAGE_URL = "https://tapper.staging.zedform.ru/users/forget";
            public static final String PERSONAL_ACCOUNT_REGISTRATION_STAGE_URL =
                    "https://tapper.staging.zedform.ru/users/registration";

            public static final String ADMIN_RESTAURANT_LOGIN_EMAIL = "kirillk8888@yandex.ru";
            public static final String ADMIN_RESTAURANT_PASSWORD = "999999";

            public static final String ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String WAITER_LOGIN_EMAIL = "jagexo5827@khaxan.com";
            public static final String WAITER_PASSWORD = "123456";

            public static final String WAITER_NO_CARD_HAS_GOAL_LOGIN_EMAIL = "webohe1675@mevori.com";
            public static final String WAITER_NO_CARD_HAS_GOAL_PASSWORD = "590f0017-8464-4259-b3d5-8425855073b0";
            public static final String WAITER_GOAL_TEXT = "У самурая нет цели, только путь";


            public static final String WAITER_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String ROBOCOP_WAITER_CHANGED_NAME = "Робокопище";
            public static final String TELEGRAM_AUTO_CHANNEL_LOGIN = "-1001764474981";
            public static final String TELEGRAM_AUTO_LOGIN = "user_unknown_nb";
            public static final String VERIFIED_WAITER_TEXT = "Официант верифицирован";
            public static final String IS_WAITING_WAITER_TEXT = "Ожидает приглашения";
            public static final String INVITED_IN_SERVICE_TEXT = "Приглаш(е|ё)н в систему";
            public static final String NOT_CHOSEN_DISH_AMOUNT_INPUT_ERROR = "Не выбрано количество ";
            public static final String FORBIDDEN_CHARS_FOR_NAME_BY_GUEST_AND_INGREDIENTS = "!\"№;%:?*()_+,,,";
            public static final String ACCEPTABLE_CHARS_FOR_NAME_BY_GUEST_AND_INGREDIENTS = ",,,";
            public static final String FORBIDDEN_CHARS_FOR_DESCRIPTION = "@#$[]{}%!?()";
            public static final String ACCEPTABLE_CHARS_FOR_DESCRIPTION = "%!?()";
            public static final String FIRST_AUTO_MENU_CATEGORY = "Не трогать эту категорию №1";
            public static final String SECOND_AUTO_MENU_CATEGORY = "Не трогать эту категорию №2";
            public static final String YANDEX_REVIEW_LINK =
                    "https://yandex.ru/maps/org/nalu_baloo/48679020937/reviews/" +
                            "?add-review=true&ll=37.559181%2C55.712556&z=13";
            public static final String TWOGIS_REVIEW_LINK =
                    "https://2gis.ru/moscow/firm/70000001060393218/tab/reviews?writeReview&m=37.58076%2C55.748187%2F16";
            public static final String GOOGLE_REVIEW_LINK =
                    "https://www.google.com/search?sxsrf=AJOqlzWmZL35-IjT_iu4nwWZOnSHikFmJw:1678769978813&q=" +
                            "%D0%BD%D0%B0%D0%BB%D1%83+%D0%B1%D0%B0%D0%BB%D1%83&spell=" +
                            "1&sa=X&ved=2ahUKEwi2pvzB0dr9AhXQX_EDHdFKB6wQBSgAegQICBAB&biw=1440&bih=680&dpr" +
                            "=2#lrd=0x46b54b4bbf99384f:0xfe6ee71d2bb7464a,3";
            public static final String OVER_LIMIT_CHARS_NAME_BY_GUEST_INPUT =
                    "В этом поле будет содержаться более шестидесяти одного символа, ага";
            public static final String LIMIT_CHARS_NAME_BY_GUEST_INPUT =
                    "В этом поле будет содержаться более шестидесяти одного символ";
            public static final String LIMIT_CHARS_NAME_BY_GUEST_COUNTER = "61 / 61";
            public static final String CHOSEN_MENU_CATEGORY = "rgb(213, 219, 241)";
            public static final String EDIT_DISH_ERROR = "rgba(236, 78, 78, 1)";
            public static final String PERIOD_BUTTON_BORDER = "rgba(103, 100, 255, 1)";


            static Faker faker = new Faker();
            static String randomWords = faker.lorem().fixedString(7);
            public static final String OVER_LIMIT_CHARS_DESCRIPTIONS_INPUT =
                    "Рост намеченных и разработке играет от формировании порядка, " + randomWords + ", порядка, " +
                            "также оценить занимаемых соответствующий реализация условий. Рост и структура " +
                            "организации заданий активности образом также намеченных идейные анализа от сфера " +
                            "условий задач. Роль нашей и форм намеченных показывает, в наше время";
            public static final String LIMIT_CHARS_DESCRIPTIONS_INPUT =
                    "Рост намеченных и разработке играет от формировании порядка, " + randomWords + ", порядка, " +
                            "также оценить занимаемых соответствующий реализация условий. Рост и структура " +
                            "организации заданий активности образом также намеченных идейные анализа от сфера" +
                            " условий задач. Роль нашей и форм намеченных показывает, в наше";
            public static final String LIMIT_CHARS_DESCRIPTIONS_COUNTER = "300 / 300";
            public static final String OVER_LIMIT_CHARS_INGREDIENTS_INPUT = "Играет особенности важную важную роль" +
                    " идейные отношении же оценить количественный важные и позволяет кадров формировании порядка," +
                    " постоянный финансовых модель эксперимент реализации также задач. А равным идейные постоянный" +
                    " нас формировании проверки прогрессии";
            public static final String LIMIT_CHARS_INGREDIENTS_INPUT = "Играет особенности важную важную " +
                    "роль идейные отношении же оценить количественный важные и позволяет кадров формировании" +
                    " порядка, постоянный финансовых модель эксперимент реализации также задач. А равным идейные" +
                    " постоянный нас формировании проверки прогре";
            public static final String LIMIT_CHARS_INGREDIENTS_COUNTER = "255 / 255";
            public static final String OVER_LIMIT_CHARS_AMOUNT_INPUT = "123456789";
            public static final String LIMIT_CHARS_AMOUNT_INPUT = "12345678";
            public static final String LIMIT_CHARS_AMOUNT_COUNTER = "8 / 8";
            public static final String OVER_LIMIT_CHARS_CALORIES_INPUT = "12345";
            public static final String LIMIT_CHARS_CALORIES_INPUT = "1234";
            public static final String LIMIT_CHARS_CALORIES_COUNTER = "4 / 4";

        }

        public static class SupportPersonalAccount {

            public static final String KEEPER_RESTAURANT_NAME = "testrkeeper";
            public static final String IIKO_RESTAURANT_NAME = "office";
            public static final String SUPPORT_LOGIN_EMAIL = "varlone_mag2@mail.ru";
            public static final String SUPPORT_PASSWORD = "567567";
            public static final String INCORRECT_DATA = "1111";

            public static final String SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST = "123456";
            public static final String SUPPORT_TEST_PHONE = "+7(123) 456-78-90";

            public static final int HISTORY_OPERATION_DEFAULT_LIST_ITEM_SIZE = 15;




        }

        public static class RegistrationData {

            public static final String NAME = "Робокоп";
            public static final String TELEPHONE_NUMBER = "9068723655";
            public static final String EMAIL = Yandex.ADMIN_RESTAURANT_TEST_LOGIN_EMAIL;
            public static final String PASSWORD = Yandex.ADMIN_RESTAURANT_TEST_PASSWORD_MAIL;
            public static final String CONFIRMATION_PASSWORD = Yandex.ADMIN_RESTAURANT_TEST_PASSWORD_MAIL;
            public static final String RESTAURANT_NAME = "show me your love";
            public static final String EXISTING_EMAIL_ERROR_TEXT = "Этот E-mail уже зарегистрирован";


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
            public static final String tableNumberRegex = "\\D+";



        }

        public static class Mail {

            public static final String WAITER_LOGIN_MAIL_REGEX = "(?<=span>)(.*)(?=</span><br>)";
            public static final String ADMINISTRATOR_LOGIN_MAIL_REGEX =
                    "(?<=Логин<\\/span>: <a href=\"autotests_admin_restaurant@tapper\\.cloud\">)(.*)(?=<\\/a><br>)";
            public static final String WAITER_PASSWORD_MAIL_REGEX = "(\\w+-\\w+-\\w+-\\w+-\\w+)";
            public static final String ADMINISTRATOR_PASSWORD_MAIL_REGEX = "(?<=Пароль<\\/span>: )(.*)(?=<br>)";
            public static final String AUTH_URL_MAIL_REGEX = "(?<=<a href=\\\")(.*)(?=\\/\" target=\\\"_blank\\\")";
            public static final String RESTORE_PASSWORD_URL_MAIL_REGEX = "(?<=href=\")(.*)(?=\" target=\"_blank\")";

        }

    }

}
