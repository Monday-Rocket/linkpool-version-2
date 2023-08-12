package linkpool.folder.port.out

import linkpool.folder.model.Folder

interface FolderPort {
    suspend fun save(folder: Folder): Folder
    suspend fun saveAll(folders: List<Folder>)
    suspend fun getById(id: Long): Folder
    suspend fun findAllByUserIdAndNameIn(userId: Long, names: List<String>): List<Folder>
    suspend fun existsByUserIdAndName(userId: Long, name: String): Boolean
    suspend fun findAllByUserId(userId: Long): List<Folder>
    suspend fun softDeleteAll(userId: Long)
    suspend fun softDelete(folderId: Long)
    suspend fun update(folder: Folder)
}