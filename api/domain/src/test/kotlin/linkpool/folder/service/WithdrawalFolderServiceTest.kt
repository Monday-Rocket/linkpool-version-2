package linkpool.folder.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import linkpool.link.folder.port.out.FolderPort
import linkpool.link.folder.service.WithdrawalFolderService
import linkpool.user2.user.model.UserSignedOutEvent

class WithdrawalFolderServiceTest : BehaviorSpec({

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val withdrawalFolderService = WithdrawalFolderService(folderPort)

  Given("회원 탈퇴시 폴더 삭제") {
    justRun { folderPort.softDeleteAll(any()) }
    When("폴더 삭제를 요청할 경우") {
      withdrawalFolderService.deleteAll(UserSignedOutEvent(1L))
      Then("폴더가 삭제된다") {
        verify { folderPort.softDeleteAll(any()) }
      }
    }
  }
})