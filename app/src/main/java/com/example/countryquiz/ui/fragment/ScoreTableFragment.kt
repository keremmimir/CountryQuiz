package com.example.countryquiz.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.countryquiz.R
import com.example.countryquiz.adapter.ScoreTableAdapter
import com.example.countryquiz.databinding.FragmentScoreTableBinding
import com.example.countryquiz.ui.viewmodel.ScoreTableViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreTableFragment : Fragment() {
    private var _binding: FragmentScoreTableBinding? = null
    private val binding get() = _binding!!
    private val scoreTableViewModel: ScoreTableViewModel by viewModels()
    private lateinit var adapter: ScoreTableAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView(){
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }
            imageBack.visibility = View.INVISIBLE
            textHeader.visibility = View.INVISIBLE
            textRank.visibility = View.INVISIBLE
            textUsername.visibility = View.INVISIBLE
            textScore.visibility = View.INVISIBLE
            view.visibility = View.INVISIBLE
        }
    }

    private fun observeData() {
        scoreTableViewModel.allScores.observe(viewLifecycleOwner) { allScore ->
            val id = scoreTableViewModel.getCurrentUser()?.uid
            adapter = ScoreTableAdapter(allScore,id!!,requireContext())
            binding.recylerView.adapter = adapter
        }

        scoreTableViewModel.loading.observe(viewLifecycleOwner){ loading ->
            if (loading){
                invisibleView()
            }else{
                visibleView()
            }
        }
    }

    private fun invisibleView(){
        binding.apply {
            imageBack.visibility = View.INVISIBLE
            textHeader.visibility = View.INVISIBLE
            textRank.visibility = View.INVISIBLE
            textUsername.visibility = View.INVISIBLE
            textScore.visibility = View.INVISIBLE
            view.visibility = View.INVISIBLE
            lottieAnimation.visibility = View.VISIBLE
            lottieAnimation.playAnimation()
        }
    }

    private fun visibleView(){
        binding.apply {
            imageBack.visibility = View.VISIBLE
            textHeader.visibility = View.VISIBLE
            textRank.visibility = View.VISIBLE
            textUsername.visibility = View.VISIBLE
            textScore.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            lottieAnimation.visibility = View.INVISIBLE
            lottieAnimation.pauseAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}