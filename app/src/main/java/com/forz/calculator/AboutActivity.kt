package com.forz.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.forz.calculator.databinding.ActivityAboutBinding
import com.forz.calculator.settings.Config
import com.forz.calculator.settings.Preferences
import com.forz.calculator.utils.InteractionAndroid
import kotlin.properties.Delegates

class AboutActivity : AppCompatActivity() {

    private var binding: ActivityAboutBinding by Delegates.notNull()
    private var preferences: Preferences by Delegates.notNull()


    @SuppressLint("DiscouragedApi", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)

        if (!Config.isDynamicColor){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }

        val typedValue = TypedValue()
        this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        window.statusBarColor = ContextCompat.getColor(this, typedValue.resourceId)


        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.versionText.text = getAppVersionName()



        binding.translateLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_translate), this)
        }

        binding.rateLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_app), this)
        }

        binding.donateLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_donate), this)
        }

        binding.emailLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_email), this)
        }

        binding.emailLayout.setOnLongClickListener {
            InteractionAndroid.copyToClipboard(getString(R.string.description_email), this)
            true
        }

        binding.openSourceLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_open_source), this)
        }

        binding.licensesLayout.setOnClickListener {
            val intent = Intent(this, LicensesActivity::class.java)
            startActivity(intent)
        }

        binding.privacyPolicyLayout.setOnClickListener {
            InteractionAndroid.openUrl(getString(R.string.url_privacy_policy), this)
        }

        binding.versionLayout.setOnLongClickListener {
            InteractionAndroid.copyToClipboard(getAppVersionName(), this)
            true
        }



        binding.loveLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val sizeInDp = 256
                val sizeInPixels = (sizeInDp * resources.displayMetrics.density + 0.5f).toInt()

                val imageView = ImageView(this).apply {
                    setImageResource(R.drawable.anim_hearts)
                    layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)
                }

                val animatedVectorDrawable = imageView.drawable as? AnimatedVectorDrawable
                animatedVectorDrawable?.start()

                val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels).apply {
                    leftToLeft = binding.background.id
                    topToTop = binding.background.id
                    marginStart = event.rawX.toInt() - sizeInPixels / 2
                    topMargin = event.rawY.toInt() - sizeInPixels / 2
                }

                imageView.layoutParams = layoutParams
                binding.background.addView(imageView)

                val animationDuration = 1600L
                imageView.postDelayed({
                    binding.background.removeView(imageView)
                }, animationDuration)

                return@setOnTouchListener true
            }
            false
        }


        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getAppVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }
}