package space.shafear.cpcalculator.algorithm

import java.util.*

/**
 * Created by micha on 31.07.2016.
 */
object Pokedex {
    val pokemonList = createPokemonList()

    private fun createPokemonList(): ArrayList<Pokemon> {
        val list = ArrayList<Pokemon>()
        for (i in 1..151)
            list.add(getPokemonByNumber(i))
        return list
    }

    fun getPokemon(name: String): Pokemon? {
        return pokemonList.findLast { obj -> obj.name == name }
    }

    fun getPokemonStringList(): ArrayList<String> {
        val result = ArrayList<String>()
        for(element in pokemonList){
                result.add(element.name)
        }
        return result
    }
}