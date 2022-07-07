package com.zarisa.ezmart.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.Review
import com.zarisa.ezmart.ui.components.removeParagraphTags
import com.zarisa.ezmart.ui.detail.ProductDetailViewModel
import kotlin.math.roundToInt

class ReviewDialog(val review: Review?) : DialogFragment() {
    val viewModel: ProductDetailViewModel by activityViewModels()
    private lateinit var dialogView: View
    private lateinit var btnSend: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var edtTxtReview: EditText
    private lateinit var ratingBar: RatingBar


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogView = LayoutInflater.from(context).inflate(R.layout.layout_review_dialog, null)
            btnSend = dialogView.findViewById(R.id.btn_sendReview)
            btnCancel = dialogView.findViewById(R.id.btn_cancel)
            edtTxtReview = dialogView.findViewById(R.id.editTextReview)
            ratingBar = dialogView.findViewById(R.id.ratingBar_review)
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    interface DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            if (review != null)
                setDataToEdit()
            onClicks()
            builder.setView(dialogView).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setDataToEdit() {
        edtTxtReview.setText(removeParagraphTags(review?.review)?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
        })
        review?.rating?.let { ratingBar.rating = it.toFloat() }
    }

    private fun onClicks() {
        btnSend.setOnClickListener {
            if (edtTxtReview.text.isNullOrBlank())
                edtTxtReview.error = "لطفا نظر خود را بنویسید."
            else {
                if (review == null)
                    viewModel.createReview(
                        ratingBar.rating.roundToInt(),
                        edtTxtReview.text.toString()
                    )
                else {
                    val updatedReview = review.apply {
                        this.review = edtTxtReview.text.toString()
                        this.rating = ratingBar.rating.roundToInt()
                    }
                    viewModel.updateReview(updatedReview)
                }
                dismiss()
            }
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}