package com.forz.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.databinding.ActivityLicensesBinding
import com.forz.calculator.licenses.License
import com.forz.calculator.licenses.LicenseActionListener
import com.forz.calculator.licenses.LicenseAdapter
import com.forz.calculator.settings.SettingsState
import kotlin.properties.Delegates.notNull

class LicensesActivity : AppCompatActivity() {

    private var binding: ActivityLicensesBinding by notNull()
    private var preferences: Preferences by notNull()
    private var adapter: LicenseAdapter by notNull()

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)

        if (!SettingsState.isDynamicColor){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }

        val typedValue = TypedValue()
        this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        window.statusBarColor = ContextCompat.getColor(this, typedValue.resourceId)


        super.onCreate(savedInstanceState)
        binding = ActivityLicensesBinding.inflate(layoutInflater).also { setContentView(it.root) }


        adapter = LicenseAdapter(this, object : LicenseActionListener {
            override fun onSelectLicense(license: License) {
                url(license.url)
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter


        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    private fun url(string: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        startActivity(intent)
    }
}