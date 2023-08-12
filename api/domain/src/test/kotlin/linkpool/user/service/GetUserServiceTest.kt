package linkpool.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import linkpool.exception.DataNotFoundException
import linkpool.exception.NotSignedUpException
import linkpool.support.spec.afterRootTest
import linkpool.user.fixtures.USER_NICKNAME
import linkpool.user.fixtures.USER_UID
import linkpool.user.fixtures.createUser
import linkpool.user.fixtures.createUserWithoutInfo
import linkpool.user.port.out.UserPort

class GetUserServiceTest: BehaviorSpec({
    val userPort = mockk<UserPort>()
    val userQueryService = GetUserService(userPort)

    Given("조회하고자 하는 유저가 없을 경우") {
        every { userPort.findById(any()) } returns null
        every { userPort.findByUid(any()) } returns null

        When("uid를 이용하여 nullable 유저를 조회하면") {
            val user = userQueryService.getByUidOrNull(USER_UID)
            Then("null이 리턴된다") {
                user shouldBe null
            }
        }
        When("id를 이용하여 nullable 유저를 조회하면") {
            val user = userQueryService.getByIdOrNull(1L)
            Then("null이 리턴된다") {
                user shouldBe null
            }
        }
        When("uid를 이용하여 유저를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<DataNotFoundException> {
                    userQueryService.getByUid(USER_UID)
                }
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
        every { userPort.findById(any()) } returns userWithoutInfo
        every { userPort.findByUid(any()) } returns userWithoutInfo

        When("uid를 이용하여 유저를 조회하면") {
            Then("NotSignedUpException 예외가 발생한다") {
                shouldThrow<NotSignedUpException> {
                    userQueryService.getByUid(USER_UID)
                }
            }
        }
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
        every { userPort.findById(any()) } returns userWithInfo
        every { userPort.findByUid(any()) } returns userWithInfo

        When("uid를 이용하여 유저를 조회하면") {
            val user = userQueryService.getByUid(USER_UID)
            Then("해당 유저가 리턴된다") {
                user shouldBe userWithInfo
            }
        }
        When("id를 이용하여 유저를 조회하면") {
            val user = userQueryService.getById(1L)
            Then("해당 유저가 리턴된다") {
                user shouldBe userWithInfo
            }
        }
    }

    Given("확인하고자 하는 닉네임이 존재하지 않는 경우") {
        every { userPort.existsByInfoNickname(any()) } returns false

        When("닉네임을 존재 여부를 확인하면") {
            val result = userQueryService.existsByNickname(USER_NICKNAME)
            Then("false가 리턴된다") {
                result shouldBe false
            }
        }
    }

    Given("확인하고자 하는 닉네임이 존재하는 경우") {
        every { userPort.existsByInfoNickname(any()) } returns true

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