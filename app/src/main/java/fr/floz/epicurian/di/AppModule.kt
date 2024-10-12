package fr.floz.epicurian.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.floz.epicurian.data.AppDatabase
import fr.floz.epicurian.data.RestaurantDao
import fr.floz.epicurian.data.RestaurantsRepository
import fr.floz.epicurian.data.RestaurantsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "restaurants.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideRestaurantDao(database: AppDatabase): RestaurantDao {
        return database.restaurantDao()
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(restaurantDao: RestaurantDao): RestaurantsRepository {
        return RestaurantsRepositoryImpl(restaurantDao)
    }
}
