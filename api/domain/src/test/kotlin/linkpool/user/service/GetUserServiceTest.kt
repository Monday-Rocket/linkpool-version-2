package linkpool.user.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import linkpool.user.user.model.Profile
import linkpool.user.user.model.User
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.service.GetUserService

class GetUserServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val getUseService = GetUserService(userPort)
    val user = User(
        1L, "uid-1234", Profile("강대훈", 1L)
    )
    Given("단순한 유저 조회 테스트 ㅇㅅㅇ") {
        coEvery { userPort.findById(1L) } returns user
        When("1번 유저를 조회하면") {
            val result = getUseService.getById(1L)
            Then("강대훈이라는 닉네임을 가진 유저가 호출된다.") {
                result.profile?.nickname shouldBe "강대훈"
            }
        }
    }
})