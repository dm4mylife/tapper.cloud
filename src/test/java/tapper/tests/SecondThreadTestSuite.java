package tapper.tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({

        "tapper.tests.admin_personal_account",
        "tapper.tests.waiter_personal_account",
        "tapper.tests.support_personal_account",
        "tapper.tests.keeper_e2e._4_addAndRemoveDishPositions",
        "tapper.tests.keeper_e2e._5_sockets"

})

@Suite
@SuiteDisplayName("part test")
public class SecondThreadTestSuite {
}
