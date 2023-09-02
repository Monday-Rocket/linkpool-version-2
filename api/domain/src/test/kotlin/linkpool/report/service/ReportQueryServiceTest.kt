package linkpool.report.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import linkpool.report.fixtures.createReport
import linkpool.report.fixtures.createReportTarget
import linkpool.report.port.out.ReportPort
import linkpool.support.spec.afterRootTest

class ReportQueryServiceTest : BehaviorSpec({
    val reportPort = mockk<ReportPort>()
    val reportQueryService = GetReportService(reportPort)

    Given("어떤 신고 정보에 대해서") {
        val report = createReport()
        every { reportPort.findByReporterIdAndTarget(any(), any()) } returns report
        When("신고자와 신고대상을 이용하여 조회하면") {
            val reportTarget = createReportTarget()
            val selected = reportQueryService.getByReportIdAndTargetOrNull(1L, reportTarget)
            Then("해당 신고자와 신고대상이 동일한 신고 정보가 조회된다") {
                selected shouldBe report
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})