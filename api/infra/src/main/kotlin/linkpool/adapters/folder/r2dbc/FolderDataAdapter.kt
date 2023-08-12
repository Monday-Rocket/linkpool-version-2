package linkpool.adapters.folder.r2dbc

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import linkpool.adapters.folder.r2dbc.entity.FolderR2dbcEntity
import linkpool.adapters.folder.r2dbc.repository.FolderRepository
import linkpool.folder.model.Folder
import linkpool.folder.model.Thumbnail
import linkpool.folder.port.out.FolderPort
import org.springframework.stereotype.Service

@Service
class FolderDataAdapter(private val folderRepository: FolderRepository) : FolderPort {

  override suspend fun save(folder: Folder): Folder {

    val folderR2dbcEntity = FolderR2dbcEntity(
      id = folder.id,
      userId = folder.userId,
      name = folder.name,
      visible = folder.visible,
      image = folder.thumbnail?.image
    )
    return toModel(folderRepository.save(folderR2dbcEntity).awaitSingle())
  }

  override suspend fun saveAll(folders: List<Folder>) {
    val folderEntities = folders.map { folder ->
      FolderR2dbcEntity(
        id = folder.id,
        userId = folder.userId,
        name = folder.name,
        visible = folder.visible,
        image = folder.thumbnail?.image
      )
    }
    folderRepository.saveAll(folderEntities).awaitSingle()
  }

  override suspend fun getById(id: Long): Folder =
    toModel(folderRepository.findById(id).awaitSingle())

  override suspend fun findAllByUserIdAndNameIn(userId: Long, names: List<String>): List<Folder> =
    toModel(folderRepository.findAllByUserIdAndNameIn(userId, names))

  override suspend fun existsByUserIdAndName(userId: Long, name: String): Boolean =
    folderRepository.existsByUserIdAndName(userId, name)

  override suspend fun findAllByUserId(userId: Long): List<Folder> =
    toModel(folderRepository.findAllByUserId(userId))

  override suspend fun softDeleteAll(userId: Long): Unit =
    folderRepository.softDeleteBatch(userId)

  override suspend fun softDelete(folderId: Long) {
    folderRepository.softDelete(folderId)
  }

  override suspend fun update(folder: Folder) {
    val folderR2dbcEntity = FolderR2dbcEntity(
      id = folder.id,
      userId = folder.userId,
      name = folder.name,
      visible = folder.visible,
      image = folder.thumbnail?.image
    )
    folderRepository.save(folderR2dbcEntity).awaitSingle()
  }


  private fun toModel(entities: List<FolderR2dbcEntity>): List<Folder> {
    return entities.map {
      toModel(it)
    }
  }

  private fun toModel(entity: FolderR2dbcEntity): Folder {
    return Folder(
      id = entity.id,
      userId = entity.userId,
      name = entity.name,
      visible = entity.visible,
      thumbnail = entity.image ?.let { toModel(it) },
      createdDateTime = entity.createdDateTime,
      modifiedDateTime = entity.modifiedDateTime
    )
  }

  private fun toModel(image: String): Thumbnail {
    return Thumbnail(
      image = image
    )
  }
}