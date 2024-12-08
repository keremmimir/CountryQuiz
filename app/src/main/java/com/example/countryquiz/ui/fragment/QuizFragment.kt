package com.example.countryquiz.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.countryquiz.R
import com.example.countryquiz.databinding.FragmentQuizBinding
import com.example.countryquiz.ui.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val quizViewModel: QuizViewModel by viewModels()
    private var countDownTimer: CountDownTimer? = null
    private var secondsRemaining: Long = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val buttons = listOf<Button>(
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
        )
        observeData(buttons)

        Handler(Looper.getMainLooper()).postDelayed({
            startTimer()
        }, 1300)

        binding.imagePause.setOnClickListener {
            pauseTimer()
            showCustomDialog()
        }
    }

    private fun observeData(buttons: List<Button>) {
        quizViewModel.correctCountry.observe(viewLifecycleOwner) { question ->
            if (question != null) {
                val (correctCountry, options) = question
                view?.let {
                    Glide.with(it)
                        .load(correctCountry.flags.png)
                        .override(300, 300)
                        .into(binding.flagImage)
                }
                options.forEachIndexed { index, country ->
                    buttons[index].text = country.name.common

                    buttons[index].setOnClickListener {
                        val response = quizViewModel.checkAnswer(country)
                        if (response) {
                            buttons[index].backgroundTintList = ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.trueButton
                            )
                        } else {
                            buttons[index].backgroundTintList = ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.falseButton
                            )
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            buttons[index].backgroundTintList =
                                ContextCompat.getColorStateList(requireContext(), R.color.button)
                            quizViewModel.createQuiz()
                        }, 500)
                    }
                }
            }
        }

        quizViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                invisibleView()
            } else {
                visibleView()
            }
        }

        quizViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        quizViewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreIntText.text = score.toString()
        }
    }

    private fun invisibleView() {
        binding.apply {
            lottieAnimation.visibility = View.VISIBLE
            lottieAnimation.playAnimation()
            questionText.visibility = View.INVISIBLE
            flagImage.visibility = View.INVISIBLE
            button1.visibility = View.INVISIBLE
            button2.visibility = View.INVISIBLE
            button3.visibility = View.INVISIBLE
            button4.visibility = View.INVISIBLE
        }
    }

    private fun visibleView() {
        binding.apply {
            lottieAnimation.pauseAnimation()
            lottieAnimation.visibility = View.GONE
            questionText.visibility = View.VISIBLE
            flagImage.visibility = View.VISIBLE
            button1.visibility = View.VISIBLE
            button2.visibility = View.VISIBLE
            button3.visibility = View.VISIBLE
            button4.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onTick(p0: Long) {
                secondsRemaining = p0 / 1000
                binding.timerText.text = "Time : ${secondsRemaining}"
            }

            override fun onFinish() {
                val score = binding.scoreIntText.text.toString().toInt()
                endGamePageNavigation(score)
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.custom_dialog_bg
            )
        )

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val btnPositive = dialogView.findViewById<Button>(R.id.buttonContinue)
        val btnNegative = dialogView.findViewById<Button>(R.id.buttonExit)

        btnPositive.setOnClickListener {
            startTimer()
            dialog.dismiss()
        }

        btnNegative.setOnClickListener {
            homePageNavigation()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun endGamePageNavigation(score: Int) {
        val action = QuizFragmentDirections.actionQuizFragmentToEndGameFragment(score = score)
        findNavController().navigate(action)
    }

    private fun homePageNavigation() {
        val action = QuizFragmentDirections.actionQuizFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}