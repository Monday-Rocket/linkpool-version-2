package linkpool.query.mainpage.r2dbc

import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.link.port.`in`.LinkWithUserResponse
import linkpool.user.port.`in`.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class MainPageRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun findAll(loggedInUserId: Long, pageable: Pageable): Mono<Page<LinkWithUserResponse>> {
        return databaseClient.sql(
            """
                SELECT 
                    l.id                    as linkId,
                    l.url                   as url, 
                    l.title                 as title,
                    l.image                 as image,
                    l.folder_id             as folderId,
                    l.describe              as linkDescribe,
                    l.created_date_time     as linkCreatedDateTime,
                    u.id                    as userId,
                    u.nickname              as nickname,
                    u.profile_image         as profileImage,
                    jg.id                   as jobGrouopId,
                    jg.name                 as jobGroupName
                FROM 
                    link l
                JOIN 
                    folder f ON l.folderId = f.id
                AND 
                    f.visible = :visible
                AND 
                    l.inflow_type = :inflowType
                AND 
                    l.id NOT IN
                    (
                        SELECT
                            r.target_id 
                        FROM
                            report r
                        WHERE 
                            r.target_type = :targetType
                        AND 
                            r.reporter_Id = :loggedInUserId
                    )
                JOIN 
                    user u ON u.id = l.user_id
                JOIN 
                    job_group jg ON u.job_group_id = jg.id
                ORDER BY l.created_date_time DESC
    """
    ).bind("visible", 1)
          .bind("inflowType", "CREATE")
          .bind("targetType", "LINK")
          .bind("loggedInUserId", loggedInUserId)
          .fetch().all()
          .map { row -> convert(row) }
          .collectList()
          .map { list -> PageImpl(list, pageable, list.size.toLong()) }
  }

    suspend fun findByJobGroup(jobGroupId: Long, loggedInUserId: Long, pageable: Pageable): Mono<Page<LinkWithUserResponse>>{
        return databaseClient.sql(
            """
            SELECT l.id                as linkId,
                   l.url               as url,
                   l.title             as title,
                   l.image             as image,
                   l.folder_id         as folderId,
                   l.describe          as linkDescribe,
                   l.created_date_time as linkCreatedDateTime,
                   u.id                as userId,
                   u.nickname          as nickname,
                   u.profile_image     as profileImage,
                   jg.id               as jobGrouopId,
                   jg.name             as jobGroupName
            FROM link l
                     INNER JOIN
                 folder f ON l.folder_id = f.id
                     INNER JOIN
                 user u on l.user_id = u.id
                     INNER JOIN
                 job_group jg on u.job_group_id = jg.id
                     LEFT OUTER JOIN
                 report ru ON (l.user_id = ru.target_id OR ru.target_id = l.id)
            WHERE
              ru.id IS NULL
              AND ru.reporter_id = :loggedInUserId
              AND u.job_group_id = :jobGroupId
              AND f.visible = :visible
              AND l.inflow_type = :inflowType
            ORDER BY l.created_date_time DESC
    """).bind("loggedInUserId", loggedInUserId)
            .bind("jobGroupId", jobGroupId)
            .bind("visible", 1)
            .bind("inflowType", "CREATE")
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .map { list -> PageImpl(list, pageable, list.size.toLong()) }
    }

    private fun convert(row: MutableMap<String, Any>): LinkWithUserResponse {
      return LinkWithUserResponse(
          id = row["linkId"].toString().toLong(),
          user = UserResponse(
              row["userId"].toString().toLong(),
              row["nickname"].toString(),
              JobGroupResponse(
                  row["jobGrouopId"].toString().toLong(),
                  row["jobGroupName"].toString(),
                  ),
              row["profileImage"].toString()
          ),
          url = row["url"].toString(),
          title = row["itle"].toString(),
          image = row["image"].toString(),
          folderId = row["folderId"].toString().toLong(),
          describe = row["linkDescribe"].toString(),
          createdDateTime = LocalDateTime.parse(row["createdDateTime"].toString())
      )
  }
}