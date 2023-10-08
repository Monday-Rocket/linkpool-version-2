package linkpool.link.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.out.LinkPort
import linkpool.link.port.out.getById
import linkpool.link.link.service.DeleteLinkService
import linkpool.user.user.model.User
import linkpool.user.user.model.Profile
import linkpool.user.user.port.`in`.GetUserUseCase

class DeleteLinkUseCaseTest: BehaviorSpec({

  val getUserUseCase = mockk<GetUserUseCase>()
  val linkPort = mockk<LinkPort>()
  val deleteLinkUseCase = DeleteLinkService(getUserUseCase, linkPort)

  Given("링크 삭제 서비스") {
    val uid = "사용자 ID"
    val linkId = 1L

    When("링크를 삭제하면") {
      val user = User(
        id = 1L,
        uid = "사용자 ID",
        profile = Profile(
          nickname = "대훈",
          jobGroupId = 2L
        )
      )
      val link = Link(
        id = 2L,
        creatorId = 1,
        title = "링크 제목",
        describe = "링크 설명",
        url = "링크 URL",
        folderId = 0L,
        inflowType = InflowType.BRING
      )

      every { getUserUseCase.getByUid(uid) } returns user
      every { linkPort.getById(linkId) } returns link

      deleteLinkUseCase.delete(uid, linkId)

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

      every { getUserUseCase.getByUid(uid) } returns user
      every { linkPort.getById(linkId) } returns link

      Then("NotAuthorizedForDataException이 throw되어야 함") {
        shouldThrow<NotAuthorizedForDataException> {
          deleteLinkUseCase.delete(uid, linkId)
        }
      }
    }

    When("폴더에 속한 링크를 일괄 삭제하면") {
      val folderId = 1L
      every { linkPort.deleteBatchByFolderId(folderId) } just runs

      deleteLinkUseCase.deleteByFolder(folderId)

      Then("폴더에 속한 링크가 일괄 삭제되어야 함") {
        verify(exactly = 1) { linkPort.deleteBatchByFolderId(folderId) }
      }
    }
  }
})