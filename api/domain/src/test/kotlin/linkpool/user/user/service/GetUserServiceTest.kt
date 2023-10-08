package linkpool.user.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import linkpool.exception.DataNotFoundException
import linkpool.exception.NotSignedUpException
import linkpool.support.spec.afterRootTest
import linkpool.user.user.fixtures.USER_NICKNAME
import linkpool.user.user.fixtures.createUser
import linkpool.user.user.fixtures.createUserWithoutInfo
import linkpool.user.user.port.out.UserPort

class GetUserServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val userQueryService = GetUserService(userPort)

    Given("조회하고자 하는 유저가 없을 경우") {
        coEvery { userPort.findById(any()) } returns null
        coEvery { userPort.findByUid(any()) } returns null

        When("id를 이용하여 nullable 유저를 조회하면") {
            val user = userQueryService.getByIdOrNull(1L)
            Then("null이 리턴된다") {
                user shouldBe null
            }
        }
        When("id를 이용하여 유저를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<DataNotFoundException> {
                    userQueryService.getById(1L)
                }
            }
        }
    }

    Given("조회하고자 하는 유저가 프로필이 생성되지 않았을 경우") {
        val userWithoutInfo = createUserWithoutInfo()
        coEvery { userPort.findById(any()) } returns userWithoutInfo
        coEvery { userPort.findByUid(any()) } returns userWithoutInfo

        When("id를 이용하여 유저를 조회") {
            Then("NotSignedUpException 예외가 발생한다") {
                shouldThrow<NotSignedUpException> {
                    userQueryService.getById(1L)
                }
            }
        }
    }

    Given("조회하고자 하는 유저가 프로필이 생성되어있을 경우") {
        val userWithInfo = createUser()
        coEvery { userPort.findById(any()) } returns userWithInfo
        coEvery { userPort.findByUid(any()) } returns userWithInfo

        When("id를 이용하여 유저를 조회하면") {
            val user = userQueryService.getById(1L)
            Then("해당 유저가 리턴된다") {
                user shouldBe userWithInfo
            }
        }
    }

    Given("확인하고자 하는 닉네임이 존재하지 않는 경우") {
        coEvery { userPort.existsByNickname(any()) } returns false

        When("닉네임을 존재 여부를 확인하면") {
            val result = userQueryService.existsByNickname(USER_NICKNAME)
            Then("false가 리턴된다") {
                result shouldBe false
            }
        }
    }

    Given("확인하고자 하는 닉네임이 존재하는 경우") {
        coEvery { userPort.existsByNickname(any()) } returns true

        When("닉네임을 존재 여부를 확인하면") {
            val result = userQueryService.existsByNickname(USER_NICKNAME)
            Then("true가 리턴된다") {
                result shouldBe true
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})