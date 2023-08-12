package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateFolderNameException
import linkpool.folder.model.Folder
import linkpool.folder.port.`in`.CreateFolderUseCase
import linkpool.folder.port.`in`.SaveFolderRequest
import linkpool.folder.port.out.FolderPort
import linkpool.user.model.User
import linkpool.user.port.`in`.GetUserUseCase
import java.time.LocalDateTime

@DomainComponent
class CreateFolderService(
  private val getUserUseCase: GetUserUseCase,
  private val folderPort: FolderPort,
) : CreateFolderUseCase {

  override suspend fun create(uid: String, request: SaveFolderRequest) {
    val user = getUserUseCase.getByUid(uid)

    if (folderPort.existsByUserIdAndName(user.id, request.name)) {
      throw DuplicateFolderNameException()
    }

    val folder = Folder(userId = user.id, name = request.name, visible = request.visible ?: false)

    folderPort.save(folder)
  }

  override suspend fun createBulk(user: User, request: List<SaveFolderRequest>) {
    val existingFolders = folderPort.findAllByUserIdAndNameIn(user.id, request.map { it.name })

    val notExisting = request.filterNot { requestEach -> existingFolders.any { existingEach ->
      requestEach.name == existingEach.name
    } }

    folderPort.saveAll(notExisting.map {
      Folder(
        userId = user.id,
        name = it.name,
        visible = it.visible ?: false,
        createdDateTime = it.createdAt ?: LocalDateTime.now()
      )
    })
  }
}