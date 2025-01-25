package bd.srabbijan.callerid.application

import android.app.Application
import bd.srabbijan.callerid.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class CallerIdApp : Application(){


    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@CallerIdApp)
            modules(listOf(appModule))
        }
    }
}