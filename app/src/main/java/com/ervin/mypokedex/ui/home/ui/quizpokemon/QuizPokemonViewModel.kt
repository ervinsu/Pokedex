package com.ervin.mypokedex.ui.home.ui.quizpokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.model.SimplePokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuizPokemonViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Who's that Pokemon ?"
    }

    val text: LiveData<String> = _text

    private val _highScore = MutableLiveData<Int>().apply {
        value = 0
    }

    fun increaseScore(){
        _highScore.value = _highScore.value!!.plus(20)
    }

    val highScore = Transformations.switchMap(_highScore) {score->
        MutableLiveData<String>().apply {
            value = "Current score $score"
        }
    }

    //ticking time
    private val _tickIn = MutableLiveData<Int>().apply {
        value = 4
    }

    fun getTickIn():MutableLiveData<Int>{
        return _tickIn
    }

    fun updateTickIn(){
        _tickIn.value = _tickIn.value!!.minus(1)
    }

    var isTickShown = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun setTickShown(boolean: Boolean){
        isTickShown.value = boolean
    }


    //total answer question
    private val _totalAnsweredQuestion =  MutableLiveData<Int>().apply {
        value = 1
    }

    fun getTotalAnsweredQuestion():MutableLiveData<Int>{
        return _totalAnsweredQuestion
    }

    fun increaseTotalAnswer(){
        _totalAnsweredQuestion.value = _totalAnsweredQuestion.value!!.plus(1)
    }


    //term to win
    val termWin= Transformations.switchMap(_highScore) {score->
        MutableLiveData<String>().apply {
            value = when (score) {
                in 50..70 -> "Okay, you got this"
                in 70..80 -> "just a little longer"
                in 80..99 -> "You almost have it!"
                100 -> "Congrats! You are qualified to become Pokemon Master!"
                else -> ""
            }
        }
    }


    //random-ed pokemon
    private lateinit var _randomPokemon: SimplePokemonModel

    suspend fun setRandomPokemon(){
        _randomPokemon = pokemonRepository.getRandomLocalSimplePokemon()
    }

    fun getRandomPokemon():Flow<SimplePokemonModel> = flow{
        emit(_randomPokemon)
    }


    //listAnswer pokemon

    private val _listAnswerPokemon = MutableLiveData<List<String>>()

    val listAnswerPokemon = Transformations.switchMap(_listAnswerPokemon) {list->
        MutableLiveData<List<String>>().apply {
            value = list
        }
    }

    suspend fun setRandomAnswer(){
        _listAnswerPokemon.value = pokemonRepository.getRandomAnswer()
    }

}