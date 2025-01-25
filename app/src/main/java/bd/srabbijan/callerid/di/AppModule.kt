package bd.srabbijan.callerid.di

import androidx.room.Room
import bd.srabbijan.callerid.data.local.db.AppDatabase
import bd.srabbijan.callerid.data.repositoryImpl.CallerRepositoryImpl
import bd.srabbijan.callerid.domain.repository.CallerRepository
import bd.srabbijan.callerid.presentation.CallerViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "caller_id_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().contactDao() }

    singleOf(::CallerRepositoryImpl).bind<CallerRepository>()
    viewModelOf(::CallerViewModel)
}