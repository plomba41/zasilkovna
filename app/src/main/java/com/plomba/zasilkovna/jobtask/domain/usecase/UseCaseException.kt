package com.plomba.zasilkovna.jobtask.domain.usecase

class UseCaseException(val e: Exception): Throwable(e) {
}