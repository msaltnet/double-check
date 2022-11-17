package net.msalt.doublecheck.bunchdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.DetailItemBinding
import timber.log.Timber

class BunchItemListAdapter(private val viewModel: BunchDetailViewModel, private val clickListener: OnItemClickListener) :
        ListAdapter<CheckItem, BunchItemListAdapter.ViewHolder>(BunchItemDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(item: CheckItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.items.value?.let {
            holder.bind(it[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    class ViewHolder private constructor(private val binding: DetailItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CheckItem) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: OnItemClickListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DetailItemBinding.inflate(layoutInflater, parent, false)

                binding.root.setOnClickListener {
                    binding.item?.let {
                        clickListener.onItemClick(it)
                    }
                }
                return ViewHolder(binding)
            }
        }
    }
}

class BunchItemDiffCallback : DiffUtil.ItemCallback<CheckItem>() {
    override fun areItemsTheSame(oldItem: CheckItem, newItem: CheckItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CheckItem, newItem: CheckItem): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("checkitems")
fun setItems(listView: RecyclerView, items: List<CheckItem>?) {
    items?.let {
        (listView.adapter as BunchItemListAdapter).submitList(items)
    }
}
