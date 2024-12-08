package com.example.countryquiz.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.countryquiz.R
import com.example.countryquiz.databinding.FragmentHomeBinding
import com.example.countryquiz.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.apply {
            startButton.setOnClickListener {
                quizPageNavigation()
            }

            scoreTableButton.setOnClickListener {
                scoreTablePageNavigation()
            }

            exitButton.setOnClickListener {
                homeViewModel.signOut()
            }
        }
    }

    private fun observeData() {
        homeViewModel.authResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.onSuccess { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    signInPageNavigation()
                }
                result.onFailure { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun quizPageNavigation() {
        val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment()
        findNavController().navigate(action)
    }

    private fun scoreTablePageNavigation() {
        val action = HomeFragmentDirections.actionHomeFragmentToScoreTableFragment()
        findNavController().navigate(action)
    }

    private fun signInPageNavigation() {
        val action = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
        findNavController().navigate(action)
        findNavController().popBackStack(R.id.homeFragment, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}