package linkpool.user.port.out

interface UserAuthPort {
    suspend fun setUserId(uid: String, id: Long)

}