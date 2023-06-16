package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@SelectPackages({

    "tapper.tests.keeper._4_1_discount"
    //"tapper.tests.iiko._4_0_part_payment",
   // "tapper.tests.iiko._4_1_add_and_remove_dishes"

})
@Suite
class _4_ThreadTestSuite { }
