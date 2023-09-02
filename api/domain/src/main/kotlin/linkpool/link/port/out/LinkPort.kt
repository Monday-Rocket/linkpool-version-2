package linkpool.link.port.out

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.model.Link

interface LinkPort {
    suspend fun findById(id: Long): Link
    suspend fun save(link: Link): Link
    suspend fun saveAll(links: List<Link>): List<Link>
    suspend fun findPageByUserIdOrderByCreatedDateTimeDesc(userId: Long, pageable: LinkPoolPageRequest): LinkPoolPage<Link>
    suspend fun delete(link: Link)
    suspend fun update(link: Link)
    suspend fun deleteBatchByUserId(userId: Long)
    suspend fun deleteBatchByFolderId(folderId: Long)

}