package linkpool.report.report.service

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import linkpool.exception.DuplicateReportException
import linkpool.report.report.fixtures.createCreateReportRequest
import linkpool.report.report.fixtures.createReport
import linkpool.report.report.port.out.ReportPort
import linkpool.support.spec.afterRootTest

class CreateReportServiceTest: BehaviorSpec({
    val reportPort = mockk<ReportPort>()
    val createReportService = CreateReportService(reportPort)

    Given("신고자와 대상이 동일한 신고 정보가 아마 등록되어있는 경우") {
        val report = createReport()
        coEvery { reportPort.findByReporterIdAndTarget(any(), any()) } returns report
        When("신고를 등록하면") {
            val createReportRequest = createCreateReportRequest()
            Then("DuplicateReportException 예외가 발생한다") {
                shouldThrow<DuplicateReportException> {
                    createReportService.create(1L, createReportRequest)
                }
            }
        }
    }

    Given("신고자와 대상이 동일한 신고 정보가 아마 등록되어있지 않은 경우") {
        val report = createReport()
        coEvery { reportPort.findByReporterIdAndTarget(any(), any()) } returns null
        coEvery { reportPort.save(any()) } returns report

        When("신고를 등록하면") {
            val createReportRequest = createCreateReportRequest()
            Then("예외가 발생하지 않고 신고가 등록된다") {
                shouldNotThrow<DuplicateReportException> {
                    createReportService.create(1L, createReportRequest)
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})