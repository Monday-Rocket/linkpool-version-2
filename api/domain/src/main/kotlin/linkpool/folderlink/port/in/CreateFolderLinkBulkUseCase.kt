package linkpool.folderlink.port.`in`

interface CreateFolderLinkBulkUseCase {
    suspend fun createBulk(uid: String, request: BulkCreateRequest)
}