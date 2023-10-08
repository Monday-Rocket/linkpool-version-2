package linkpool.link.folder.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import linkpool.exception.DuplicateFolderNameException
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.`in`.SaveFolderRequest
import linkpool.link.folder.port.out.FolderPort
import linkpool.support.spec.afterRootTest
import linkpool.user.user.service.GetUserService
import java.time.LocalDateTime

class CreateFolderServiceTest : BehaviorSpec({

  @MockK
  val userUseCase = mockk<GetUserService>()

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val createFolderService = CreateFolderService(folderPort)

  Given("폴더 저장") {
    coEvery { folderPort.save(any()) } answers { Folder(ownerId = 1L, name = "hwjeon") }
    coEvery { folderPort.existsByOwnerIdAndName(any(), any()) } answers { false }
    When("폴더 저장함수을 요청할 경우") {
      createFolderService.create(1L, createSaveFolderRequest())
      Then("폴더가 저장된다.") {
        coVerify { folderPort.save(any()) }
      }
    }
  }


  Given("이미 폴더가 존재할 경우") {
    coEvery { folderPort.existsByOwnerIdAndName(any(), any()) } answers { true }
    When("폴더 저장을 요청할 경우") {
      val exception = shouldThrow<DuplicateFolderNameException> {
        createFolderService.create(1L, createSaveFolderRequest())
      }
      Then("예외를 던진다.") {
        exception.message shouldBe "이미 등록된 폴더명입니다."
        coVerify(exactly = 1) {
          folderPort.existsByOwnerIdAndName(any(), any())
        }
        coVerify(exactly = 0) {
          folderPort.save(any())
        }
      }
    }
  }
//    Given("폴더 저장시 이미 존재하는 폴더의 경우") {
////    every { folderPort.existsByUserIdAndName(any(), any()) } answers { true }
////    every { userUseCase.getByUid(any()) } answers { User(id = 1L, uid = "") }
//      When("폴더 저장함수가 호출될 경우 예외가 발생한다.") {
//        val exception = shouldThrow<DuplicateFolderNameException> {
//          createFolderService.create("", SaveFolderRequest("name", true, LocalDateTime.now()))
//        }
//        Then("이때 에러는 NotAuthorizedForDataException 이다.") {
//          exception.message shouldNotBe null
//          exception.message shouldBe "이미 등록된 폴더명입니다."
//          verify(exactly = 1) {
//            folderPort.existsByUserIdAndName(any(), any())
//            userUseCase.getByUid(any())
//          }
//          verify(exactly = 0) {
//            folderPort.existsByUserIdAndName(any(), any())
//          }
//        }
//      }
//    }

  afterRootTest {
    clearAllMocks()
  }
})

private fun createSaveFolderRequest() = SaveFolderRequest("name", true, LocalDateTime.now())