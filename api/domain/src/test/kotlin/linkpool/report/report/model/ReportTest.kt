package linkpool.report.report.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import linkpool.report.report.fixtures.createReport
import linkpool.support.spec.afterRootTest

class ReportTest: BehaviorSpec({

    Given("유효한 신고 정보에 대해서") {
        val report = createReport()
        When("이를 삭제하면") {
            report.delete()
            Then("삭제 여부가 true가 된다") {
                report.deleted shouldBe true
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})