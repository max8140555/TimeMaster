package com.max.timemaster

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels


import com.max.timemaster.databinding.DialogMessengerBinding
import com.max.timemaster.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class MessengerDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<MessengerViewModel> {
        getVmFactory(
            MessengerDialogArgs.fromBundle(
                requireArguments()
            ).messengerKey
        )
    }
    lateinit var binding: DialogMessengerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMessengerBinding.inflate(inflater, container, false)
        when(viewModel.messenger){
            "conflict" -> binding.message.text = "　時間衝突囉！"
            "timeError" -> binding.message.text = "　時間設定有誤！"
            "null" -> binding.message.text = "　請填寫行程！"
            "allNull" -> binding.message.text = "　請輸入完整！"
            "active"-> binding.message.text = "追蹤中"
            "!Active"-> binding.message.text = "已封存對象"
        }

            Handler().postDelayed({
                dismiss()
            },1500)

        return binding.root
    }

}
