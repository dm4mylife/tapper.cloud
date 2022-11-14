package api;

import java.util.ArrayList;

public class testPojo {

    public class OrdersElementAll{
        public String position_id;
        public int sort;
        public String name;
        public int price;
        public String order_id;
        public int orders_id;
        public int id;
        public ArrayList<Object> modificators;
        public Object status_pay;
        public boolean selected;
        public Object selected_guest_id;
        public int amount;
    }

    public class Root{
        public ArrayList<OrdersElementAll> ordersElementAll;
    }



}
