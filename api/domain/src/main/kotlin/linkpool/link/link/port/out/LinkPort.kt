package linkpool.link.link.port.out

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.link.model.Link

interface LinkPort {
    suspend fun findById(id: Long): Link?
    suspend fun save(link: Link): Link
    suspend fun saveAll(links: List<Link>): List<Link>
    suspend fun findPageByCreatorIdOrderByCreatedDateTimeDesc(creatorId: Long, pageable: LinkPoolPageRequest): LinkPoolPage<Link>
    suspend fun delete(link: Link)
    suspend fun update(link: Link)
    suspend fun deleteBatchByCreatorId(creatorId: Long)
    suspend fun deleteBatchByFolderId(folderId: Long)

}