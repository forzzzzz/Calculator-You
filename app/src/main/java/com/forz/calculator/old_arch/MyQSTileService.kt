package com.forz.calculator.old_arch

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import com.forz.calculator.old_arch.calculator.Evaluator
import com.forz.calculator.old_arch.settings.Config
import com.forz.calculator.old_arch.utils.NumberFormatter
import java.math.BigDecimal

class MyQSTileService: TileService() {

    override fun onStartListening() {
        super.onStartListening()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val tile = qsTile ?: return

            if (Evaluator.converterResult.value != null){
                tile.subtitle = NumberFormatter.formatResult(
                    BigDecimal(Evaluator.converterResult.value!!),
                    Config.numberPrecision,
                    Config.maxScientificNotationDigits,
                    Config.groupingSeparatorSymbol,
                    Config.decimalSeparatorSymbol
                )
            } else{
                tile.subtitle = ""
            }

            tile.updateTile()
        }
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_IMMUTABLE))
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }
}