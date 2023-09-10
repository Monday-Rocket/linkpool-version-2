package linkpool.link.adapters.folder.r2dbc.repository

import linkpool.link.adapters.folder.r2dbc.entity.FolderR2dbcEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface FolderRepository : ReactiveCrudRepository<FolderR2dbcEntity, Long> {

  suspend fun findAllByOwnerIdAndNameIn(ownerId: Long, names: List<String>): List<FolderR2dbcEntity>
  suspend fun existsByOwnerIdAndName(ownerId: Long, name: String): Boolean
  suspend fun findAllByOwnerId(ownerId: Long): List<FolderR2dbcEntity>

  @Modifying
  @Query("UPDATE folder f SET f.deleted = true WHERE f.owner_id = :ownerId")
  suspend fun softDeleteBatch(ownerId: Long)

  @Modifying
  @Query("UPDATE folder f SET f.deleted = true WHERE f.folder_id = :folderId")
  suspend fun softDelete(folderId: Long)
}