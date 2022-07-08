package com.zarisa.ezmart.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.button.MaterialButton
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.Status
import com.zarisa.ezmart.ui.shopping.ShoppingViewModel


class CompleteOrderDialog(val lifeCycle: LifecycleOwner) : DialogFragment() {
    val viewModel: ShoppingViewModel by activityViewModels()
    private lateinit var dialogView: View
    private lateinit var etCoupon: EditText
    private lateinit var btnCheckCoupon: MaterialButton
    private lateinit var btnRegisterOrder: Button
    private lateinit var tvFinalPrice: TextView
    private lateinit var btnCancel: MaterialButton

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogView = LayoutInflater.from(context).inflate(R.layout.layot_complete_order, null)
            etCoupon = dialogView.findViewById(R.id.editText_coupon)
            btnCheckCoupon = dialogView.findViewById(R.id.btn_checkCoupon)
            btnRegisterOrder = dialogView.findViewById(R.id.btn_register_order)
            tvFinalPrice = dialogView.findViewById(R.id.tv_finalPrice)
            btnCancel = dialogView.findViewById(R.id.btn_cancel_completion)
        } catch (e: ClassCastException) {
            throw ClassCastException(
                ("$context must implement NoticeDialogListener")
            )
        }
    }

    interface DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            bindView()
            builder.setView(dialogView).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun bindView() {
        viewModel.total.observe(lifeCycle) {
            tvFinalPrice.text = "$it تومان"
        }
        viewModel.couponStatus.observe(lifeCycle) { status ->
            when (status) {
                Status.LOADING -> {
                    btnCheckCoupon.let {
                        it.setIconResource(R.drawable.ic_load_image)
                        it.text = ""
                        it.isEnabled = false
                    }
                }
                Status.NETWORK_ERROR -> {
                    btnCheckCoupon.let {
                        it.setIconResource(R.drawable.ic_no_internet)
                        it.text = ""
                        it.isEnabled = true
                        it.isClickable = false
                    }
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            btnCheckCoupon.let {
                                it.setIconResource(0)
                                it.setText(R.string.check_coupon)
                                it.isEnabled = true
                                it.isClickable = true
                            }
                        }
                    }.start()
                }
                Status.SERVER_ERROR -> {
                    btnCheckCoupon.let {
                        it.setIconResource(R.drawable.ic_server_error)
                        it.text = ""
                        it.isEnabled = true
                        it.isClickable = false
                    }
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            btnCheckCoupon.let {
                                it.setIconResource(0)
                                it.setText(R.string.check_coupon)
                                it.isEnabled = true
                                it.isClickable = true
                            }
                        }
                    }.start()
                }
                Status.EMPTY_CART -> {
                    btnCheckCoupon.let {
                        it.setIconResource(0)
                        it.text = "کد تخفیف معتبر نیست."
                        it.isEnabled = true
                        it.isClickable = false
                    }
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            btnCheckCoupon.let {
                                it.setIconResource(0)
                                it.setText(R.string.check_coupon)
                                it.isEnabled = true
                                it.isClickable = true
                            }
                        }
                    }.start()
                }
                else -> {
                    btnCheckCoupon.let {
                        it.setIconResource(0)
                        it.setText(R.string.check_coupon)
                        it.isEnabled = true
                        it.isClickable = true
                    }
                }
            }
        }
        btnCheckCoupon.setOnClickListener {
            if (!etCoupon.text.isNullOrBlank())
                viewModel.checkCoupon(etCoupon.text.toString())
        }
        btnRegisterOrder.setOnClickListener {
            viewModel.completeOrder()
            dismiss()
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}
