package linkpool.common.rest

data class ApiResponse<T>(
    val status: Int? = 0,
    val message: String? = "",
    val data: T? = null
) {
    companion object {

        fun error(status: Int, message: String?): ApiResponse<Unit> =
            ApiResponse(status = status, message = message)

        fun error(code: ResponseCode): ApiResponse<Unit> =
            ApiResponse(status = code.statusCode, message = code.message)

        fun error(message: String?): ApiResponse<Unit> =
            ApiResponse(message = message)

        fun <T> success(data: T? = null): ApiResponse<T> = ApiResponse(data = data)
    }
}
