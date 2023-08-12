package linkpool.jobgroup.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import linkpool.exception.DataNotFoundException
import linkpool.jobgroup.fixtures.createJobGroup
import linkpool.jobgroup.port.out.JobGroupPort
import linkpool.support.spec.afterRootTest

class JobGroupQueryServiceTest: BehaviorSpec({
    val jobGroupPort = mockk<JobGroupPort>()
    val jobGroupQueryService = JobGroupQueryService(jobGroupPort)

    Given("직업군이 1개 이상 등록되어 있을 경우") {
        val jobGroup1 = createJobGroup(id = 1)
        val jobGroup2 = createJobGroup(id = 2)
        every { jobGroupPort.findAll() } returns listOf(jobGroup1, jobGroup2)
        every { jobGroupPort.findById(1L) } returns jobGroup1
        every { jobGroupPort.findById(3L) } returns null

        When("직업군을 모두 조회하면") {
            val all = jobGroupQueryService.getAll()
            Then("존재하는 모든 직업군이 조회된다") {
                all shouldContainAll listOf(jobGroup1,jobGroup2)
            }
        }

        When("유효한 ID를 이용하여 직업군을 조회하면") {
            val jobGroup = jobGroupQueryService.getById(1L)
            Then("해당 ID를 가진 직업군이 조회된다") {
                jobGroup shouldBe jobGroup1
            }
        }

        When("유효하지 않은 ID를 이용하여 nullable 직업군을 조회하면") {
            val jobGroup = jobGroupQueryService.getByIdOrNull(3L)
            Then("null이 리턴된다") {
                jobGroup shouldBe null
            }
        }

        When("유효하지 않은 ID를 이용하여 직업군을 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<DataNotFoundException> {
                    jobGroupQueryService.getById(3L)
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})