package ru.alex.danilov.findafilm

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

class BackDialogFragment : DialogFragment() {

    lateinit var listener: BackDialogListener

    interface BackDialogListener {
        fun onDialogPositiveClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            CustomDialog(it)
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as BackDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement BackDialogListener")
        }
    }

    inner class CustomDialog(context: Context) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.back_dialog)
            val yesButton = findViewById<Button>(R.id.yes_button)
            val noButton = findViewById<Button>(R.id.no_button)
            yesButton.setOnClickListener {
                listener.onDialogPositiveClick()
            }
            noButton.setOnClickListener {
                dismiss() }
        }

    }

}