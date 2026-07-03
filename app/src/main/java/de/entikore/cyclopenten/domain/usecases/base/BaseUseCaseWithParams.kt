package de.entikore.cyclopenten.domain.usecases.base

interface BaseUseCaseWithParams<P, R> {
    operator fun invoke(params: P): R
}
