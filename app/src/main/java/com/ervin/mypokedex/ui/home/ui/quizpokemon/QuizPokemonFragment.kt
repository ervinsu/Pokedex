package com.ervin.mypokedex.ui.home.ui.quizpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ervin.mypokedex.R
import com.ervin.mypokedex.databinding.FragmentQuizPokemonBinding
import kotlinx.android.synthetic.main.fragment_quiz_pokemon.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class QuizPokemonFragment : Fragment() {
    private lateinit var job: Job
    private lateinit var root:View
    private var listAnswer:MutableList<String> = arrayListOf()

//    private val quizPokemonViewModel: QuizPokemonViewModel by lazy{
//        val factory: ViewModelFactory =
//            ViewModelFactory.getInstance(this@QuizPokemonFragment.requireActivity().application)
//        return@lazy ViewModelProvider(
//            this@QuizPokemonFragment,
//            factory
//        ).get(QuizPokemonViewModel::class.java)
//    }

    private val quizPokemonViewModel by viewModel<QuizPokemonViewModel>()

    private val adapter: QuizPokemonAnswerAdapter by lazy {
        return@lazy QuizPokemonAnswerAdapter(this@QuizPokemonFragment.requireActivity().applicationContext, quizPokemonViewModel).apply {
            setListPokemon(listAnswer)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()
        val binding: FragmentQuizPokemonBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_pokemon, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = quizPokemonViewModel

        root = binding.root
        root.rv_list_quiz_answer.adapter = adapter

        quizPokemonViewModel.apply {
            CoroutineScope(Dispatchers.Main).launch {
                setRandomAnswer()
            }

            listAnswerPokemon.observe(viewLifecycleOwner, Observer { list->
                if(listAnswer.size!=0) listAnswer = arrayListOf()
                listAnswer.addAll(list)
                    CoroutineScope(job + Dispatchers.Main).launch {

                        setRandomPokemon()

                        getRandomPokemon().collect { simplePokemon ->

                            Glide.with(this@QuizPokemonFragment)
                                .load(simplePokemon.pokemonSpritesUrl)
                                .into(root.iv_quiz_question)
                            root.iv_quiz_question.let { img->
                                SpringAnimation(img, DynamicAnimation.TRANSLATION_Y,0f).apply {
                                    val springForce = SpringForce().apply {
                                        dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
                                    }
                                    spring = springForce
                                }
                            }

                            listAnswer.add(simplePokemon.pokemonName)
                            listAnswer.shuffle()

                            adapter.setAnswerSimplePokemon(simplePokemon)
                            adapter.setListPokemon(listAnswer)
                        }
                    }
            })



        }

        return root
    }
}