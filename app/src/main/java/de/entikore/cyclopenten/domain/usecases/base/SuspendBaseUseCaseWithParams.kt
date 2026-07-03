package de.entikore.cyclopenten.domain.usecases.base

interface SuspendBaseUseCaseWithParams<P, R> {
    suspend operator fun invoke(params: P): R
}
