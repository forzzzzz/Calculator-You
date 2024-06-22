package com.forz.calculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.forz.calculator.databinding.ActivityAboutBinding
import com.forz.calculator.settings.SettingsState
import kotlin.properties.Delegates

class AboutActivity : AppCompatActivity() {

    private var binding: ActivityAboutBinding by Delegates.notNull()
    private var preferences: Preferences by Delegates.notNull()


    @SuppressLint("DiscouragedApi", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this@AboutActivity)

        if (!SettingsState.isDynamicColor){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }

        val typedValue = TypedValue()
        this@AboutActivity.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        window.statusBarColor = ContextCompat.getColor(this@AboutActivity, typedValue.resourceId)


        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.versionText.text = getAppVersionName()



        binding.translateLayout.setOnClickListener {
            url(getString(R.string.url_translate))
        }

        binding.rateLayout.setOnClickListener {
            url(getString(R.string.url_app))
        }

        binding.donateLayout.setOnClickListener {
            url(getString(R.string.url_donate))
        }

        binding.telegramLayout.setOnClickListener {
            url(getString(R.string.url_telegram))
        }

        binding.telegramLayout.setOnLongClickListener {
            copy(getString(R.string.description_telegram))
            true
        }

        binding.emailLayout.setOnClickListener {
            url(getString(R.string.url_email))
        }

        binding.emailLayout.setOnLongClickListener {
            copy(getString(R.string.description_email))
            true
        }

        binding.openSourceLayout.setOnClickListener {
            url(getString(R.string.url_open_source))
        }

        binding.licensesLayout.setOnClickListener {
            Toast.makeText(this@AboutActivity, "Test", Toast.LENGTH_SHORT).show()
        }

        binding.privacyPolicyLayout.setOnClickListener {
            url(getString(R.string.url_privacy_policy))
        }

        binding.versionLayout.setOnLongClickListener {
            copy(getAppVersionName())
            true
        }



        val background: ConstraintLayout = findViewById(R.id.background)
        binding.loveLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val sizeInDp = 256
                    val sizeInPixels = (sizeInDp * resources.displayMetrics.density + 0.5f).toInt()

                    val imageView = ImageView(this).apply {
                        setImageResource(R.drawable.hearts_anim)
                        layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)
                    }

                    val animatedVectorDrawable = imageView.drawable as? AnimatedVectorDrawable
                    animatedVectorDrawable?.start()

                    val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels).apply {
                        leftToLeft = background.id
                        topToTop = background.id
                        marginStart = event.rawX.toInt() - sizeInPixels / 2
                        topMargin = event.rawY.toInt() - sizeInPixels / 2
                    }

                    imageView.layoutParams = layoutParams
                    background.addView(imageView)

                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }
    }


    private fun copy(string: String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, string)
        clipboardManager.setPrimaryClip(clipData)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        }
    }

    private fun url(string: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        startActivity(intent)
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