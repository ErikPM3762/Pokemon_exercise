package com.example.pokemonexercise.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.toolsAndResources.R
import com.example.toolsAndResources.databinding.DialogFailedInternetBinding

/**
 * The class [GenericErrorDialogInternet] create error dialog
 */
class GenericErrorDialogInternet : DialogFragment() {
    var listenerOk: DialogListener? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        shown = true
        val view: View = layoutInflater.inflate(R.layout.dialog_failed_internet, null)
        val binding = DialogFailedInternetBinding.bind(view)

        binding.imCloseDialog.setOnClickListener {
            dismiss()
            listenerOk?.okAction()
        }

        return AlertDialog.Builder(requireContext()).apply {
            setView(view)
        }.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        shown = false
        super.onDismiss(dialog)
    }

    companion object {
        var shown = false
    }

    interface DialogListener {
        fun okAction()
    }
}
