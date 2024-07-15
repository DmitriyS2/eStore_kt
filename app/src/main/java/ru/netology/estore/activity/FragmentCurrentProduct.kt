package ru.netology.estore.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentCurrentProductBinding
import ru.netology.estore.viewmodel.MainViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class FragmentCurrentProduct : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
            .setDuration(2000L)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCurrentProductBinding.inflate(inflater, container, false)

        val args: FragmentCurrentProductArgs by navArgs()
        val currentName = args.transitName
        val currentImageView = binding.avatar
        ViewCompat.setTransitionName(currentImageView, currentName)

        val currentId = currentName.toInt()

        viewModel.dataFull.observe(viewLifecycleOwner)
        { full ->
            full.products.find {
                it.id == currentId
            }?.let { product ->
                binding.apply {
                    if (product.isHit) {
                        hit.visibility = View.VISIBLE
                        ObjectAnimator.ofFloat(hit, View.ROTATION, 0F, 360F).apply {
                            duration = 1500
                            repeatCount = 100
                        }.start()
                    } else {
                        hit.visibility = View.INVISIBLE
                    }

                    if (product.isDiscount) {
                        groupDiscount.visibility = View.VISIBLE
                        val scalePictureX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.3F, 1F)
                        val scalePictureY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.3F, 1F)
                        ObjectAnimator.ofPropertyValuesHolder(
                            pictureDiscount,
                            scalePictureX,
                            scalePictureY
                        ).apply {
                            duration = 1000
                            repeatCount = 150
                        }.start()
                        textDiscount.text = "-${product.minusPercent}%"
                        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.3F, 1F)
                        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.3F, 1F)
                        ObjectAnimator.ofPropertyValuesHolder(textDiscount, scaleX, scaleY).apply {
                            duration = 1000
                            repeatCount = 100
                        }.start()
                        price.alpha = 0.3f
                        newPrice.text = "${
                            (product.priceN * BigDecimal((100 - product.minusPercent) / 100.0)).setScale(
                                2,
                                RoundingMode.HALF_UP
                            )
                        } " +
                                "${getString(R.string.rub)}/${product.unitWeight}"

                    } else {
                        price.alpha = 1f
                        groupDiscount.visibility = View.INVISIBLE
                    }

                    avatar.setImageResource(product.picture)

                    txItem.text = product.name

                    price.text =
                        "${product.priceN} ${getString(R.string.rub)}/${product.unitWeight}"

                    textInfoAboutCountry.text = product.country
                    textInfoAboutStorage.text = product.storage
                    textInfoAboutPack.text = product.pack
                }
            }
        }

        return binding.root
    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = FragmentCurrentProduct()
//    }
}