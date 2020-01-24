package com.ervin.mypokedex.ui.home.ui.quizpokemon

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.App
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.model.SimplePokemonModel
import com.ervin.mypokedex.utils.setDisable
import kotlinx.android.synthetic.main.quiz_answer_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizPokemonAnswerAdapter(private val context: Context, private val viewModel: QuizPokemonViewModel) :
    RecyclerView.Adapter<QuizPokemonAnswerAdapter.AnswerPokemonViewHolder>() {
    private var currSimplePokemon = SimplePokemonModel(0, "", "")
    private var positionUsersAnswer: Int = 0
    private var isAnswered = false
    private var listAnswers: List<String> = arrayListOf()

    companion object{
        const val TAG = "answerAdapter"
    }

    fun setListPokemon(_listAnswers: List<String>){
        listAnswers = _listAnswers
        notifyItemRangeChanged(0,listAnswers.size)
    }

    fun setAnswerSimplePokemon(_currSimplePokemon: SimplePokemonModel) {
        currSimplePokemon = _currSimplePokemon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerPokemonViewHolder {
        return AnswerPokemonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.quiz_answer_item,
                parent,
                false
            )
        )
     }

    override fun getItemCount(): Int {
        return listAnswers.size
    }

    override fun onBindViewHolder(holder: AnswerPokemonViewHolder, position: Int) {
        if(isAnswered){
            holder.itemView.rb_answer.isChecked = false
            holder.itemView.tv_answer.text = listAnswers[position]
            holder.itemView.rb_answer.setDisable()
            holder.itemView.container_answer_item.setDisable()
            if(currSimplePokemon.pokemonName == listAnswers[position])
                holder.itemView.tv_answer.setTextColor(ContextCompat.getColor(context,R.color.quizTrue))
            else
                holder.itemView.tv_answer.setTextColor(ContextCompat.getColor(context,R.color.quizFalse))

            if(positionUsersAnswer == position) {
                if(currSimplePokemon.pokemonName == listAnswers[position])
                    viewModel.increaseScore()
                holder.itemView.rb_answer.isChecked = true
                Log.d(
                    TAG,
                    "${currSimplePokemon.pokemonName} ${listAnswers[position]} $positionUsersAnswer $position"
                )
            }else
                holder.itemView.rb_answer.isChecked = false

            if(position == listAnswers.size-1){
                if(viewModel.getTotalAnsweredQuestion().value == App().getTotalQuestion()){
                    Toast.makeText(App().getContext(), "Question Complete!", Toast.LENGTH_LONG).show()
                    return
                }
                val timer = object: CountDownTimer(3000, 1000){
                    override fun onFinish() {
                        Log.d(TAG, "createNeW RANDOM")
                        isAnswered = false
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.setRandomAnswer()
                            viewModel.increaseTotalAnswer()
                            viewModel.setTickShown(false)
                        }
                    }

                    override fun onTick(p0: Long) {
                        viewModel.updateTickIn()
                    }

                }
                viewModel.setTickShown(true)
                timer.start()
            }
        }else{
            holder.itemView.tv_answer.text = listAnswers[position]
            holder.itemView.rb_answer.setOnClickListener {
                positionUsersAnswer = position
                isAnswered = true
                holder.itemView.rb_answer.isChecked = false
                notifyDataSetChanged()
            }

            holder.itemView.container_answer_item.setOnClickListener {
                positionUsersAnswer = position
                isAnswered = true
                holder.itemView.rb_answer.isChecked = false
                notifyDataSetChanged()
            }
        }
    }

    class AnswerPokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}