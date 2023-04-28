package data.table_data_annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;

@Retention(RetentionPolicy.RUNTIME)
public @interface SixTableData {
      String restaurantName() default R_KEEPER_RESTAURANT;
      String tableCode() default TABLE_CODE_666;
      String waiter() default WAITER_ROBOCOP_VERIFIED_WITH_CARD;
      String apiUri() default AUTO_API_URI;
      String tableUrl() default STAGE_RKEEPER_TABLE_666;
      String tableId() default TABLE_AUTO_666_ID;

}
