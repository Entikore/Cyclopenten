package de.entikore.cyclopenten.domain.usecases.base

interface SuspendBaseUseCaseWithOutParams<R> {
    suspend operator fun invoke(): R
}
