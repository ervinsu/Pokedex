package com.ervin.mypokedex.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.TypeElementEntity
import com.ervin.mypokedex.utils.TablePokemon
import com.ervin.mypokedex.utils.TablePokemonTypeElement
import com.ervin.mypokedex.utils.TableTypeElement

class SimplePokemonWithTypePojoModel {
     @Embedded
     lateinit var pokemonModel: SimplePokemonModel

     @Relation(
         parentColumn = TablePokemon.PokemonID,
         entity = TypeElementEntity::class,
         entityColumn = TableTypeElement.TypeID,
         associateBy = Junction(
             value = PokemonTypeElementEntity::class,
             parentColumn = TablePokemonTypeElement.PokemonTypeElementPokemonID,
             entityColumn = TablePokemonTypeElement.PokemonTypeElementID
         )
     )
     var typeList: List<TypeElementEntity> = arrayListOf()
 }