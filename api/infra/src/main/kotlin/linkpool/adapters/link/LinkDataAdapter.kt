package linkpool.adapters.link

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.adapters.link.r2dbc.repository.LinkRepository
import linkpool.link.model.InflowType
import linkpool.link.model.Link
import linkpool.link.port.out.LinkPort
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service


@Service
class LinkDataAdapter(
    private val linkRepository: LinkRepository,
) : LinkPort {

    override suspend fun findById(id: Long): Link {
        return toModel(linkRepository.findById(id).awaitSingle())
    }
    override suspend fun save(link: Link): Link {
        return toModel(linkRepository.save(toJpa(link)).awaitSingle())
    }

    override suspend fun saveAll(links: List<Link>): List<Link> {
        return toModel(linkRepository.saveAll(toJpa(links))
            .collectList()
            .awaitSingle()
        )
    }

    override suspend fun findPageByFolderIdOrderByCreatedDateTimeDesc(folderId: Long, linkPoolPageRequest: LinkPoolPageRequest): LinkPoolPage<Link> {
        val pageRequest = toSpringPageRequest(linkPoolPageRequest)
        val list = linkRepository.findByFolderIdOrderByCreatedDateTimeDesc(folderId, linkPoolPageRequest.page_no, linkPoolPageRequest.page_size)
        val count = linkRepository.countByFolderId(folderId)
        return toModel(
                PageImpl(list, pageRequest, count)
        )
    }

    override suspend fun findPageByUserIdOrderByCreatedDateTimeDesc(userId: Long, linkPoolPageRequest: LinkPoolPageRequest): LinkPoolPage<Link> {
        val pageRequest = toSpringPageRequest(linkPoolPageRequest)
        val list = linkRepository.findByUserIdOrderByCreatedDateTimeDesc(userId, linkPoolPageRequest.page_size, linkPoolPageRequest.page_size * linkPoolPageRequest.page_no)
        val count = linkRepository.countByUserId(userId)
        return toModel(PageImpl(list, pageRequest, count))
    }
    override suspend fun findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId: Long): Link? {
        return toModel(linkRepository.findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId))
    }

    override suspend fun delete(link: Link) {
        linkRepository.save(toJpa(link))
    }

    override suspend fun update(link: Link) {
        linkRepository.save(toJpa(link))
    }

    override suspend fun deleteBatchByUserId(userId: Long) {
//        return linkRepository.deleteBatchByUserId(userId)
        TODO()
    }

    override suspend fun deleteBatchByFolderId(folderId: Long) {
//        return linkRepository.deleteBatchByFolderId(folderId)
        TODO()
    }

    private fun toModel(pages: Page<LinkR2dbcEntity>): LinkPoolPage<Link> =
        LinkPoolPage(
            page_no = pages.number,
            page_size = pages.size,
            total_count = pages.totalElements,
            total_page = pages.totalPages,
            contents = toModel(pages.content)
        )

    private fun toModel(entities: List<LinkR2dbcEntity>) =
        entities.map {
            toModel(it)
        }

    private fun toJpa(models: List<Link>) =
        models.map {
            toJpa(it)
        }

    private fun toModel(entity: LinkR2dbcEntity) =
        Link(
            id = entity.id,
            userId = entity.userId,
            folderId = entity.folderId,
            url = entity.url,
            title = entity.title,
            image = entity.image,
            describe = entity.describe,
            inflowType = when(entity.inflowType){
                0 -> InflowType.CREATE
                1 -> InflowType.BRING
                else -> InflowType.CREATE
            },
            createdDateTime = entity.createdDateTime,
            modifiedDateTime = entity.modifiedDateTime
        )

    private fun toJpa(model: Link) =
        LinkR2dbcEntity(
            id = model.id,
            userId = model.userId,
            folderId = model.folderId,
            url = model.url,
            title = model.title,
            image = model.image,
            describe = model.describe,
            inflowType = model.inflowType.ordinal,
            createdDateTime = model.createdDateTime
        )

    private fun toSpringPageRequest(linkPoolPageRequest: LinkPoolPageRequest) =
        PageRequest.of(linkPoolPageRequest.page_no, linkPoolPageRequest.page_size)

}