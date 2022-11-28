package net.msalt.doublecheck.bunchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.databinding.BunchCardBinding

class BunchCardListAdapter(
    private val viewModel: BunchListViewModel,
    private val itemClickListener: OnClickListener,
    private val copyButtonClickListener: OnClickListener,
    private val deleteButtonClickListener: OnClickListener
) :
    ListAdapter<Bunch, BunchCardListAdapter.ViewHolder>(BunchCardDiffCallback()) {

    interface OnClickListener {
        fun onClick(item: Bunch)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent,
            itemClickListener,
            copyButtonClickListener,
            deleteButtonClickListener
        )
    }

    class ViewHolder private constructor(private val binding: BunchCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Bunch) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: OnClickListener,
                copyButtonClickListener: OnClickListener,
                deleteButtonClickListener: OnClickListener
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BunchCardBinding.inflate(layoutInflater, parent, false)

                binding.bunchTitleText.setOnClickListener {
                    binding.item?.let {
                        itemClickListener.onClick(it)
                    }
                }
                binding.bunchDigest.setOnClickListener {
                    binding.item?.let {
                        itemClickListener.onClick(it)
                    }
                }
                binding.copyBunch.setOnClickListener {
                    binding.item?.let {
                        copyButtonClickListener.onClick(it)
                    }
                }
                binding.deleteBunch.setOnClickListener {
                    binding.item?.let {
                        deleteButtonClickListener.onClick(it)
                    }
                }

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

@BindingAdapter("bunchitems")
fun setItems(listView: RecyclerView, items: List<Bunch>?) {
    items?.let {
        (listView.adapter as BunchCardListAdapter).submitList(items)
    }
}
