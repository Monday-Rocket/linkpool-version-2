package linkpool.user.user.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import linkpool.user.user.model.Profile
import linkpool.user.user.model.User
import linkpool.user.user.port.`in`.GetUserUseCase
import linkpool.user.user.port.`in`.ProfileRequest
import linkpool.user.user.port.`in`.UpdateUserUseCase
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.service.GetUserService
import linkpool.user.user.service.UpdateUserService

class UpdateUserServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val getUserUseCase = mockk<GetUserUseCase>()

    val getUseService = UpdateUserService(userPort, getUserUseCase)
    Given("유저 업데이트 테스트") {
        val user = User(
            1L, "uid-1234", Profile("강대훈", 1L)
        )

        val updateProfileRequest = ProfileRequest(
            "대훈",
            2L,
            "주황버섯.jpg"
        )
        coEvery { getUserUseCase.getById(1L) } returns user
        coEvery { userPort.existsByNickname(updateProfileRequest.nickname!!) } returns false
        coEvery { userPort.patch(user) } just Runs

        When("PK가 1이고 닉네임이 '강대훈'인 유저를 업데이트하면") {
            getUseService.updateProfile(1L, updateProfileRequest)
            Then("닉네임이 '대훈'으로 변경된다.") {
                user.profile?.let { it ->
                    it.nickname shouldBe "대훈"
                    it.profileImage shouldBe "주황버섯.jpg"
                }
            }
        }
    }
})