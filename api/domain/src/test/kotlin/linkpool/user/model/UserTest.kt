package linkpool.user.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import linkpool.exception.NotEnoughForSigningUpException
import linkpool.support.spec.afterRootTest
import linkpool.user.fixtures.createUser
import linkpool.user.fixtures.createUserInfoRequest
import linkpool.user.fixtures.createUserInformation
import linkpool.user.fixtures.createUserWithoutInfo

class UserTest: BehaviorSpec({

    Given("활성화된 유저에 대해서") {
        val user = createUser()
        When("비활성화를 하면") {
            user.delete()
            Then("프로필 정보가 삭제되고 유저가 비활성화된다") {
                user.info shouldBe null
                user.isNotActivated() shouldBe true
            }
        }
    }

    Given("비활성화된 유저에 대해서") {
        val user = createUser(deleted = true)
        When("활성화를 하면") {
            user.activate()
            Then("유저가 활성화된다") {
                user.isActivated() shouldBe true
            }
        }
    }

    Given("프로필이 있는 유저에 대해서") {
        val user1 = createUser()
        val user2 = createUser()
        val user3 = createUser()
        When("프로필 정보를 주고 수정하면") {
            val userInfoRequestWithNickname =
                createUserInfoRequest(nickname = "사용자2", jobGroupId = null, profileImage = null)
            val userInfoRequestWithJobGroupId =
                createUserInfoRequest(nickname = null, jobGroupId = 2L, profileImage = null)
            val userInfoRequestWithProfileImage =
                createUserInfoRequest(nickname = null, jobGroupId = null, profileImage = "02")
            user1.patchInfo(userInfoRequestWithNickname)
            user2.patchInfo(userInfoRequestWithJobGroupId)
            user3.patchInfo(userInfoRequestWithProfileImage)
            Then("요청한 정보만 수정되고 나머지는 기존을 유지한다") {
                user1.info!!.nickname shouldBe userInfoRequestWithNickname.nickname
                user1.info!!.jobGroupId shouldBe createUser().info!!.jobGroupId
                user1.info!!.profileImage shouldBe createUser().info!!.profileImage
                user2.info!!.nickname shouldBe createUser().info!!.nickname
                user2.info!!.jobGroupId shouldBe userInfoRequestWithJobGroupId.jobGroupId
                user2.info!!.profileImage shouldBe createUser().info!!.profileImage
                user3.info!!.nickname shouldBe createUser().info!!.nickname
                user3.info!!.jobGroupId shouldBe createUser().info!!.jobGroupId
                user3.info!!.profileImage shouldBe userInfoRequestWithProfileImage.profileImage
            }
        }
    }


    Given("프로필이 없는 유저에 대해서") {
        When("프로필 정보 생성 여부를 확인하면") {
            val user = createUserWithoutInfo()
            val result = user.checkInfoCreated()
            Then("false가 리턴된다") {
                result shouldBe false
            }
        }

        When("완전한 정보를 주고 프로필을 생성하면") {
            val user = createUserWithoutInfo()
            val userInfo = createUserInformation()
            user.createInfo(userInfo)
            Then("요청 정보대로 프로필이 생성된다") {
                user.info shouldNotBe null
                user.info!!.nickname shouldBe userInfo.nickname
                user.info!!.jobGroupId shouldBe userInfo.jobGroupId
                user.info!!.profileImage shouldBe userInfo.profileImage
            }
        }

        When("완전한 정보를 주고 프로필을 수정하면") {
            val user = createUserWithoutInfo()
            val userInfoRequest = createUserInfoRequest()
            user.patchInfo(userInfoRequest)
            Then("요청 정보대로 프로필이 생성된다") {
                user.info shouldNotBe null
                user.info!!.nickname shouldBe userInfoRequest.nickname
                user.info!!.jobGroupId shouldBe userInfoRequest.jobGroupId
                user.info!!.profileImage shouldBe userInfoRequest.profileImage
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }

})