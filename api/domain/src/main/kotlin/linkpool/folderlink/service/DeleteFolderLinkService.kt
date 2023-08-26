package linkpool.folderlink.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.folder.port.`in`.DeleteFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.folderlink.port.`in`.DeleteFolderLinkUseCase
import javax.transaction.Transactional

@DomainComponent
@Transactional
class DeleteFolderLinkService(
    private val folderPort: FolderPort,
    private val deleteFolderUseCase: DeleteFolderUseCase,
) : DeleteFolderLinkUseCase{

  override suspend fun delete(userId: Long, folderId: Long) {
    val folder = folderPort.getById(folderId)

    if(!folder.isOwner(userId)) {
      throw NotAuthorizedForDataException()
    }

    deleteFolderUseCase.delete(userId, folderId)

    /**
     * 링크 삭제 처리를 별도의 프로세스에서 진행한다.
     *
     * 폴더 당 최대 링크수 제한이 없으므로, 링크건수가 많을 경우 링크삭제 프로세스에서 병목이 발생될 수 있다.
     * 링크개수 제한정책에 따라서 'Async + 개별스레드' 또는 별도의 애플리케이션에서 동작하도록 개발이 필요하다.
     * @author : 전현우
     * @since : 1.0 (2023.06.20)
     */

    /**
     * 인프라 단에서 논블록킹으로 구현한다.
     * 이벤트 형태로 구현할 필요 없어보임
     * 배치 이벤트를 인프라로보내는 것이 아닌, delete 됬다라는 이벤트를 링크 도메인 으로 넘겨서 거기서 컨숨.
     * 케밥 개힘듬
     * Folder domain -> call delete action to link domain -> action delete batch in link infra
     * linkPort.processDeleteBatch("")
     * @author : 전현우
     * @since : 2.0 (2023.07.15)
     *
     */

//    linkBatchEvent.processDeleteBatch(folderId)
  }
}