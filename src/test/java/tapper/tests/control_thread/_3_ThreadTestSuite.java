package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.keeper_e2e._3_1_waiter",
    "tapper.tests.keeper_e2e._3_2_modificator",
    "tapper.tests.keeper_e2e._3_3_partPayment"

})
@Suite
@SuiteDisplayName("third")
public class _3_ThreadTestSuite {


}
