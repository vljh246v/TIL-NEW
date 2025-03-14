package com.graffiti.pad.day20250302

import kotlinx.coroutines.delay

suspend fun main() {
    val userService = UserService()
    println(userService.findUser(1L, null))
}


interface Continuation {
    suspend fun resumeWith(data: Any?)
}

class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    private abstract class FindUserContinuation() : Continuation {
        var label = 0
        var profile: Profile? = null
        var image: Image? = null
    }

    suspend fun findUser(userId: Long, continuation: Continuation?): UserDto {
        val sm = continuation as? FindUserContinuation ?: object : FindUserContinuation() {
            override suspend fun resumeWith(data: Any?) {
                when (label) {
                    0 -> {
                        profile = data as Profile
                        label = 1
                    }

                    1 -> {
                        image = data as Image
                        label = 2
                    }
                }
                findUser(userId, this)
            }
        }

        when (sm.label) {
            0 -> {
                // 0단계 - 초기 시작
                printWithThread("findUser start")
                userProfileRepository.findProfile(userId, sm)
            }

            1 -> {
                // 1단계 - 1차 중단 후 재시작
                printWithThread("findUser image start")
                userImageRepository.findImage(sm.profile!!, sm)
            }
        }
        return UserDto(sm.profile!!, sm.image!!)
    }
}

data class UserDto(val profile: Profile, val image: Image)

class UserProfileRepository {
    suspend fun findProfile(userId: Long, continuation: Continuation): Unit {
        delay(100L)
        continuation.resumeWith(Profile())
    }

}

class Profile

class UserImageRepository {
    suspend fun findImage(profile: Profile, continuation: Continuation): Unit {
        delay(100L)
        continuation.resumeWith(Image())
    }
}

class Image