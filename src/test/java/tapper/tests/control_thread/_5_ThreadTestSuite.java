package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.admin_personal_account",
    "tapper.tests.waiter_personal_account",
    "tapper.tests.support_personal_account",
    "tapper.tests.screenshots_comparison"

})
@Suite
@SuiteDisplayName("fifth")
public class _5_ThreadTestSuite {


}
