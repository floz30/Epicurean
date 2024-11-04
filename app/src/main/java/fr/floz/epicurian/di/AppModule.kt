package fr.floz.epicurian.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.floz.epicurian.data.AppDatabase
import fr.floz.epicurian.data.implementation.ElementsRepositoryImpl
import fr.floz.epicurian.data.services.local.ElementDao
import fr.floz.epicurian.data.services.remote.OverpassApi
import fr.floz.epicurian.data.services.remote.OverpassApiImpl
import fr.floz.epicurian.domain.repo.ElementsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "epicurean.db")
            .createFromAsset("database/epicurean.db")
            // Supprime les données présentes en bdd lors d'une mise à jour de version
            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRestaurantDao(database: AppDatabase): ElementDao {
        return database.elementDao()
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(elementDao: ElementDao): ElementsRepository {
        return ElementsRepositoryImpl(elementDao)
    }

    @Provides
    @Singleton
    fun provideOsm(client: HttpClient): OverpassApi {
        return OverpassApiImpl(client)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

}
