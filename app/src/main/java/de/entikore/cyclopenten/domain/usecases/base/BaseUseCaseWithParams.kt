package de.entikore.cyclopenten.domain.usecases.base

interface BaseUseCaseWithParams<P, R> {
    suspend operator fun invoke(params: P): R
}
