package linkpool.link.folderlink.service

import linkpool.common.DomainComponent
import linkpool.link.folder.port.`in`.CreateFolderUseCase
import linkpool.link.folder.port.out.FolderPort
import linkpool.link.folderlink.port.`in`.BulkCreateRequest
import linkpool.link.folderlink.port.`in`.CreateFolderLinkBulkUseCase
import linkpool.link.link.port.`in`.CreateLinkUseCase
import linkpool.link.link.port.`in`.SaveLinkRequest
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class CreateFolderLinkBulkService(
    private val createFolderUseCase: CreateFolderUseCase,
    private val folderPort: FolderPort,
    private val createLinkUseCase: CreateLinkUseCase
): CreateFolderLinkBulkUseCase {

    override suspend fun createBulk(userId: Long, request: BulkCreateRequest) {
        createFolderUseCase.createBulk(userId, request.newFolders)

        val folders = folderPort.findAllByOwnerIdAndNameIn(userId,
            request.newLinks.filter { it.folderName != null }.map {
            it.folderName!!
        })

        createLinkUseCase.createBulk(userId, request.newLinks.map {
            SaveLinkRequest(
                url = it.url,
                title = it.title,
                describe = it.describe,
                image = it.image,
                folderId = folders.find { folder -> it.folderName == folder.name }?.id,
                createdAt = it.createdAt
            )
        })
    }
}