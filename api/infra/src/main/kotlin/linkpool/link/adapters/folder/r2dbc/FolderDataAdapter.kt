package linkpool.link.adapters.folder.r2dbc

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import linkpool.link.adapters.folder.r2dbc.entity.FolderR2dbcEntity
import linkpool.link.adapters.folder.r2dbc.repository.FolderRepository
import linkpool.link.folder.model.Folder
import linkpool.link.folder.model.Thumbnail
import linkpool.link.folder.port.out.FolderPort
import org.springframework.stereotype.Service

@Service
class FolderDataAdapter(private val folderRepository: FolderRepository) : FolderPort {

  override suspend fun save(folder: Folder): Folder {

    val folderR2dbcEntity = FolderR2dbcEntity(
      id = folder.id,
      ownerId = folder.ownerId,
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
        ownerId = folder.ownerId,
        name = folder.name,
        visible = folder.visible,
        image = folder.thumbnail?.image
      )
    }
    folderRepository.saveAll(folderEntities).awaitSingle()
  }

  override suspend fun getById(id: Long): Folder =
    toModel(folderRepository.findById(id).awaitSingle())

  override suspend fun findAllByOwnerIdAndNameIn(ownerId: Long, names: List<String>): List<Folder> =
    toModel(folderRepository.findAllByOwnerIdAndNameIn(ownerId, names))

  override suspend fun existsByOwnerIdAndName(ownerId: Long, name: String): Boolean =
    folderRepository.existsByOwnerIdAndName(ownerId, name)

  override suspend fun findAllByOwnerId(ownerId: Long): List<Folder> =
    toModel(folderRepository.findAllByOwnerId(ownerId))

  override suspend fun softDeleteAll(ownerId: Long): Unit =
    folderRepository.softDeleteBatch(ownerId)

  override suspend fun softDelete(folderId: Long) {
    folderRepository.softDelete(folderId)
  }

  override suspend fun update(folder: Folder) {
    val folderR2dbcEntity = FolderR2dbcEntity(
      id = folder.id,
      ownerId = folder.ownerId,
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
      ownerId = entity.ownerId,
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