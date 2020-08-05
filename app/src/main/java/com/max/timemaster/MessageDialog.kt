package com.max.timemaster

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.max.timemaster.databinding.DialogMessageBinding
import com.max.timemaster.ext.getVmFactory


class MessageDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<MessageViewModel> {
        getVmFactory(MessageDialogArgs.fromBundle(requireArguments()).messengerKey)
    }

    lateinit var binding: DialogMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DialogMessageBinding.inflate(inflater, container, false)

        binding.message.text = when (viewModel.messenger) {
            MessageType.CONFLICT.value -> getString(R.string.time_conflict_text)
            MessageType.TIME_ERROR.value -> getString(R.string.time_error_text)
            MessageType.NOT_TITLE.value -> getString(R.string.incomplete_title_text)
            MessageType.INCOMPLETE_TEXT.value -> getString(R.string.incomplete_text)
            MessageType.ACTIVE.value -> getString(R.string.activity_text)
            MessageType.ARCHIVE.value -> getString(R.string.archive_text)
            else -> getString(R.string.you_know_nothing)
        }

        Handler().postDelayed({
            dismiss()
        }, 1500)

        return binding.root
    }

}
