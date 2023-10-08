package linkpool.folder.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import linkpool.link.folder.port.out.FolderPort
import linkpool.link.folder.service.DeleteFolderService

class DeleteFolderServiceTest : BehaviorSpec({

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val deleteFolderService = DeleteFolderService(folderPort)

  Given("폴더 삭제") {
    justRun { folderPort.softDelete(any()) }
    When("폴더 삭제를 요청할 경우 하면") {
      deleteFolderService.delete(1L)
      Then("폴더가 삭제된다.") {
        verify { folderPort.softDelete(any()) }
      }
    }
  }
})