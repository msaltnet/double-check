package net.msalt.doublecheck.bunchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.BunchCardBinding
import timber.log.Timber

class BunchCardListAdapter(private val viewModel: BunchListViewModel) :
        ListAdapter<Bunch, BunchCardListAdapter.ViewHolder>(BunchCardDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.items.value?.get(position) ?: Bunch("Temp Bunch"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: BunchCardBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Bunch) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BunchCardBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class BunchCardDiffCallback : DiffUtil.ItemCallback<Bunch>() {
    override fun areItemsTheSame(oldItem: Bunch, newItem: Bunch): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Bunch, newItem: Bunch): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:bunchitems")
fun setItems(listView: RecyclerView, items: List<Bunch>?) {
    items?.let {
        (listView.adapter as BunchCardListAdapter).submitList(items)
    }
}
