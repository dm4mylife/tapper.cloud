package api;

public class ApiData {

    public static class EndPoints {

        public static final String createOrder = "rkeeper/createorder";
        public static final String fillingOrder = "rkeeper/fillingorder";
        public static final String deleteOrder = "rkeeper/delorder";
        public static final String orderGet = "order/get";

    }


    public static class orderData {

        public static final String R_KEEPER_RESTAURANT = "testrkeeper";
        public static final String TABLE_3 = "12";
        public static final String TABLE_3_ID = "1000046";
        public static final String WAITER_ROBOCOP = "23";
        public static final String BARNOE_PIVO = "1000303";
        public static final String GLAZUNYA = "1000263";
        public static final String SOLYANKA = "1000176";
        public static final String GOVYADINA_PORTION = "1000147";
        public static final String BARANINA = "1000148";


    }


    public static class QueryParams {

        public static String rqParamsCreateOrderBasic(String subDomen, String tableCode, String waiterCode) {

            return "{\n" +
                    "  \"subDomen\": \"" + subDomen + "\",\n" +
                    "  \"tableCode\": " + tableCode + ",\n" +
                    "  \"waiterCode\": \"" + waiterCode + "\",\n" +
                    "  \"persistentComment\": 100500\n" +
                    "}";

        }

        public static String rqParamsFillingOrderBasic(String subDomen, String visit, String dishId, String quantity) {

            return "{\n" +
                   "  \"subDomen\": \"" + subDomen + "\",\n" +
                   "  \"quantity\": " + quantity + ",\n" +
                   "  \"visit\": \"" + visit + "\",\n" +
                   "  \"dishId\": \"" + dishId + "\"\n" +
                   "}";

        }

        public static String rqParamsOrderGet(String table_id, String domen, String guest, String session) {

            return "{\n" +
                    "  \"table_id\": \"" + table_id + "\",\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guest\": \"" + guest + "\",\n" +
                    "  \"session\": \"" + session + "\"\n" +
                    "}";

        }


    }




}
