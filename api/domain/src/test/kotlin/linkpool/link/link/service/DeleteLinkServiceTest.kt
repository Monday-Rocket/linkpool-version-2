package linkpool.link.link.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.out.LinkPort
import linkpool.user.user.model.User
import linkpool.user.user.model.Profile
import linkpool.user.user.port.`in`.GetUserUseCase

class DeleteLinkServiceTest: BehaviorSpec({

  val linkPort = mockk<LinkPort>()
  val deleteLinkService = DeleteLinkService(linkPort)

  Given("링크 삭제 서비스") {
    val userId = 1L
    val linkId = 2L

    When("링크를 삭제하면") {
      val user = User(
        id = userId,
        uid = "사용자 ID",
        profile = Profile(
          nickname = "대훈",
          jobGroupId = 2L
        )
      )
      val link = Link(
        id = linkId,
        creatorId = 1,
        title = "링크 제목",
        describe = "링크 설명",
        url = "링크 URL",
        folderId = 0L,
        inflowType = InflowType.BRING
      )

      coEvery { linkPort.findById(linkId) } returns link
      coJustRun { linkPort.delete(link) }

      deleteLinkService.delete(userId, linkId)

      Then("링크가 삭제된다.") {
        link.isDeleted() shouldBe true
      }
    }

    When("링크를 삭제할 때 권한이 없는 경우") {
      val user = User(
        id = 1L,
        uid = "다른 사용자 ID",
        profile = Profile(
          nickname = "다른 사용자",
          jobGroupId = 2L
        )
      )
      val link = Link(
        id = linkId,
        creatorId = 1,
        title = "링크 제목",
        describe = "링크 설명",
        url = "링크 URL",
        folderId = 0L,
        inflowType = InflowType.BRING
      )

      coEvery { linkPort.findById(linkId) } returns link

      Then("NotAuthorizedForDataException이 throw되어야 함") {
        shouldThrow<NotAuthorizedForDataException> {
          deleteLinkService.delete(2L, linkId)
        }
      }
    }

    When("폴더에 속한 링크를 일괄 삭제하면") {
      val folderId = 1L
      coEvery { linkPort.deleteBatchByFolderId(folderId) } just runs

      deleteLinkService.deleteByFolder(folderId)

      Then("폴더에 속한 링크가 일괄 삭제되어야 함") {
        coVerify (exactly = 1) { linkPort.deleteBatchByFolderId(folderId) }
      }
    }
  }
})