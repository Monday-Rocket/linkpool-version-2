package linkpool.link.folderlink.port.`in`

import linkpool.link.folder.port.`in`.SaveFolderRequest
import linkpool.link.link.port.`in`.SaveLinkWithFolderNameRequest

data class BulkCreateRequest(
    val newFolders: List<SaveFolderRequest>,
    val newLinks: List<SaveLinkWithFolderNameRequest>,
)