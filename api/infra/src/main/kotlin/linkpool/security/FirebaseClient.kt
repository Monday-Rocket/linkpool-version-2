package linkpool.security

import com.google.api.core.ApiFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord.UpdateRequest
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class FirebaseClient(
    private val firebaseAuth: FirebaseAuth
) {

    fun getUserDetailsByToken(header: String): Mono<LinkPoolAuthentication> {
        return firebaseAuth.verifyIdTokenAsync(header).toMono()
            .map { token -> LinkPoolAuthentication(token, token.name.toLongOrNull() ?: 0L, token.uid) }
            .onErrorMap { _ -> UnauthorizedException() }
    }

    suspend fun updateFirebaseUserId(uid: String, id: Long) {
        firebaseAuth.updateUserAsync(UpdateRequest(uid).setDisplayName(id.toString()))
    }

    private fun <F : Any?> ApiFuture<F>.toMono(): Mono<F> {
        return Mono.fromCallable {
            try {
                return@fromCallable this.get()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }.subscribeOn(Schedulers.boundedElastic())
    }


}
