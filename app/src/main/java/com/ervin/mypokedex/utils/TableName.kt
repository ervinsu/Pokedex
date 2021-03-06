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