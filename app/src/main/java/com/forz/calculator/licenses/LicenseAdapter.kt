package com.forz.calculator.licenses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.forz.calculator.databinding.ItemLicenseBinding

interface LicenseActionListener {
    fun onSelectLicense(license: License)
}

class LicenseAdapter(
    private val context: Context,
    private val actionListener: LicenseActionListener
    ) : RecyclerView.Adapter<LicenseAdapter.LicenseViewHolder>(), View.OnClickListener {

     private val licenses = listOf(
         License(id = 0, name = "Core Kotlin Extensions", author = "The Android Open Source Project", description = "Kotlin extensions for 'core' artifact.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/core"),
         License(id = 1, name = "Appcompat", author = "The Android Open Source Project", description = "Allows access to new APIs on older API versions of the platform.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/appcompat"),
         License(id = 2, name = "Material Components for Android", author = "The Android Open Source Project", description = "Modular and customizable Material Design UI components for Android.", license = "Apache-2.0", url = "https://github.com/material-components/material-components-android"),
         License(id = 3, name = "Constraintlayout", author = "The Android Open Source Project", description = "Position and size widgets in a flexible way with relative positioning.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/constraintlayout"),
         License(id = 4, name = "Preference", author = "The Android Open Source Project", description = "Build interactive settings screens without needing to interact with device storage or manage the UI.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/preference"),
         License(id = 5, name = "ViewPager2", author = "The Android Open Source Project", description = "Display Views or Fragments in a swipeable format.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/viewpager2"),
         License(id = 6, name = "Lifecycle", author = "The Android Open Source Project", description = "Lifecycle-aware components perform actions in response to a change in the lifecycle status of another component, such as activities and fragments. These components help you produce better-organized, and often lighter-weight code, that is easier to maintain.", license = "Apache-2.0", url = "https://developer.android.com/jetpack/androidx/releases/lifecycle"),
         License(id = 7, name = "Scalable Size Unit", author = "Intuit", description = "An Android lib that provides a new size unit - sdp (scalable dp). This size unit scales with the screen size.", license = "MIT license", url = "https://github.com/intuit/sdp"),
         License(id = 8, name = "CircleView", author = "Lopez Mikhael", description = "Create circular view in android (change color, border & shadow).", license = "Apache-2.0", url = "https://github.com/lopspower/CircleView"),
         License(id = 9, name = "Arithmetic", author = "Mihai Preda", description = "Java Arithmetic Engine (formerly Arity).", license = "Apache-2.0", url = "https://github.com/preda/arithmetic"),
         License(id = 10, name = "Converter by CalcYou", author = "You Apps", description = "Unit converters.", license = "GPL-3.0", url = "https://github.com/you-apps/CalcYou")
    )


    override fun onClick(v: View) {
        val license = v.tag as License
        actionListener.onSelectLicense(license)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLicenseBinding.inflate(inflater, parent, false)

        binding.background.setOnClickListener(this)

        return LicenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return licenses.size
    }

    override fun onBindViewHolder(holder: LicenseViewHolder, position: Int) {
        val license = licenses[position]
        with(holder.binding) {
            background.tag = license

            val density = context.resources.displayMetrics.density

            if (position != 0){
                (background.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 0
            }else{
                val topMargin = (16 * density).toInt()
                (background.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topMargin
            }

            background.contentDescription = license.name
            name.text = license.name
            author.text = license.author
            description.text = license.description
            this.license.text = license.license
        }
    }

    class LicenseViewHolder(
        val binding: ItemLicenseBinding
    ) : RecyclerView.ViewHolder(binding.root)
}