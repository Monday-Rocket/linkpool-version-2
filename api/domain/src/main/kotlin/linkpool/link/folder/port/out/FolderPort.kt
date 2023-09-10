package linkpool.link.folder.port.out

import linkpool.link.folder.model.Folder

interface FolderPort {
    suspend fun save(folder: Folder): Folder
    suspend fun saveAll(folders: List<Folder>)
    suspend fun getById(id: Long): Folder
    suspend fun findAllByOwnerIdAndNameIn(ownerId: Long, names: List<String>): List<Folder>
    suspend fun existsByOwnerIdAndName(ownerId: Long, name: String): Boolean
    suspend fun findAllByOwnerId(ownerId: Long): List<Folder>
    suspend fun softDeleteAll(ownerId: Long)
    suspend fun softDelete(folderId: Long)
    suspend fun update(folder: Folder)
}