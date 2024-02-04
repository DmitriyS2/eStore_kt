package ru.netology.estore.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentCurrentProductBinding
import ru.netology.estore.dto.getSumWithTwoDecimal
import ru.netology.estore.util.StringArg
import ru.netology.estore.viewmodel.MainViewModel

class FragmentCurrentProduct : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

 //   lateinit var binding: FragmentCurrentProductBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentProductBinding.inflate(inflater, container, false)

        val currentId = arguments?.textArgument?.toInt()

        viewModel.dataFull.observe(viewLifecycleOwner) { full ->
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
                        Price.alpha = 0.3f
                        newPrice.text = "${
                            getSumWithTwoDecimal(
                                product.price * (100 - product.minusPercent) / 100,
                                100.0
                            )
                        }"+getString(R.string.rub_za_1)+ product.unitWeight

                    } else {
                        Price.alpha = 1f
                        groupDiscount.visibility = View.INVISIBLE
                    }

                    avatar.setImageResource(product.picture)

                    txItem.text = product.name

                    Price.text = "${product.price}" + getString(R.string.rub_za_1) + product.unitWeight

                    textInfoAboutCountry.text = product.country
                    textInfoAboutStorage.text = product.storage
                    textInfoAboutPack.text = product.pack
                }
            }
        }

        return binding.root
    }

    companion object {

        var Bundle.textArgument: String? by StringArg

        @JvmStatic
        fun newInstance() = FragmentCurrentProduct()
    }
}