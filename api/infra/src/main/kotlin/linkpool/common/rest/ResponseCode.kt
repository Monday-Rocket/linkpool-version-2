package linkpool.common.rest

import linkpool.exception.*
import linkpool.security.UnauthorizedException
import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

enum class ResponseCode(
    val statusCode: Int,
    val message: String,
    val httpStatus: HttpStatus,
    val exception: KClass<out DomainException>?
) {

    SUCCESS(0, "", HttpStatus.OK, null),
    NO_RESOURCE(1, "", HttpStatus.NOT_FOUND, DataNotFoundException::class),

    NOT_AUTHORIZED(1000, "회원 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED, UnauthorizedException::class),
    NOT_SIGNED_UP(1001, "회원가입을 완료하지 않은 회원입니다.", HttpStatus.FORBIDDEN, NotSignedUpException::class),
    NOT_AUTHORIZED_FOR_THE_DATA(1002, "해당 정보에 접근권한이 없습니다.", HttpStatus.UNAUTHORIZED, NotAuthorizedForDataException::class),
    NOT_ENOUGH_FOR_SIGNING_UP(1003, "회원가입에 필요한 정보를 모두 입력하지 않았습니다.", HttpStatus.BAD_REQUEST, NotEnoughForSigningUpException::class),
    DUPLICATE_NICKNAME(1004, "이미 존재하는 이름입니다.", HttpStatus.BAD_REQUEST, DuplicateNicknameException::class),
    REPORT_DATA(1005, "신고된 정보입니다.", HttpStatus.BAD_REQUEST, null),

    DUPLICATE_FOLDER_NAME(2000, "이미 등록된 폴더명입니다.", HttpStatus.BAD_REQUEST, DuplicateFolderNameException::class),

    DUPLICATE_LINK_URL(3000, "이미 등록된 url입니다.", HttpStatus.BAD_REQUEST, null),

    DUPLICATE_REPORT(4000, "이미 등록된 신고정보입니다.", HttpStatus.BAD_REQUEST, DuplicateReportException::class),

    HTTPS_MESSAGE_NOT_READABLE(9990, "메시지를 읽을 수 없습니다.", HttpStatus.BAD_REQUEST, null),
    METHOD_ARGUMENT_NOT_VALID(9991, "잘못된 메시지 형식입니다.", HttpStatus.BAD_REQUEST, null),
    ILLEGAL_ARGUMENT(9992, "유효하지 않은 데이터입니다.", HttpStatus.BAD_REQUEST, null);

    companion object {
        fun fromException(e: DomainException): ResponseCode? {
            return values().find { it.exception == e::class }
        }
    }

}
