package linkpool.folderlink.service

import linkpool.common.DomainComponent
import linkpool.folder.port.`in`.CreateFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.folderlink.port.`in`.BulkCreateRequest
import linkpool.folderlink.port.`in`.CreateFolderLinkBulkUseCase
import linkpool.link.port.`in`.CreateLinkUseCase
import linkpool.link.port.`in`.SaveLinkRequest
import linkpool.user.port.`in`.GetUserUseCase

@DomainComponent
class CreateFolderLinkBulkService(
    private val getUserUseCase: GetUserUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val folderPort: FolderPort,
    private val createLinkUseCase: CreateLinkUseCase
): CreateFolderLinkBulkUseCase {

    override suspend fun createBulk(uid: String, request: BulkCreateRequest) {

        val user = getUserUseCase.getByUid(uid)

        createFolderUseCase.createBulk(user, request.newFolders)

        val folders = folderPort.findAllByUserIdAndNameIn(user.id,
            request.newLinks.filter { it.folderName != null }.map {
            it.folderName!!
        })

        createLinkUseCase.createBulk(user, request.newLinks.map {
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