package linkpool.link.link.event

interface LinkBatchEventPort {
  suspend fun processDeleteBatch(folderId: Long)
}