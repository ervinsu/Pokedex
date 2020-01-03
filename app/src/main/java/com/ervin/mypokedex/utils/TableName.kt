package com.ervin.mypokedex.utils

class TablePokemon {
    companion object {
        const val PokemonTable: String = "PokemonTable"
        const val PokemonID: String = "pokemonId"
        const val PokemonName: String = "pokemonName"
        const val PokemonSprites: String = "pokemonSpritesUrl"
        const val PokemonSpeed: String = "pokemonSpeed"
        const val PokemonDefense: String = "pokemonDefense"
        const val PokemonSpAttack: String = "pokemonSpAttack"
        const val PokemonSpDefense: String = "pokemonSpDefense"
        const val PokemonAttack: String = "pokemonAttack"
        const val PokemonHp: String = "pokemonHp"
        const val PokemonWeight: String = "pokemonWeight"
        const val PokemonBaseExp: String = "pokemonBaseExp"
        const val PokemonHeight: String = "pokemonHeight"
        const val PokemonDesc: String = "pokemonDesc"
    }
}

class TableTypeElement {
    companion object {
        const val TypeTable: String = "TypeTable"
        const val TypeName: String = "typeClassName"
        const val TypeID: String = "typeClassId"
        const val TypeUrl: String = "typeUrl"
        const val TypeColor: String = "typeClassColor"
    }
}

class TablePokemonTypeElement {
    companion object {
        const val PokemonTypeElementTable: String = "PokemonTypeElementTable"
        const val PokemonTypeElementPokemonID: String = "pokePokemonId"
        const val PokemonTypeElementID: String = "pokeTypeClassId"
    }
}


class TableTypeElementSuperEffectiveTo{
    companion object{
        const val TypeID: String ="typeClassId"
        const val TypeSuperEffectiveTableTo: String ="TypeEffectiveTableTo"
        const val TypeIDSuperEffectiveTo: String ="typeIDEffectiveTo"
    }
}


class TableTypeElementNotEffectiveTo {
    companion object {
        const val TypeID: String = "typeClassId"
        const val TypeNotEffectiveTableTo: String = "TypeNotEffectiveTableTo"
        const val TypeIDNotEffectiveTo: String = "typeIDNotEffectiveTo"
    }
}

class TableTypeElementNoDamageTo{
    companion object{
        const val TypeID: String ="typeClassId"
        const val TypeNoDamageTableTo: String ="typeNoDamageTableTo"
        const val TypeIDNoDamageTo: String ="typeIDNoDamageTo"
    }
}

class TableTypeElementNoDamageFrom{
    companion object{
        const val TypeID: String ="typeClassId"
        const val TypeNoDamageTableFrom: String ="typeNoDamageTableFrom"
        const val TypeIDNoDamageFrom: String ="typeIDNoDamageFrom"
    }
}

class TableTypeElementSuperEffectiveFrom{
    companion object{
        const val TypeID: String ="typeClassId"
        const val TypeSuperEffectiveTableFrom: String ="TypeEffectiveTableFrom"
        const val TypeIDSuperEffectiveFrom: String ="typeIDEffectiveFrom"
    }
}


class TableTypeElementNotEffectiveFrom{
    companion object{
        const val TypeID: String ="typeClassId"
        const val TypeNotEffectiveTableFrom: String ="TypeNotEffectiveTableFrom"
        const val TypeIDNotEffectiveFrom: String ="typeIDNotEffectiveFrom"
    }
}


class TablePokemonEvolveTo{
   companion object{
       const val PokemonEvolveTable: String = "pokemonEvolveTable"
       const val PokemonId: String = "pokemonId"
       const val PokemonIdEvolveTo: String = "pokemonIdEvolveTo"
       const val PokemonEvolveRequire:String = "pokemonEvolveRequire"
   }
}