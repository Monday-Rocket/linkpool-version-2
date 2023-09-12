package linkpool.user.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.support.spec.afterRootTest
import linkpool.user.fixtures.createUser
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.service.SignOutService

class SignOutServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val signOutService = SignOutService(userPort)

    Given("활성화된 회원 일 경우") {
        val user = createUser(deleted = false)
        every { userPort.patch(any()) } just Runs
        When("회원 탈퇴를 하면") {
            signOutService.signOut(user)
            Then("회원 정보가 폐기되고 비활성화된다") {
                user.profile shouldBe null
                verify { userPort.patch(user) }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }

})