package linkpool.link.folder.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.out.FolderPort

class DeleteFolderServiceTest : BehaviorSpec({

  @MockK
  val folderPort = mockk<FolderPort>()

  @InjectMockKs
  val deleteFolderService = DeleteFolderService(folderPort)

  Given("폴더 삭제") {
    coEvery { folderPort.getById(any()) } returns Folder(ownerId = 1L, name = "hwjeon")
    coJustRun { folderPort.softDelete(any()) }
    When("폴더 삭제를 요청할 경우 하면") {
      deleteFolderService.delete(1L, 1L)
      Then("폴더가 삭제된다.") {
        coVerify { folderPort.softDelete(any()) }
      }
    }
  }
})