package com.ervin.mypokedex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.TypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityFrom
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityTo

@Database(
    entities = [PokemonEntity::class,
        PokemonTypeElementEntity::class,
        TypeElementEntity::class,
        TypeElementSuperEffectiveEntityTo::class,
        TypeElementNotEffectiveEntityTo::class,
        TypeElementNoDamageEntityTo::class,
        TypeElementSuperEffectiveEntityFrom::class,
        TypeElementNotEffectiveEntityFrom::class,
        TypeElementNoDamageEntityFrom::class
    ]
    , version = 4
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

