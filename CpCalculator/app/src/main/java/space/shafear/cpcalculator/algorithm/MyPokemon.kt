package algorithm

import android.util.Log
import space.shafear.cpcalculator.algorithm.*
import java.util.*

data class MyPokemon(var pokemon: Pokemon, var level: Int, var hp: Int, var cp: Int, val stardust: Int){

    data class Results(var results: HashSet<Pair<Stats, Result>>){
        fun getMaxCp(): Int?{
            val element = results.sortedBy { obj -> obj.second.cp }.lastOrNull() ?: return null
            return element.second.cp
        }

        fun getMinCp(): Int?{
            val element = results.sortedBy { obj -> obj.second.cp }.firstOrNull() ?: return null
            return element.second.cp
        }

        fun getMaxHp(): Int?{
            val element = results.sortedBy { obj -> obj.second.hp }.lastOrNull() ?: return null
            return element.second.hp
        }

        fun getMinHp(): Int?{
            val element = results.sortedBy { obj -> obj.second.hp }.firstOrNull() ?: return null
            return element.second.hp
        }
    }

    fun calculateMaxCpAndHp(percents: Double): Results{
        val resultList = HashSet<Pair<Stats, Result>>()
        for(iv in calculateStatistics(percents))
            resultList.add(iv to pokemon.calculateMaxCpAndHp(iv))
        return Results(resultList)
    }

    fun calculateCpAndHp(level: Int, percents: Double): Results{
        val resultList = HashSet<Pair<Stats, Result>>()
        for(iv in calculateStatistics(percents))
            resultList.add(iv to pokemon.calculateHpAndCp(iv, level))
        return Results(resultList)
    }

    private fun calculateStatistics(percents: Double): HashSet<Stats>{
        for(i in 3..25){
            val scale = i.toDouble()/100.0
            val res = calculateStatistics(percents, scale)
            if(res.isNotEmpty())
                return res
        }
        return hashSetOf()
    }

    private fun calculateStatistics(percents: Double, scale: Double): HashSet<Stats> {
        val resultList = HashSet<Stats>()
        for(lvl in stardustToLevel.findLast { obj -> obj.component1()==stardust }!!.component2().first
                ..stardustToLevel.findLast { obj -> obj.component1()==stardust }!!.component2().second) {
            for (stamina in 0..15) {
                for (attack in 0..15)
                    for (defence in 0..15) {
                        val individualValues = Stats(attack = attack, defence = defence, stamina = stamina)
                        individualValues.setIndividualValues()
                        val calculatedCp = calculateCp(pokemon.baseStats, individualValues, getCpMultiplierByLevel(lvl-1))
                        val calculatedHp = calculateHp(pokemon.baseStats, individualValues, getCpMultiplierByLevel(lvl-1))
                        if (hp == calculatedHp) {
                            val calculatedCpMax = calculateCp(pokemon.baseStats, individualValues, cPmultiplierByLevel[level*2-1])
                            val calculatedCpMin = calculateCp(pokemon.baseStats, individualValues, cPmultiplierByLevel.first())
                            val delta = calculatedCpMax - calculatedCpMin
                            if(cp-calculatedCpMin in (percents-scale)*delta..(percents+scale)*delta) {
                                resultList.add(individualValues)
                            }
                        }
                    }
            }
        }
        return resultList
    }
}

