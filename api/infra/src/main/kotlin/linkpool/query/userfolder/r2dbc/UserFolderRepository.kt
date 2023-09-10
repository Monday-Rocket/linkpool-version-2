package linkpool.query.userfolder.r2dbc

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
class UserFolderRepository(
  private val databaseClient: DatabaseClient,
) {
  suspend fun findFoldersByOwnerId(ownerId: Long): List<UserFolderListResult> {
    return databaseClient.sql(
      """
        SELECT f.*, count(l.folder_id) as link_count
        FROM folder AS f
        INNER JOIN link AS l ON f.id = l.folder_id
        WHERE f.visible IS true
            AND f.owner_id = :ownerId
        GROUP BY f.id
      """
    )
      .bind("ownerId", ownerId)
      .fetch().all()
      .map { row -> convert(row) }
      .collectList()
      .awaitSingle()
  }

  private fun convert(row: MutableMap<String, Any>): UserFolderListResult {
    return UserFolderListResult(
      id = row["id"].toString().toLong(),
      name = row["name"].toString(),
      thumbnail = row["image"].toString(),
      visible = row["visible"].toString().toBoolean(),
      linkCount = row["link_count"]?.toString()!!.toInt(),
      createdDateTime = (row["created_date_time"] as ZonedDateTime).toLocalDateTime()
    )
  }
}