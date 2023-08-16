package linkpool.query.linkfolder.r2dbc

import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.link.model.InflowType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class LinkFolderRepository(
    private val databaseClient: DatabaseClient,
) {
  suspend fun findVisiblePageByUserIdIn(userIds: List<Long>, loggedInUserId: Long, pageable: Pageable): Mono<Page<LinkR2dbcEntity>> {
      return databaseClient.sql(
        """
            SELECT 
                l.*
            FROM 
                link as l
            INNER JOIN 
                folder as f ON l.folder_id = f.id
            WHERE l.user_id in :userIds
            AND f.visible = true
            AND l.inflow_type = :inflowType
            AND l.id NOT IN
            (
                SELECT
                    r.target_id 
                FROM 
                    report as r
                WHERE 
                    r.target_type = :targetType
                AND 
                    r.reporter_id = :loggedInUserId
            )
            ORDER BY l.created_date_time DESC
    """
    ).bind("userIds", userIds)
          .bind("inflowType", 0)
          .bind("targetType", 1)
          .bind("loggedInUserId", loggedInUserId)
          .fetch().all()
        .map { row -> convert(row) }
        .collectList()
        .map { list -> PageImpl(list, pageable, list.size.toLong()) }

  }


  private fun convert(row: MutableMap<String, Any>): LinkR2dbcEntity {
      return LinkR2dbcEntity(
          id = row["id"].toString().toLong(),
          userId = row["userId"].toString().toLong(),
          folderId = row["folderId"]?.toString()?.toLong(),
          url = row["url"].toString(),
          title = row["url"]?.toString(),
          image = row["image"]?.toString(),
          describe = row["describe"]?.toString(),
          inflowType = row["inflowType"].toString().toInt(),
          createdDateTime = LocalDateTime.parse(row["linkCreatedDateTime"].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z[UTC]'"))
      )
  }
}