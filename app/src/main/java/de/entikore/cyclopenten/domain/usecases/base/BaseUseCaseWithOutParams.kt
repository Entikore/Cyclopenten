package de.entikore.cyclopenten.domain.usecases.base

interface BaseUseCaseWithOutParams<R> {
    suspend operator fun invoke(): R
}
