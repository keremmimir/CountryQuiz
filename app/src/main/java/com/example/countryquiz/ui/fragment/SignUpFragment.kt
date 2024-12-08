package com.example.countryquiz.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.countryquiz.databinding.FragmentSignUpBinding
import com.example.countryquiz.ui.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        signUp()
        observerData()
    }

    private fun setupView(){
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun signUp() {
        binding.apply {
            buttonSignup.setOnClickListener {
                val email = editTextEmail.text.toString()
                val username = editTextUsername.text.toString()
                val password = editTextPassword.text.toString()
                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields!!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    signUpViewModel.signUp(email, username, password)
                }
            }
        }
    }

    private fun observerData() {
        signUpViewModel.authResult.observe(viewLifecycleOwner) { event ->
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

    private fun signInPageNavigation() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}