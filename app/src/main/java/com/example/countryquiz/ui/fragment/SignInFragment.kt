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
import com.example.countryquiz.databinding.FragmentSignInBinding
import com.example.countryquiz.ui.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        val user = signInViewModel.getCurrentUser()
        if (user != null) {
            homePageNavigation()
        }

        binding.apply {
            buttonSignin.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    signInViewModel.signIn(email, password)
                }
            }
            buttonSignup.setOnClickListener {
                signUpPageNavigation()
            }
        }
    }

    private fun observeData() {
        signInViewModel.authResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.onSuccess { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    homePageNavigation()
                }
                result.onFailure { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun homePageNavigation() {
        val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun signUpPageNavigation() {
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}