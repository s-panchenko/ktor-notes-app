package me.spanchenko.module

import me.spanchenko.config.DatabaseInitializer
import me.spanchenko.service.NoteService
import me.spanchenko.service.NoteServiceImpl
import org.koin.dsl.module

val appModule = module {
    single { DatabaseInitializer() }
    single<NoteService> { NoteServiceImpl() }
}
