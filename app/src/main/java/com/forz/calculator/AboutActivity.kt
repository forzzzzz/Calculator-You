package com.forz.calculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class AboutActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences


    @SuppressLint("DiscouragedApi", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {


        preferences = Preferences(this@AboutActivity)

        if (!preferences.getDynamicColor()){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }

        val typedValue = TypedValue()
        this@AboutActivity.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        window.statusBarColor = ContextCompat.getColor(this@AboutActivity, typedValue.resourceId)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)


        val arrowBack: ImageView = findViewById(R.id.arrowBack)

        val translateLayout: ConstraintLayout = findViewById(R.id.translateLayout)
        val rateLayout: ConstraintLayout = findViewById(R.id.rateLayout)
        val telegramLayout: ConstraintLayout = findViewById(R.id.telegramLayout)
        val emailLayout: ConstraintLayout = findViewById(R.id.emailLayout)
        val openSourceLayout: ConstraintLayout = findViewById(R.id.openSourceLayout)
        val licensesLayout: ConstraintLayout = findViewById(R.id.licensesLayout)
        val privacyPolicyLayout: ConstraintLayout = findViewById(R.id.privacyPolicyLayout)
        val versionLayout: ConstraintLayout = findViewById(R.id.versionLayout)
        val loveLayout: LinearLayout = findViewById(R.id.loveLayout)




        translateLayout.setOnClickListener {
            Toast.makeText(this@AboutActivity, "Test", Toast.LENGTH_SHORT).show()
        }


        rateLayout.setOnClickListener {
            url(getString(R.string.url_app))
        }


        telegramLayout.setOnClickListener {
            url(getString(R.string.url_telegram))
        }
        telegramLayout.setOnLongClickListener {
            copy(getString(R.string.description_telegram))
            true
        }


        emailLayout.setOnLongClickListener {
            copy(getString(R.string.description_email))
            true
        }


        openSourceLayout.setOnClickListener {
            Toast.makeText(this@AboutActivity, "Test", Toast.LENGTH_SHORT).show()
        }

        licensesLayout.setOnClickListener {
            Toast.makeText(this@AboutActivity, "Test", Toast.LENGTH_SHORT).show()
        }

        privacyPolicyLayout.setOnClickListener {
            url(getString(R.string.url_privacy_policy))
        }

        versionLayout.setOnLongClickListener {
            copy(getString(R.string.description_version_app))
            true
        }



        val background: ConstraintLayout = findViewById(R.id.background)
        loveLayout.setOnTouchListener { _, event ->
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












        arrowBack.setOnClickListener {
            finish()
        }
    }


    private fun copy(string: String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("versionApp", string)
        clipboardManager.setPrimaryClip(clipData)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        }
    }

    private fun url(string: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        startActivity(intent)
    }
}