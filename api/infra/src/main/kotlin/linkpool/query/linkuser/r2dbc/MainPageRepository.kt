package linkpool.query.linkuser.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.user.jobgroup.port.`in`.JobGroupResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
class MainPageRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun findAll(loggedInUserId: Long, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
                SELECT 
                    l.id                    as linkId,
                    l.url                   as url, 
                    l.title                 as title,
                    l.image                 as image,
                    l.folder_id             as folderId,
                    l.describe              as linkDescribe,
                    l.created_date_time     as linkCreatedDateTime,
                    u.id                    as creatorId,
                    u.nickname              as nickname,
                    u.profile_image         as profileImage,
                    jg.id                   as jobGroupId,
                    jg.name                 as jobGroupName
                FROM 
                    link l
                JOIN 
                    folder f ON l.folder_id = f.id
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
                    user u ON u.id = l.creator_id
                JOIN 
                    job_group jg ON u.job_group_id = jg.id
                WHERE l.deleted = 0
                ORDER BY l.created_date_time DESC
                LIMIT :limit
                OFFSET :offset
    """
    )
            .bind("visible", 1)
            .bind("inflowType", 0)
            .bind("targetType", 1)
            .bind("loggedInUserId", loggedInUserId)
            .bind("limit", pageable.pageSize)
            .bind("offset", pageable.pageSize * pageable.pageNumber)
          .fetch().all()
          .map { row -> convert(row) }
          .collectList()
          .awaitSingle()

    val count =  databaseClient.sql(
        """
                SELECT 
                    l.*
                FROM 
                    link l
                JOIN 
                    folder f ON l.folder_id = f.id
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
                    user u ON u.id = l.creator_id
                JOIN 
                    job_group jg ON u.job_group_id = jg.id
                WHERE l.deleted = 0
    """
    ).bind("visible", 1)
        .bind("inflowType", 0)
        .bind("targetType", 1)
        .bind("loggedInUserId", loggedInUserId)
        .fetch().all().count().awaitSingle()

    return PageImpl(list, pageable, count)
  }

    suspend fun findByJobGroup(jobGroupId: Long, loggedInUserId: Long, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
            SELECT l.id                as linkId,
                   l.url               as url,
                   l.title             as title,
                   l.image             as image,
                   l.folder_id         as folderId,
                   l.describe          as linkDescribe,
                   l.created_date_time as linkCreatedDateTime,
                   u.id                as creatorId,
                   u.nickname          as nickname,
                   u.profile_image     as profileImage,
                   jg.id               as jobGroupId,
                   jg.name             as jobGroupName
            FROM link l
                     INNER JOIN
                 folder f ON l.folder_id = f.id
                     INNER JOIN
                 user u on l.creator_id = u.id
                     INNER JOIN
                 job_group jg on u.job_group_id = jg.id
                     LEFT OUTER JOIN
                 report ru ON (
                        (l.creator_id = ru.target_id OR l.id = ru.target_id)
                        AND ru.reporter_id = :loggedInUserId
                 )
            WHERE
              ru.id IS NULL
              AND u.job_group_id = :jobGroupId
              AND f.visible = :visible
              AND l.inflow_type = :inflowType
              AND l.deleted = 0
            ORDER BY l.created_date_time DESC
            LIMIT :limit
            OFFSET :offset
    """)
            .bind("loggedInUserId", loggedInUserId)
            .bind("jobGroupId", jobGroupId)
            .bind("visible", 1)
            .bind("inflowType", 0)
            .bind("limit", pageable.pageSize)
            .bind("offset", pageable.pageSize * pageable.pageNumber)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .awaitSingle()

        val count = databaseClient.sql(
            """
            SELECT 
                l.*
            FROM link l
                     INNER JOIN
                 folder f ON l.folder_id = f.id
                     INNER JOIN
                 user u on l.creator_id = u.id
                     INNER JOIN
                 job_group jg on u.job_group_id = jg.id
                     LEFT OUTER JOIN
                 report ru 
                    ON (
                        (l.creator_id = ru.target_id OR l.id = ru.target_id)
                         and ru.reporter_id = :loggedInUserId
                    )
            WHERE
              ru.id IS NULL
              AND u.job_group_id = :jobGroupId
              AND f.visible = :visible
              AND l.inflow_type = :inflowType
              AND l.deleted = 0
    """)
            .bind("loggedInUserId", loggedInUserId)
            .bind("jobGroupId", jobGroupId)
            .bind("visible", 1)
            .bind("inflowType", 0)
            .fetch().all().count().awaitSingle()

        return PageImpl(list, pageable, count)
    }

    private fun convert(row: MutableMap<String, Any>): LinkWithUserResult {
        return LinkWithUserResult(
            id = row["linkId"].toString().toLong(),
            creator = UserResult(
                row["creatorId"].toString().toLong(),
                row["nickname"]?.toString(),
                JobGroupResponse(
                    row["jobGroupId"].toString().toLong(),
                    row["jobGroupName"].toString(),
                ),
                row["profileImage"].toString()
            ),
            url = row["url"].toString(),
            title = row["title"]?.toString(),
            image = row["image"]?.toString(),
            folderId = row["folderId"]?.toString()?.toLong(),
            describe = row["linkDescribe"]?.toString(),
            createdDateTime = (row["linkCreatedDateTime"] as ZonedDateTime).toLocalDateTime()
        )
    }
}