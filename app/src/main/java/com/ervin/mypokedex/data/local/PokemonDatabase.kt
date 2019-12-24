package com.ervin.mypokedex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ervin.mypokedex.data.local.entity.*

@Database(
    entities = [PokemonEntity::class,
        PokemonTypeElementEntity::class,
        TypeElementEntity::class,
        TypeElementSuperEffectiveEntity::class,
        TypeElementNotEffectiveEntity::class,
        TypeElementNoDamageEntity::class
    ]
    , version = 2
    , exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun dao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

