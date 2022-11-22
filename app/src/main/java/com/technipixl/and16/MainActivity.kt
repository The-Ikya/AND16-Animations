package com.technipixl.and16

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.technipixl.and16.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private var _binding: ActivityMainBinding? = null
	private val binding get() = _binding!!

	private var isMenuActivated = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val menuAnims = getMenuAnims(
			binding.calendarButton,
			binding.searchButton,
			binding.settingsButton
		)

		binding.menuButton.setOnClickListener {
			when (isMenuActivated) {
				true -> {
					val animSet = AnimatorSet()
					animSet.playTogether(menuAnims)
					animSet.reverse()
				}
				false -> {
					val animSet = AnimatorSet()
					animSet.playTogether(menuAnims)
					animSet.start()
				}
			}
			isMenuActivated = !isMenuActivated
		}
	}

	private fun getMenuAnims(vararg items: View) : List<ValueAnimator> {
		val endValueInterval = (180 / (items.size + 1)).toFloat()
		val finalDelay = (items.size - 1) * 150
		val finalList: MutableList<ValueAnimator> = mutableListOf()

		items.forEachIndexed { index, view ->
			val endValue = (index + 1) * endValueInterval
			val delay = (finalDelay - (index * 150)).toLong()
			finalList.addAll(getMenuItemAnims(view, endValue, delay))
		}

		return finalList
	}

	private fun getMenuItemAnims(view: View, endValue: Float, delay: Long = 0) : List<ValueAnimator> {

		val angleAnim = ValueAnimator.ofFloat(0f, endValue).apply {
			duration = 1000 - delay/2
			startDelay = delay

			addUpdateListener {
				val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
				layoutParams.circleAngle = it.animatedValue as Float
				view.layoutParams = layoutParams
			}
		}

		val alphaAnim = ValueAnimator.ofFloat(0f, 1f).apply {
			duration = 500
			startDelay = 100 + delay

			addUpdateListener {
				view.alpha = it.animatedValue as Float
			}
		}

		return listOf(angleAnim, alphaAnim)
	}
}