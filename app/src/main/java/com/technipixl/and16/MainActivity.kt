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
			toggleMenuWithAnimations(menuAnims)
		}
	}

	/**
	 * Description: function that open or close a menu
	 *
	 * @param animations List of all the animations to play when opening or closing the menu
	 */
	private fun toggleMenuWithAnimations(animations: List<ValueAnimator>) {
		when (isMenuActivated) {
			true -> {
				val animSet = AnimatorSet()
				animSet.playTogether(animations)
				animSet.reverse()
			}
			false -> {
				val animSet = AnimatorSet()
				animSet.playTogether(animations)
				animSet.start()
			}
		}
		isMenuActivated = !isMenuActivated
	}

	/**
	 * Description: function that take all menu items and return all animations to make a left
	 * vertical menu.
	 *
	 * @param items the views to animate from top to bottom
	 *
	 * @return [List] of [ValueAnimator]
	 */
	private fun getMenuAnims(vararg items: View) : List<ValueAnimator> {
		val endValueInterval = (180f / (items.size + 1))
		val finalDelay = (items.size - 1) * 150
		val finalList: MutableList<ValueAnimator> = mutableListOf()

		items.forEachIndexed { index, view ->
			val endValue = (index + 1) * endValueInterval
			val delay = (finalDelay - (index * 150L))
			finalList.addAll(getMenuItemAnims(view, endValue, delay))
		}

		return finalList
	}

	/**
	 * Description: function that take a view, generate rotation and alpha animations and return them
	 * in a list
	 *
	 * @param view the view to animate
	 *
	 * @param endValue the end position angle (in degrees)
	 *
	 * @param delay the delay before starting the animation (default value is 0)
	 *
	 * @return [List] of 2 [ValueAnimator]
	 */
	private fun getMenuItemAnims(view: View, endValue: Float, delay: Long = 0) : List<ValueAnimator> {

		val angleAnim = ValueAnimator.ofFloat(0f, endValue).apply {
			duration = 1_000 - delay/2
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