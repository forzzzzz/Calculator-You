package com.forz.calculator.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast

object InteractionAndroid {
    fun copyToClipboard(string: String, context: Context){
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, string)
        clipboardManager.setPrimaryClip(clipData)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        }
    }

    fun share(string: String, context: Context){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, string)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun openUrl(string: String, context: Context){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        context.startActivity(intent)
    }
}