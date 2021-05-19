package com.allosh.martyes.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.allosh.martyes.R
import com.allosh.martyes.databinding.DialogMartyrsBinding
import com.allosh.martyes.model.Martyrs

class MartyrsDialog(context: Context) : Dialog(context, R.style.Theme_Dialog) {

    private val binding: DialogMartyrsBinding = DialogMartyrsBinding.inflate(LayoutInflater.from(context))
    private val list :ArrayList<Martyrs> = ArrayList()
    private val martyrsAdapter: MartyrsAdapter = MartyrsAdapter(context, list)

    init {
        setContentView(binding.root)
        setCancelable(true)
        window!!.setBackgroundDrawable(null)
        list.add(Martyrs("Alaa", 20, "16th May 2011"))
        list.add(Martyrs("Alaa", 20, "16th May 2011"))
        list.add(Martyrs("Alaa", 20, "16th May 2011"))
        list.add(Martyrs("Alaa", 20, "16th May 2011"))
        binding.martyrs.adapter = martyrsAdapter
    }
}