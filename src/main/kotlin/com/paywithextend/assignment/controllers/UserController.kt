package com.paywithextend.assignment.controllers

import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.repositories.UserRepositoryInterface
import com.paywithextend.assignment.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController/*(value = "/user")*/
class UserController(
    private val userService:UserService,
    private val userRepository: /*UserRepositoryLocal*/UserRepositoryInterface
) {

    @GetMapping(value = ["/users"],
                produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsers(): Mono<List<UserModel>> = userRepository.getAllUsers()
            .map { it.map { user -> user.toSafeUser() } }

    @GetMapping(value = ["/user/{userId}"],
                produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable userId:String): Mono<UserModel> = userRepository.findById(UUID.fromString(userId))
            .map { it.toSafeUser() }

    @PostMapping(value = ["/user"])
    @ResponseStatus(HttpStatus.CREATED)
    fun addUser(@RequestBody userModel: UserModel): Mono<Boolean> = userService.addUser(userModel)

    @PutMapping(value = ["/user"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun replaceUser(@RequestBody userModel: UserModel): Mono<Boolean> = userService.replaceUser(userModel)

    @DeleteMapping(value = ["/user/{userId}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeUser(@PathVariable userId:String): Mono<Boolean> = userService.removeUser(UUID.fromString(userId))


    private fun UserModel.toSafeUser():UserModel = this.copy(
            extendPassword = "*****",
            extendToken = extendToken?.copy(value = "***.***.***"))
}
