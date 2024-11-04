package fr.floz.epicurian.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.floz.epicurian.data.services.local.CuisineEntity
import fr.floz.epicurian.data.services.local.ElementCuisineCrossRef
import fr.floz.epicurian.data.services.local.ElementDao
import fr.floz.epicurian.data.services.local.ElementEntity

@Database(entities = [ElementEntity::class, CuisineEntity::class, ElementCuisineCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun elementDao(): ElementDao

}