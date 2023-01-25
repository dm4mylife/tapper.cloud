package tapper.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import tapper.tests.keeper_e2e._1_fullPayment._1_1_TipsScTest;
import tapper.tests.keeper_e2e._1_fullPayment._1_2_TipsNoScTest;

@SelectPackages({

        "tapper.tests.admin_personal_account",
        "tapper.tests.waiter_personal_account",

})

@Suite
@SuiteDisplayName("part test")
public class FirstThreadTestSuite {
}
