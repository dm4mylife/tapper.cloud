package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@SelectPackages({

    "tapper.tests.personal_account.admin",
    "tapper.tests.personal_account.support",
    "tapper.tests.personal_account.waiter"

})
@Suite
class _5_ThreadTestSuite { }
