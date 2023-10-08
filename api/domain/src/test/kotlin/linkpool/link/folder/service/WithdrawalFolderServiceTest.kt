package linkpool.link.folder.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import linkpool.link.folder.port.out.FolderPort
import linkpool.user.user.model.UserSignedOutEvent

class WithdrawalFolderServiceTest : BehaviorSpec({

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val withdrawalFolderService = WithdrawalFolderService(folderPort)

  Given("회원 탈퇴시 폴더 삭제") {
    coJustRun { folderPort.softDeleteAll(any()) }
    When("폴더 삭제를 요청할 경우") {
      withdrawalFolderService.deleteAll(UserSignedOutEvent(1L))
      Then("폴더가 삭제된다") {
        coVerify { folderPort.softDeleteAll(any()) }
      }
    }
  }
})