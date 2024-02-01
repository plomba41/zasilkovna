package com.plomba.zasilkovna.jobtask.domain.usecase

import android.content.Context
import com.plomba.zasilkovna.jobtask.R
import com.plomba.zasilkovna.jobtask.common.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

abstract class BaseUseCase<RETURN_VALUE, ARGUMENT> {

    @Throws(UseCaseException::class)
    abstract suspend fun performUseCase(arg: ARGUMENT?):RETURN_VALUE?

    inline fun <reified T> getClassName(obj: T?):String{
        return T::class.java.name
    }

    operator fun invoke(arg: ARGUMENT? = null): Flow<Resource<RETURN_VALUE>> = flow {
        try {
            emit(Resource.Loading<RETURN_VALUE>())
            val returnValue = performUseCase(arg)
            if(returnValue != null){
                emit(Resource.Success<RETURN_VALUE>(returnValue))
            } else {
                emit(Resource.Error<RETURN_VALUE>( message = "null object"))
            }

        } catch(e: UseCaseException) {
            emit(Resource.Error<RETURN_VALUE>(e.localizedMessage ?: e.javaClass.name))
        }
    }
}