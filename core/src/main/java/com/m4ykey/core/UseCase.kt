package com.m4ykey.core

interface UseCase<in P, R> {
    suspend operator fun invoke(params : P) : R
}

interface NoParamsUseCase<R> {
    suspend operator fun invoke() : R
}