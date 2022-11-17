package net.msalt.doublecheck.editbunch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.EditItemBinding

class CheckItemListAdapter(private val viewModel: EditBunchViewModel) :
        ListAdapter<CheckItem, CheckItemListAdapter.ViewHolder>(CheckItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return viewModel.items.size
    }

    class ViewHolder private constructor(private val binding: EditItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CheckItem) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EditItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class CheckItemDiffCallback : DiffUtil.ItemCallback<CheckItem>() {
    override fun areItemsTheSame(oldItem: CheckItem, newItem: CheckItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CheckItem, newItem: CheckItem): Boolean {
        return oldItem == newItem
    }
}
