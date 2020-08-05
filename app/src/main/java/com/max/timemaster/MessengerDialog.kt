package com.max.timemaster

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.max.timemaster.databinding.DialogMessengerBinding
import com.max.timemaster.ext.getVmFactory


class MessengerDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<MessengerViewModel> {
        getVmFactory(MessengerDialogArgs.fromBundle(requireArguments()).messengerKey)
    }

    lateinit var binding: DialogMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DialogMessengerBinding.inflate(inflater, container, false)

        when (viewModel.messenger) {
            MessageTypeFilter.CONFLICT.value -> binding.message.text = getString(R.string.time_conflict_text)
            MessageTypeFilter.TIME_ERROR.value -> binding.message.text = getString(R.string.time_error_text)
            MessageTypeFilter.NOT_TITLE.value -> binding.message.text = getString(R.string.incomplete_title_text)
            MessageTypeFilter.INCOMPLETE_TEXT.value -> binding.message.text = getString(R.string.incomplete_text)
            MessageTypeFilter.ACTIVE.value -> binding.message.text = getString(R.string.activity_text)
            MessageTypeFilter.ARCHIVE.value -> binding.message.text = getString(R.string.archive_text)
        }

        Handler().postDelayed({
            dismiss()
        }, 1500)

        return binding.root
    }

}
