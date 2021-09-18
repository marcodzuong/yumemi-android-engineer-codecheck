package jp.co.yumemi.android.code_check

import jp.co.yumemi.android.code_check.TopActivityTest
import jp.co.yumemi.android.code_check.features.screen.search.OneFragmentTest
import jp.co.yumemi.android.code_check.features.screen.search.OneViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    TopActivityTest::class,
    OneViewModelTest::class,
    OneFragmentTest::class
)
class UITestSuite