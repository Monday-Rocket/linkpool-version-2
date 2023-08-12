package linkpool.adapters.link.batch

import linkpool.adapters.link.r2dbc.repository.LinkRepository

class DeleteLinkBatchJobLauncher(private val linkRepository: LinkRepository) {

  suspend fun process(folderId: Long) {
    linkRepository.deleteBatchByFolderId(folderId)
  }
}