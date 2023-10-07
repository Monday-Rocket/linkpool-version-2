package linkpool.link.adapters.link.event

import linkpool.link.adapters.link.batch.DeleteLinkBatchJobLauncher
import linkpool.link.link.event.LinkBatchEventPort

class LinkBatchEventAdapter(private val linkBatchJobLauncher: DeleteLinkBatchJobLauncher) : LinkBatchEventPort {
  override suspend fun processDeleteBatch(folderId: Long) {
    linkBatchJobLauncher.process(folderId)
  }
}