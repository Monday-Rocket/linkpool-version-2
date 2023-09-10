package linkpool.link.folderlink.port.`in`

interface CreateFolderLinkBulkUseCase {
    suspend fun createBulk(userId: Long, request: BulkCreateRequest)
}