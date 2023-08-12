package linkpool.link.port.out

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.exception.DataNotFoundException
import linkpool.link.model.Link

suspend fun LinkPort.getById(id: Long): Link = findById(id)
    ?: throw DataNotFoundException("링크가 존재하지 않습니다. id: $id")

interface LinkPort {
    suspend fun findById(id: Long): Link?
    suspend fun save(link: Link): Link
    suspend fun saveAll(links: List<Link>): List<Link>
    suspend fun findAllByFolderId(folderId: Long): List<Link>
    suspend fun findAllByUserId(userId: Long): List<Link>
    suspend fun countByFolderId(folderId: Long): Int
    suspend fun countByUserIdAndFolderIdIsNull(userId: Long): Int
    suspend fun findPageByFolderIdOrderByCreatedDateTimeDesc(folderId: Long, pageable: LinkPoolPageRequest): LinkPoolPage<Link>
    suspend fun existsByUserIdAndUrl(userId: Long, url: String): Boolean
    suspend fun findPageByUserIdOrderByCreatedDateTimeDesc(id: Long, pageable: LinkPoolPageRequest): LinkPoolPage<Link>
    suspend fun findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId: Long): Link?
    suspend fun delete(link: Link)
    suspend fun update(link: Link)

    suspend fun deleteBatchByUserId(userId: Long)
    suspend fun deleteBatchByFolderId(folderId: Long)

}