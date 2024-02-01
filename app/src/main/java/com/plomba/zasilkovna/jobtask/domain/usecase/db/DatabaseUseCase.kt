package com.plomba.zasilkovna.jobtask.domain.usecase.db

import android.os.TransactionTooLargeException
import com.plomba.zasilkovna.jobtask.domain.usecase.BaseUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.UseCaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

abstract class DatabaseUseCase<RETURN_VALUE, ARGUMENT> : BaseUseCase<RETURN_VALUE, ARGUMENT>() {

    abstract suspend fun performDbUseCase(arg: ARGUMENT?):RETURN_VALUE?

    override suspend fun performUseCase(arg: ARGUMENT?):RETURN_VALUE? {
        var ret: RETURN_VALUE? = null
        withContext(Dispatchers.IO) {
            try {
                ret = performDbUseCase(arg)
            } catch (e: SQLException) {
                throw UseCaseException(e)
            } catch (e: TransactionTooLargeException) {
                throw UseCaseException(e)
            }
        }
        return ret
    }
}