package space.shafear.cpcalculator.algorithm

import space.shafear.cpcalculator.algorithm.cPmultiplierByLevel
import space.shafear.cpcalculator.algorithm.evolutions
import space.shafear.cpcalculator.algorithm.pokemonBaseStats
import java.util.*

/**
 * Created by micha on 31.07.2016.
 */
data class Pokemon(val name: String, val number: Int, val baseStats: Stats, val evolutions: HashSet<Pokemon>) {
    fun calculateMaxCpAndHp(): Result {
        val iv = Stats(15, 15, 15)
        return calculateHpAndCp(iv, 79)
    }

    fun calculateMaxCpAndHp(level: Int): Result {
        val iv = Stats(15, 15, 15)
        return calculateHpAndCp(iv, level)
    }

    fun calculateMaxCpAndHp(iv: Stats): Result {
        return calculateHpAndCp(iv, 79)
    }

    fun calculateHpAndCp(iv: Stats, level: Int): Result {
        val cp = calculateCp(baseStats, iv, getCpMultiplierByLevel(level))
        val hp = calculateHp(baseStats, iv, getCpMultiplierByLevel(level))
        return Result(cp, hp)
    }
}

data class Result(val cp: Int, val hp: Int)

data class Stats(var attack: Int, var defence: Int, var stamina: Int, var sum: Int = attack + defence + stamina) {
    operator fun plus(stats: Stats): Stats {
        return Stats(
                this.attack + stats.attack,
                this.defence + stats.defence,
                this.stamina + stats.stamina
        )
    }

    fun setIndividualValues() {
        if (stamina > 15) stamina = 15
        if (stamina < 0) stamina = 0
        if (defence > 15) defence = 15
        if (defence < 0) defence = 0
        if (attack > 15) attack = 15
        if (attack < 0) attack = 0
        sum = attack + defence + stamina
    }
}

fun getPokemonByNumber(pokemonNumber: Int): Pokemon {
    val index = pokemonNumber - 1
    if (index in 0 until pokemonBaseStats.size)
        return getBasePokemonByIndex(index)
    return getBasePokemonByIndex(0)
}

fun getCpMultiplierByLevel(level: Int): Double {
    if (level in 0 until cPmultiplierByLevel.size)
        return cPmultiplierByLevel[level]
    return cPmultiplierByLevel.first()
}

fun calculateCp(baseStats: Stats, individualValues: Stats, powerUpValue: Double): Int {
    val stats: Stats = baseStats.copy() + individualValues
    var result = Math.floor(
            stats.attack.toDouble()
                    * Math.pow(stats.defence.toDouble(), 0.5)
                    * Math.pow(stats.stamina.toDouble(), 0.5)
                    * Math.pow(powerUpValue, 2.0) / 10).toInt()
    if (result < 10) return 10 else return result
}

fun calculateHp(baseStats: Stats, individualValues: Stats, powerUpValue: Double): Int {
    val stats: Stats = baseStats.copy() + individualValues
    val result = Math.floor(stats.stamina * powerUpValue).toInt()
    if (result < 10) return 10 else return result
}

private fun getBasePokemonByIndex(index: Int): Pokemon {
    val pokemon = pokemonBaseStats[index]
    return Pokemon(
            name = pokemon.component1(),
            number = index + 1,
            baseStats = pokemon.component2(),
            evolutions = getEvolutionsByNumber(index + 1)
    )
}

fun getEvolutionsByNumber(number: Int): HashSet<Pokemon> {
    val pokemonEvolutions = HashSet<Pokemon>()
    val evos = evolutions[(pokemonBaseStats[number - 1].first)]
    if (evos != null)
        for (element in evos) {
            val pokemons = pokemonBaseStats.filter { obj -> obj.first == element }
            val num = pokemonBaseStats.indexOf(pokemons.first())
            pokemonEvolutions.add(getPokemonByNumber(num + 1))
        }
    return pokemonEvolutions
}