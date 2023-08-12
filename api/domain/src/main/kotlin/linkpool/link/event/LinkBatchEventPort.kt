package linkpool.link.event

interface LinkBatchEventPort {
  suspend fun processDeleteBatch(folderId: Long)
}