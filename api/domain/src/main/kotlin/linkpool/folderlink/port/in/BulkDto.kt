package linkpool.folderlink.port.`in`

import linkpool.folder.port.`in`.SaveFolderRequest
import linkpool.link.port.`in`.SaveLinkWithFolderNameRequest

data class BulkCreateRequest(
    val newFolders: List<SaveFolderRequest>,
    val newLinks: List<SaveLinkWithFolderNameRequest>,
)