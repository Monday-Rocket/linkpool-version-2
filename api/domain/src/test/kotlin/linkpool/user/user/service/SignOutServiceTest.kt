package linkpool.user.user.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.support.spec.afterRootTest
import linkpool.user.user.fixtures.createUser
import linkpool.user.user.port.`in`.GetUserUseCase
import linkpool.user.user.port.out.UserEventPublishPort
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.service.SignOutService

class SignOutServiceTest: BehaviorSpec({
    val getUserUseCase = mockk<GetUserUseCase>()
    val userPort = mockk<UserPort>()
    val userEventPublishPort = mockk<UserEventPublishPort>()

    val signOutService = SignOutService(getUserUseCase, userPort, userEventPublishPort)

    Given("활성화된 회원 일 경우") {
        val user = createUser(deleted = false)
        coEvery { getUserUseCase.getById(any()) } returns user
        coEvery { userPort.patch(any()) } just Runs
        coJustRun { userEventPublishPort.publishUserSignedOutEvent(any()) }
        When("회원 탈퇴를 하면") {
            signOutService.signOut(1L)
            Then("회원 정보가 폐기되고 비활성화된다") {
                user.profile shouldBe null
                coVerify { userPort.patch(user) }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }

})