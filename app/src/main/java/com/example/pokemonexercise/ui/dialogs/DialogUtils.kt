package com.example.pokemonexercise.ui.dialogs

import androidx.fragment.app.FragmentManager

object DialogUtils {

    fun showErrorDialogInternet(supportFragmentManager: FragmentManager) {
        if (GenericErrorDialogInternet.shown) {
            return
        }
        val dialog = GenericErrorDialogInternet().apply {
            isCancelable = false
        }
        dialog.show(supportFragmentManager, "GenericErrorDialog")
    }

}