package net.msalt.doublecheck.bunchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.msalt.doublecheck.DoubleCheckViewModelFactory
import net.msalt.doublecheck.MainActivity
import net.msalt.doublecheck.R
import net.msalt.doublecheck.data.BunchCard
import net.msalt.doublecheck.databinding.BunchListFragBinding
import timber.log.Timber

class BunchListFragment : Fragment() {

    private val viewModel by viewModels<BunchListViewModel> { DoubleCheckViewModelFactory }
    private lateinit var binding: BunchListFragBinding
    private lateinit var listAdapter: BunchCardListAdapter
    private var isWaitingClone = false
    private var clonedBunchId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BunchListFragBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.activeBunchId = ""
            findNavController().navigate(R.id.bunch_detail_fragment_dest)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel
        setupListAdapter()
        viewModel.start()
        viewModel.cloneCompleted.observe(this.viewLifecycleOwner) {
            if (it && isWaitingClone) {
                isWaitingClone = false
                goToBunchDetail(clonedBunchId)
            }
        }
    }

    private fun goToBunchDetail(id: String) {
        val mainActivity = activity as MainActivity
        mainActivity.activeBunchId = id
        findNavController().navigate(R.id.bunch_detail_fragment_dest)
    }

    private fun setupListAdapter() {
        val itemClickListener = object : BunchCardListAdapter.OnClickListener {
            override fun onClick(item: BunchCard) {
                Timber.d("ON ITEM CLICK ${item.id}")
                goToBunchDetail(item.id)
            }
        }

        val cloneClickListener = object : BunchCardListAdapter.OnClickListener {
            override fun onClick(item: BunchCard) {
                Timber.d("ON CLONE CLICK ${item.id} $isWaitingClone")
                if (isWaitingClone)
                    return

                isWaitingClone = true
                val id = viewModel.cloneBunch(item)
                clonedBunchId = id
            }
        }

        val deleteClickListener = object : BunchCardListAdapter.OnClickListener {
            override fun onClick(item: BunchCard) {
                Timber.d("ON DELETE CLICK ${item.id}")
                viewModel.items.value?.let {
                    val pos = it.indexOf(item)
                    viewModel.deleteBunch(item)
                    listAdapter.notifyItemRemoved(pos)
                    listAdapter.notifyItemRangeChanged(pos, it.size)
                }
            }
        }

        listAdapter =
            BunchCardListAdapter(
                itemClickListener,
                cloneClickListener,
                deleteClickListener
            )
        binding.bunchList.adapter = listAdapter
        viewModel.loaded.observe(this.viewLifecycleOwner) {
            if (it) {
                listAdapter.notifyDataSetChanged()
            }
        }
    }
}
