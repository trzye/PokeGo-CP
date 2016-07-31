package space.shafear.cpcalculator

import algorithm.MyPokemon
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.triggertrap.seekarc.SeekArc
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
import space.shafear.cpcalculator.algorithm.Pokedex
import space.shafear.cpcalculator.algorithm.stardustToLevel
import java.util.*

class NotificationActivity : Activity() {

    val context = this
    var percent = 0.0
    var pokemon = Pokedex.pokemonList.first()
    var level = 1
    var stardust = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        initStatbar()
    }

    fun calculateResults(view: View){
        val dialogBuilder = AlertDialog.Builder(context)
        try {
            val hp = (findViewById(R.id.hp_edit_text) as EditText).text.toString().toInt()
            val cp = (findViewById(R.id.cp_edit_text) as EditText).text.toString().toInt()
            val myPokemon = MyPokemon(pokemon, level, hp, cp, stardust)
            val results = myPokemon.calculateMaxCpAndHp(percent)
            dialogBuilder
                    .setTitle("Results")
                    .setMessage("Cp: ${results.getMinCp()}~${results.getMaxCp()}/${myPokemon.pokemon.calculateMaxCpAndHp().cp}\n" +
                                "Hp: ${results.getMinHp()}~${results.getMaxHp()}/${myPokemon.pokemon.calculateMaxCpAndHp().hp}")
                    .create().show()
        }
        catch (e: NumberFormatException) {
            Toast.makeText(context, "Input HP and CP!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSpinners() {
        setPokemonSpinner()
        setLevelSpinner()
        setStardustSpinner()
    }

    private fun setStardustSpinner() {
        val stardustSpinner = findViewById(R.id.stardust_spinner) as Spinner
        stardustSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, getStardustStringlist())
        stardustSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                stardust = 200
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stardust = getStardustStringlist()[position].toInt()
            }
        }
    }

    private fun setLevelSpinner() {
        val levelSpinner = findViewById(R.id.level_spinner) as Spinner
        levelSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, getLevelStringList())
        levelSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                level = 1
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                level = position + 1
            }
        }
    }

    private fun setPokemonSpinner() {
        val pokemonSpinner = findViewById(R.id.pokemon_spinner) as Spinner
        pokemonSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, Pokedex.getPokemonStringList())
        pokemonSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                pokemon = Pokedex.pokemonList.first()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pokemon = Pokedex.getPokemon(Pokedex.getPokemonStringList()[position])!!
            }
        }
    }

    private fun  getStardustStringlist(): ArrayList<String> {
        val result = ArrayList<String>()
        for(element in stardustToLevel){
            result.add(element.component1().toString())
        }
        return result
    }

    private fun getLevelStringList(): ArrayList<String> {
        val result = ArrayList<String>()
        for(i in 1..40){
            result.add(i.toString())
        }
        return result
    }


    private fun initStatbar() {
        setPercentListener()
        setSpinners()
    }

    private fun setPercentListener() {
        val seekArc = findViewById(R.id.seekArc) as SeekArc
        percent = seekArc.progress / 100.0
        seekArc.setOnSeekArcChangeListener(object : OnSeekArcChangeListener {
            override fun onProgressChanged(p0: SeekArc?, p1: Int, p2: Boolean) {
                percent = p1 / 100.0
            }

            override fun onStartTrackingTouch(p0: SeekArc?) {
            }

            override fun onStopTrackingTouch(p0: SeekArc?) {
            }

        })
    }
}
