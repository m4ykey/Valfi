package com.m4ykey.core

interface UseCase<P, R> {
    suspend operator fun invoke(params : P) : R
}