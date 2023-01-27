package tapper.tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({

        "tapper.tests.admin_personal_account",
        "tapper.tests.waiter_personal_account",
        "tapper.tests.keeper_e2e._5_sockets",
        "tapper.tests.keeper_e2e._3_tips",
        "tapper.tests.keeper_e2e._8_discount"

})

@Suite
@SuiteDisplayName("part test")
public class FirstThreadTestSuite {
}
