package linkpool.user.user.port.out

interface UserAuthPort {
    suspend fun setUserId(uid: String, id: Long)

}