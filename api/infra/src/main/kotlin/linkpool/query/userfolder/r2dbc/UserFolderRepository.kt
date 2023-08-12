package linkpool.query.userfolder.r2dbc

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserFolderRepository(
  private val databaseClient: DatabaseClient,
) {
  suspend fun findFoldersByUserId(userId: Long): List<UserFolderListResult> {
    return databaseClient.sql(
      """
        SELECT f.*, count(l.folder_id) as link_count
        FROM folder AS f
        INNER JOIN link AS l ON f.id = l.folder_id
        WHERE f.visible IS true
            AND f.user_id = :userId
        GROUP BY f.id
      """
    )
      .bind("userId", userId)
      .fetch().all()
      .map { row -> convert(row) }
      .collectList()
      .awaitSingle()
  }

  suspend fun findFoldersByUserUid(uid: String): List<UserFolderListResult> {
    return databaseClient.sql(
      """
        SELECT f.*, count(l.folder_id) as link_count
        FROM folder AS f
        INNER JOIN link AS l ON f.id = l.folder_id
        WHERE f.visible IS true
            AND f.user_id in (
                SELECT u.id  
                FROM user u
                WHERE u.uid = :uid 
            )
        GROUP BY f.id;
      """
    )
      .bind("uid", uid)
      .fetch().all()
      .map { row -> convert(row) }
      .collectList()
      .awaitSingle()
  }

  private fun convert(row: MutableMap<String, Any>): UserFolderListResult {
    return UserFolderListResult(
      id = row["id"].toString().toLong(),
      name = row["nickname"].toString(),
      thumbnail = row["thumbnail"].toString(),
      visible = row["visible"].toString().toBoolean(),
      linkCount = row["link_count"]?.toString()!!.toInt(),
      createdDateTime = LocalDateTime.parse(row["createdDateTime"].toString())
    )
  }
}