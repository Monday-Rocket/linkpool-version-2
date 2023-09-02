package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateFolderNameException
import linkpool.folder.model.Folder
import linkpool.folder.port.`in`.CreateFolderUseCase
import linkpool.folder.port.`in`.SaveFolderRequest
import linkpool.folder.port.out.FolderPort
import java.time.LocalDateTime
import javax.transaction.Transactional

@DomainComponent
@Transactional
class CreateFolderService(
  private val folderPort: FolderPort,
) : CreateFolderUseCase {

  override suspend fun create(userId: Long, request: SaveFolderRequest) {
    if (folderPort.existsByUserIdAndName(userId, request.name)) {
      throw DuplicateFolderNameException()
    }

    val folder = Folder(userId = userId, name = request.name, visible = request.visible ?: false)

    folderPort.save(folder)
  }

  override suspend fun createBulk(userId: Long, request: List<SaveFolderRequest>) {
    val existingFolders = folderPort.findAllByUserIdAndNameIn(userId, request.map { it.name })

    val notExisting = request.filterNot { requestEach -> existingFolders.any { existingEach ->
      requestEach.name == existingEach.name
    } }

    folderPort.saveAll(notExisting.map {
      Folder(
        userId = userId,
        name = it.name,
        visible = it.visible ?: false,
        createdDateTime = it.createdAt ?: LocalDateTime.now()
      )
    })
  }
}