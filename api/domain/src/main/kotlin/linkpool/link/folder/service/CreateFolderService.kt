package linkpool.link.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateFolderNameException
import linkpool.link.folder.model.Folder
import linkpool.link.folder.port.`in`.CreateFolderUseCase
import linkpool.link.folder.port.`in`.SaveFolderRequest
import linkpool.link.folder.port.out.FolderPort
import java.time.LocalDateTime
import javax.transaction.Transactional

@DomainComponent
@Transactional
class CreateFolderService(
  private val folderPort: FolderPort,
) : CreateFolderUseCase {

  override suspend fun create(ownerId: Long, request: SaveFolderRequest) {
    if (folderPort.existsByOwnerIdAndName(ownerId, request.name)) {
      throw DuplicateFolderNameException()
    }

    val folder = Folder(ownerId = ownerId, name = request.name, visible = request.visible ?: false)

    folderPort.save(folder)
  }

  override suspend fun createBulk(ownerId: Long, request: List<SaveFolderRequest>) {
    val existingFolders = folderPort.findAllByOwnerIdAndNameIn(ownerId, request.map { it.name })

    val notExisting = request.filterNot { requestEach -> existingFolders.any { existingEach ->
      requestEach.name == existingEach.name
    } }

    folderPort.saveAll(notExisting.map {
      Folder(
        ownerId = ownerId,
        name = it.name,
        visible = it.visible ?: false,
        createdDateTime = it.createdAt ?: LocalDateTime.now()
      )
    })
  }
}