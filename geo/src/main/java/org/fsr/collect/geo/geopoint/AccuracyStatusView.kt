package org.fsr.collect.geo.geopoint

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.fsr.collect.androidshared.system.ContextUtils.getThemeAttributeValue
import org.fsr.collect.geo.GeoUtils.formatAccuracy
import org.fsr.collect.geo.R
import org.fsr.collect.geo.databinding.AccuracyStatusBinding

internal class AccuracyStatusView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    var binding = AccuracyStatusBinding.inflate(LayoutInflater.from(context), this, true)
        private set

    var accuracy: GeoPointAccuracy? = null
        set(value) {
            field = value
            if (value != null) {
                render(value)
            }
        }

    private fun render(accuracy: GeoPointAccuracy) {
        val (backgroundColor, textColor) = getBackgroundAndTextColor(accuracy)
        binding.root.background = ColorDrawable(backgroundColor)
        binding.title.setTextColor(textColor)
        binding.text.setTextColor(textColor)
        binding.currentAccuracy.setTextColor(textColor)
        binding.strength.setIndicatorColor(textColor)

        binding.currentAccuracy.text = formatAccuracy(context, accuracy.value)

        val (text, strength) = getTextAndStrength(accuracy)
        binding.text.setText(text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.strength.setProgress(strength, true)
        } else {
            binding.strength.progress = strength
        }
    }

    private fun getBackgroundAndTextColor(accuracy: GeoPointAccuracy): Pair<Int, Int> {
        return if (accuracy is GeoPointAccuracy.Unacceptable) {
            Pair(
                getThemeAttributeValue(context, com.google.android.material.R.attr.colorError),
                getThemeAttributeValue(context, com.google.android.material.R.attr.colorOnError)
            )
        } else {
            Pair(
                getThemeAttributeValue(context, com.google.android.material.R.attr.colorPrimary),
                getThemeAttributeValue(context, com.google.android.material.R.attr.colorOnPrimary)
            )
        }
    }

    private fun getTextAndStrength(accuracy: GeoPointAccuracy): Pair<Int, Int> {
        return when (accuracy) {
            is GeoPointAccuracy.Improving -> Pair(org.fsr.collect.strings.R.string.improving_accuracy, 80)
            is GeoPointAccuracy.Poor -> Pair(org.fsr.collect.strings.R.string.poor_accuracy, 60)
            is GeoPointAccuracy.Unacceptable -> Pair(org.fsr.collect.strings.R.string.unacceptable_accuracy, 40)
        }
    }
}
