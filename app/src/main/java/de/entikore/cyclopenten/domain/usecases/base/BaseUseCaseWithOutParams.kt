package de.entikore.cyclopenten.domain.usecases.base

interface BaseUseCaseWithOutParams<R> {
    operator fun invoke(): R
}
