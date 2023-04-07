package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({

    "tapper.tests.keeper_e2e._2_1_sockets",
    "tapper.tests.keeper_e2e._2_2_stress",
    "tapper.tests.critical_tests.admin_personal_account.lock"

})

@Suite
@SuiteDisplayName("part test")
public class _2_ThreadTestSuite {
}
