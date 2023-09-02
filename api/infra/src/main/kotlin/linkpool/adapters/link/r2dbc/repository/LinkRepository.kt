package linkpool.adapters.link.r2dbc.repository

import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface LinkRepository : ReactiveSortingRepository<LinkR2dbcEntity, Long> {

    suspend fun countByFolderId(folderId: Long): Long
    suspend fun findByFolderIdOrderByCreatedDateTimeDesc(folderId: Long, limit: Int, offset: Int): List<LinkR2dbcEntity>
    @Query("SELECT * FROM link WHERE user_id = :userId AND deleted = 0 ORDER BY created_date_time DESC LIMIT :limit OFFSET :offset")
    suspend fun findByUserIdOrderByCreatedDateTimeDesc(userId: Long, limit: Int, offset: Int): List<LinkR2dbcEntity>
    @Query("SELECT count(*) FROM link WHERE user_id = :userId AND deleted = 0")
    suspend fun countByUserId(userId: Long): Long
    suspend fun findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId: Long): LinkR2dbcEntity

//    @Modifying
//    @Query("UPDATE LinkJpaEntity l SET l.deleted = true WHERE l.userId = :userId")
//    suspend fun deleteBatchByUserId(@Param("userId") userId: Long)

    @Query("UPDATE LinkJpaEntity l SET l.deleted = true WHERE l.folderId = :folderId")
    suspend fun deleteBatchByFolderId(@Param("folderId") folderId: Long)


}
