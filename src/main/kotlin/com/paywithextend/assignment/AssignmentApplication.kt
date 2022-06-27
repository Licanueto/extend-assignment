package com.paywithextend.assignment

import com.paywithextend.assignment.clients.SignInClient
import com.paywithextend.assignment.controllers.UserController
import com.paywithextend.assignment.repositories.UserRepositoryInterface
import com.paywithextend.assignment.services.UserService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackageClasses = [UserController::class, UserService::class, SignInClient::class, UserRepositoryInterface::class])
class AssignmentApplication

fun main(args: Array<String>) {
	runApplication<AssignmentApplication>(*args)
}
