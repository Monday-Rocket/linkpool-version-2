package linkpool.adapters.link.event

import linkpool.adapters.link.batch.DeleteLinkBatchJobLauncher
import linkpool.link.event.LinkBatchEventPort

class LinkBatchEventAdapter(private val linkBatchJobLauncher: DeleteLinkBatchJobLauncher) : LinkBatchEventPort {
  override suspend fun processDeleteBatch(folderId: Long) {
    linkBatchJobLauncher.process(folderId)
  }
}