package com.allosh.martyes.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allosh.martyes.databinding.ItemMartyrsBinding
import com.allosh.martyes.model.Martyrs
import com.allosh.martyes.util.UIUtil


class MartyrsAdapter(private val context: Context, private val list: List<Martyrs>) :
    RecyclerView.Adapter<MartyrsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding = ItemMartyrsBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.setData(list[i])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder internal constructor(private val binding: ItemMartyrsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ),
        View.OnClickListener {
        override fun onClick(view: View) {}

        init {
            UIUtil.setOnClickListeners(arrayOf(binding.martyrsLayout), this)
        }

        fun setData(martyrs: Martyrs) {
            binding.name.text = martyrs.name
            binding.ageDateOfKilled.text = "${martyrs.age} years, ${martyrs.dateOfKilled}"
            if (context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                binding.more.rotation = 180f
            }
        }
    }
}