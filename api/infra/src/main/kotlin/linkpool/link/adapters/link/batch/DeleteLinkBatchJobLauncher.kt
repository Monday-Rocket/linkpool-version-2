package linkpool.link.adapters.link.batch

import linkpool.link.adapters.link.r2dbc.repository.LinkRepository

class DeleteLinkBatchJobLauncher(private val linkRepository: LinkRepository) {

  suspend fun process(folderId: Long) {
    linkRepository.deleteBatchByFolderId(folderId)
  }
}