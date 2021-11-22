package za.co.fortunemaseko.codefactory.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import za.co.fortunemaseko.codefactory.R
import za.co.fortunemaseko.codefactory.databinding.FragmentViewPagerBinding
import za.co.fortunemaseko.codefactory.ui.fragments.onboarding.screens.FirstScreenFragment
import za.co.fortunemaseko.codefactory.ui.fragments.onboarding.screens.SecondScreenFragment
import za.co.fortunemaseko.codefactory.ui.fragments.onboarding.screens.ThirdScreenFragment


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var proceedBtn: Button
    private lateinit var viewPagerIndicators: LinearLayout

    companion object {
        private val TAG = ViewPagerFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        initViews()
        setupViewPager()

        binding.tvSkip.setOnClickListener {
            skipOnboarding()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.unregisterOnPageChangeCallback(viewPagerPageChangeCallback)
        _binding = null
    }

    private val viewPagerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setCurrentPageIndicator(position)
        }
    }

    private fun initViews() {
        viewPager = binding.viewPager
        proceedBtn = binding.btnProceed
        viewPagerIndicators = binding.viewPagerIndicators
    }

    private fun setupViewPager() {
        val fragmentList = arrayListOf(
            FirstScreenFragment(),
            SecondScreenFragment(),
            ThirdScreenFragment()
        )

        viewPager.adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter?.itemCount?.let { itemCount ->
            repeat(itemCount) {
                populateViewPagerIndicators()
            }

            proceedBtn.setOnClickListener {
                if (viewPager.currentItem + 1 < itemCount) {
                    viewPager.currentItem++
                } else {
                    skipOnboarding()
                }
            }
        }

        setCurrentPageIndicator(0)

        viewPager.registerOnPageChangeCallback(viewPagerPageChangeCallback)
    }

    private fun populateViewPagerIndicators() {
        val ivIndicator = ImageView(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        ivIndicator.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.inactive_onboarding_tab_indicator
            )
        )

        ivIndicator.layoutParams = layoutParams

        viewPagerIndicators.addView(ivIndicator)
    }

    private fun setCurrentPageIndicator(targetImageIndex: Int) {
        viewPager.adapter?.itemCount?.let { itemCount ->
            if (targetImageIndex == itemCount - 1) {
                proceedBtn.text = getText(R.string.lets_start)
            } else {
                proceedBtn.text = getText(R.string.next)
            }
        }

        repeat(viewPagerIndicators.childCount) { currentPageIndex ->
            val ivIndicator = viewPagerIndicators.getChildAt(currentPageIndex) as ImageView

            if (currentPageIndex == targetImageIndex) {
                ivIndicator.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.active_onboarding_tab_indicator
                    )
                )
            } else {
                ivIndicator.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.inactive_onboarding_tab_indicator
                    )
                )
            }
        }
    }

    private fun skipOnboarding() {
        findNavController().navigate(R.id.action_viewPagerFragment_to_homeScreenFragment)
    }
}