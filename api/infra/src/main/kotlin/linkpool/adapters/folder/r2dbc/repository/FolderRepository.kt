package linkpool.adapters.folder.r2dbc.repository

import linkpool.adapters.folder.r2dbc.entity.FolderR2dbcEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface FolderRepository : ReactiveCrudRepository<FolderR2dbcEntity, Long> {

  suspend fun findAllByUserIdAndNameIn(userId: Long, names: List<String>): List<FolderR2dbcEntity>
  suspend fun existsByUserIdAndName(userId: Long, name: String): Boolean
  suspend fun findAllByUserId(userId: Long): List<FolderR2dbcEntity>

  @Modifying
  @Query("UPDATE folder f SET f.deleted = true WHERE f.user_id = :userId")
  suspend fun softDeleteBatch(userId: Long)

  @Modifying
  @Query("UPDATE folder f SET f.deleted = true WHERE f.folder_id = :folderId")
  suspend fun softDelete(userId: Long)
}