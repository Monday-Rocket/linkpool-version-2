package linkpool.folderlink.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.`in`.DeleteFolderUseCase
import linkpool.link.folder.port.out.FolderPort
import linkpool.link.folderlink.service.DeleteFolderLinkService
import linkpool.link.link.event.LinkBatchEventPort
import linkpool.user.user.model.User
import linkpool.user.user.service.GetUserService

class DeleteFolderLinkServiceTest : BehaviorSpec({

  @MockK
  val userUseCase = mockk<GetUserService>()

  @MockK
  val folderPort = mockk<FolderPort>()

  @MockK
  val deleteFolderUseCase = mockk<DeleteFolderUseCase>()

  @MockK
  val linkBatchEvent = mockk<LinkBatchEventPort>()


  @InjectMockKs
  val deleteFolderLinkService = DeleteFolderLinkService(folderPort, userUseCase, deleteFolderUseCase, linkBatchEvent)

  Given("폴더에 속한 링크 삭제") {
    every { userUseCase.getByUid(any()) } answers { User(id = 1L, uid = "") }
    every { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    justRun { deleteFolderUseCase.delete(any()) }
    justRun { linkBatchEvent.processDeleteBatch(any()) }
    When("폴더에 속한 링크삭제 함수를 호출할 경우") {
      deleteFolderLinkService.delete("", 1L)
      Then("해당 함수들이 호출된다") {
        verify(exactly = 1) {
          folderPort.getById(any())
          userUseCase.getByUid(any())
          linkBatchEvent.processDeleteBatch(any())
          deleteFolderUseCase.delete(any())
        }
      }
    }
    And("만약 내폴더가 아니라면") {
      clearAllMocks()
      every { userUseCase.getByUid(any()) } answers { User(id = 2L, uid = "") }
      every { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
      When("폴더에 속한 링크삭제 함수를 호출할 경우") {
        val exception = shouldThrow<NotAuthorizedForDataException> {
          deleteFolderLinkService.delete("", 1L)
        }
        Then("에러가 반환된다.") {
          exception.message shouldBe "해당 정보에 접근권한이 없습니다."
          verify(exactly = 1) {
            folderPort.getById(any())
            userUseCase.getByUid(any())
          }
          verify(exactly = 0) {
            linkBatchEvent.processDeleteBatch(any())
            deleteFolderUseCase.delete(any())
          }
        }
      }
    }
  }
})