package linkpool.link.adapters.link.r2dbc.repository

import linkpool.link.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface LinkRepository : ReactiveSortingRepository<LinkR2dbcEntity, Long> {
    @Query("SELECT * FROM link WHERE creator_id = :creatorId AND deleted = 0 ORDER BY created_date_time DESC LIMIT :limit OFFSET :offset")
    suspend fun findByCreatorIdOrderByCreatedDateTimeDesc(creatorId: Long, limit: Int, offset: Int): List<LinkR2dbcEntity>
    @Query("SELECT count(*) FROM link WHERE creator_id = :creatorId AND deleted = 0")
    suspend fun countByCreatorId(creatorId: Long): Long

    @Modifying
    @Query("UPDATE link l SET l.deleted = true WHERE l.creator_id = :creatorId")
    suspend fun deleteBatchByCreatorId(@Param("creatorId") creatorId: Long)

    @Modifying
    @Query("UPDATE link l SET l.deleted = true WHERE l.folder_id = :folderId")
    suspend fun deleteBatchByFolderId(@Param("folderId") folderId: Long)

}
