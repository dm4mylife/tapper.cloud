package tapper.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.admin_personal_account",
    "tapper.tests.waiter_personal_account",
    "tapper.tests.support_personal_account"

})
@Disabled
@Suite
@SuiteDisplayName("fifth")
public class _5_ThreadTest {


}
