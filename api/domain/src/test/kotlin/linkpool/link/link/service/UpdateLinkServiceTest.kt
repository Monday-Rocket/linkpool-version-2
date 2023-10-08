package linkpool.link.link.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.`in`.UpdateLinkRequest
import linkpool.link.link.port.out.LinkPort
import linkpool.link.link.service.UpdateLinkService
import linkpool.user.user.model.User
import linkpool.user.user.model.Profile

class UpdateLinkServiceTest: BehaviorSpec({
  val linkPort = mockk<LinkPort>()

  val updateLinkUseCase = UpdateLinkService(linkPort)

  afterContainer {
    clearAllMocks()
  }

  Given("링크 정보 업데이트 서비스") {
    val uid = "12345"
    val linkId = 1L
    val request = UpdateLinkRequest(
      url = "새로운 URL",
      title = "새로운 제목",
      describe = "새로운 설명",
      image = "새로운 이미지"
    )

    When("링크 정보를 업데이트하면") {
      val user = User(
        id = 1L,
        uid = "사용자 ID",
        profile = Profile(
          nickname = "대훈",
          jobGroupId = 2L
        )
      )
      val link = Link(
        id = linkId,
        creatorId = 2,
        title = "기존 제목",
        describe = "기존 설명",
        url = "기존 URL",
        folderId = 0L,
        inflowType = InflowType.BRING
      )

      coEvery { linkPort.findById(linkId) } returns link
      coJustRun { linkPort.update(link) }

      updateLinkUseCase.update(1L, linkId, request)

      Then("링크 정보가 업데이트되어야 한다") {
        link.url shouldBe "새로운 URL"
        link.title shouldBe "새로운 제목"
        link.describe shouldBe "새로운 설명"
        link.image shouldBe "새로운 이미지"
      }
    }

    When("링크 정보를 업데이트할 때 다른 아이디라 권한이 없는 경우") {
      val user = User(
        id = 2L,
        uid = "사용자 ID",
        profile = Profile(
          nickname = "대훈",
          jobGroupId = 2L
        )
      )
      val link = Link(
        id = linkId,
        creatorId = 1,
        title = "기존 제목",
        describe = "기존 설명",
        url = "기존 URL",
        folderId = 0L,
        inflowType = InflowType.BRING
      )

      coEvery { linkPort.findById(linkId) } returns link

      Then("CustomException이 throw되어야 한다") {
        shouldThrow<NotAuthorizedForDataException> {
          updateLinkUseCase.update(2L, linkId, request)
        }
      }
    }
  }
})
