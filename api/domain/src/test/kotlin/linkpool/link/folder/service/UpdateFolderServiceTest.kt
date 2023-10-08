package linkpool.link.folder.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.`in`.UpdateFolderRequest
import linkpool.link.folder.port.out.FolderPort
import linkpool.support.spec.afterRootTest
import linkpool.user.user.model.User
import linkpool.user.user.service.GetUserService

class UpdateFolderServiceTest : BehaviorSpec({

  @MockK
  val userUseCase = mockk<GetUserService>()

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val updateFolderService = UpdateFolderService(folderPort)

  Given("폴더 정보 갱신") {
    coEvery { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    coEvery { folderPort.update(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    When("폴더 정보 갱신을 요청할 경우") {
      updateFolderService.update(1L, 1L, UpdateFolderRequest())
      Then("폴더가 갱신된다.") {
        coVerify(exactly = 1) {
          folderPort.getById(any())
          folderPort.update(any())
        }
      }
    }
  }

  Given("만약 내폴더가 아니라면") {
    coEvery { folderPort.getById(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    When("폴더 정보 갱신을 요청할 경우") {
      val exception = shouldThrow<NotAuthorizedForDataException> {
        updateFolderService.update(2L, 1L, UpdateFolderRequest())
      }
      Then("에러가 반환된다.") {
        exception.message shouldBe "해당 정보에 접근권한이 없습니다."
        coVerify(exactly = 1) {
          folderPort.getById(any())
        }
        coVerify(exactly = 0) {
          folderPort.update(any())
        }
      }
    }
  }

  afterRootTest {
    clearAllMocks()
  }
})