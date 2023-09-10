package linkpool.adapters.link.r2dbc.repository

import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface LinkRepository : ReactiveSortingRepository<LinkR2dbcEntity, Long> {
    @Query("SELECT * FROM link WHERE user_id = :userId AND deleted = 0 ORDER BY created_date_time DESC LIMIT :limit OFFSET :offset")
    suspend fun findByUserIdOrderByCreatedDateTimeDesc(userId: Long, limit: Int, offset: Int): List<LinkR2dbcEntity>
    @Query("SELECT count(*) FROM link WHERE user_id = :userId AND deleted = 0")
    suspend fun countByUserId(userId: Long): Long

    @Modifying
    @Query("UPDATE link l SET l.deleted = true WHERE l.user_id = :userId")
    suspend fun deleteBatchByUserId(@Param("userId") userId: Long)

    @Modifying
    @Query("UPDATE link l SET l.deleted = true WHERE l.folder_id = :folderId")
    suspend fun deleteBatchByFolderId(@Param("folderId") folderId: Long)


}
