package linkpool.user.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.support.spec.afterRootTest
import linkpool.user.fixtures.USER_UID
import linkpool.user.fixtures.createUser
import linkpool.user.fixtures.createUserWithoutInfo
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.service.CreateUserService

class CreateUserServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val signUpService = CreateUserService(userPort)

    Given("한번도 회원가입한 적 없는 유저인 경우") {
        val user = createUserWithoutInfo()
        every { userPort.findByUidIncludingDeleted(USER_UID) } returns null
        every { userPort.save(any()) } returns user
        When("회원 가입을 하면") {
            val response = signUpService.createUser(USER_UID)
            Then("유저가 저장되고 ID가 부여되며 새로운 유저임이 표시된다") {
                verify { userPort.save(any()) }
                response.id shouldBe user.id
                response.isNew shouldBe true
            }
        }
    }

    Given("회원가입이 완료된 유저인 경우") {
        val user = createUser()
        every { userPort.findByUidIncludingDeleted(USER_UID) } returns user

        When("회원 가입을 하면") {
            val response = signUpService.createUser(USER_UID)
            Then("해당 유저가 새로운 유저가 아님이 표시된다") {
                response.id shouldBe user.id
                response.isNew shouldBe false
            }
        }
    }

    Given("회원가입은 되었으나 프로필이 생성되지 않은 유저일 경우") {
        val user = createUserWithoutInfo()
        every { userPort.findByUidIncludingDeleted(USER_UID) } returns user
        When("회원 가입을 하면") {
            val response = signUpService.createUser(USER_UID)
            Then("해당 유저의 ID 값을 전해주나, 새로운 유저임이 표시된다") {
                response.id shouldBe user.id
                response.isNew shouldBe true
            }
        }
    }

    Given("회원탈퇴를 완료한 유저일 경우") {
        val user = createUserWithoutInfo(deleted = true)
        every { userPort.findByUidIncludingDeleted(USER_UID) } returns user
        every { userPort.patch(any()) } just Runs
        When("회원 가입을 하면") {
            val response = signUpService.createUser(USER_UID)
            Then("해당 유저를 활성화하고, 해당 ID 값을 전해주며, 새로운 유저임이 표시된다") {
                user.isActivated() shouldBe true
                verify { userPort.patch(user) }
                response.id shouldBe user.id
                response.isNew shouldBe true
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }

})