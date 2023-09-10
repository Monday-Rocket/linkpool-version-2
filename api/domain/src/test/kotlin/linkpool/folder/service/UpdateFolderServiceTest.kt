package linkpool.folder.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.`in`.UpdateFolderRequest
import linkpool.link.folder.port.out.FolderPort
import linkpool.link.folder.service.UpdateFolderService
import linkpool.user2.user.model.User
import linkpool.user2.user.service.GetUserService

class UpdateFolderServiceTest : BehaviorSpec({

  @MockK
  val userUseCase = mockk<GetUserService>()

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val updateFolderService = UpdateFolderService(userUseCase, folderPort)

  Given("폴더 정보 갱신") {
    every { userUseCase.getByUid(any()) } answers { User(id = 1L, uid = "") }
    every { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    every { folderPort.update(any(), any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    When("폴더 정보 갱신을 요청할 경우") {
      updateFolderService.update("", 1L, UpdateFolderRequest())
      Then("폴더가 갱신된다.") {
        verify(exactly = 1) {
          folderPort.getById(any())
          userUseCase.getByUid(any())
          folderPort.update(any(), any())
        }
      }
    }
    And("먼역 내폴더가 아니라면") {
      clearAllMocks()
      every { userUseCase.getByUid(any()) } answers { User(id = 2L, uid = "") }
      every { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
      When("폴더 정보 갱신을 요청할 경우") {
        val exception = shouldThrow<NotAuthorizedForDataException> {
          updateFolderService.update("", 1L, UpdateFolderRequest())
        }
        Then("에러가 반환된다.") {
          exception.message shouldBe "해당 정보에 접근권한이 없습니다."
          verify(exactly = 1) {
            userUseCase.getByUid(any())
            folderPort.getById(any())
          }
          verify(exactly = 0) {
            folderPort.update(any(), any())
          }
        }
      }
    }
  }
})