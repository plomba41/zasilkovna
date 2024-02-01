package com.plomba.zasilkovna.jobtask.domain.usecase.http

import com.plomba.zasilkovna.jobtask.domain.usecase.BaseUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.UseCaseException
import retrofit2.HttpException
import java.io.IOException

abstract class HttpUseCase<RETURN_VALUE, ARGUMENT> : BaseUseCase<RETURN_VALUE, ARGUMENT>() {

    abstract suspend fun performHttpUseCase(arg: ARGUMENT?):RETURN_VALUE?

    override suspend fun performUseCase(arg: ARGUMENT?):RETURN_VALUE?{
        try {
            return performHttpUseCase(arg)
        } catch (e: HttpException){
            throw UseCaseException(e)
        } catch (e: IOException){
            throw UseCaseException(e)
        }
    }
}