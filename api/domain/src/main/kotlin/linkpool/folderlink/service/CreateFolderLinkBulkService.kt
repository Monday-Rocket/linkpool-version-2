package linkpool.folderlink.service

import linkpool.common.DomainComponent
import linkpool.folder.port.`in`.CreateFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.folderlink.port.`in`.BulkCreateRequest
import linkpool.folderlink.port.`in`.CreateFolderLinkBulkUseCase
import linkpool.link.port.`in`.CreateLinkUseCase
import linkpool.link.port.`in`.SaveLinkRequest
import javax.transaction.Transactional

@DomainComponent
@Transactional
class CreateFolderLinkBulkService(
    private val createFolderUseCase: CreateFolderUseCase,
    private val folderPort: FolderPort,
    private val createLinkUseCase: CreateLinkUseCase
): CreateFolderLinkBulkUseCase {

    override suspend fun createBulk(userId: Long, request: BulkCreateRequest) {
        createFolderUseCase.createBulk(userId, request.newFolders)

        val folders = folderPort.findAllByUserIdAndNameIn(userId,
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