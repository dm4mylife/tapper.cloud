package tapper.tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.keeper_e2e._5_sockets",
    "tapper.tests.keeper_e2e._6_waiter",
    "tapper.tests.keeper_e2e._7_modificator",
    "tapper.tests.keeper_e2e._8_discount"

})
@Suite
@SuiteDisplayName("third")
public class ThirdThreadTestSuite {


}
