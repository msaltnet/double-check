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
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.BunchListFragBinding
import net.msalt.doublecheck.editbunch.CheckItemListAdapter
import net.msalt.doublecheck.editbunch.EditBunchViewModel
import timber.log.Timber

class BunchListFragment : Fragment() {

    private var _binding: BunchListFragBinding? = null

    private val viewModel by viewModels<BunchListViewModel> { DoubleCheckViewModelFactory }

    private lateinit var listAdapter: BunchCardListAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BunchListFragBinding.inflate(inflater, container, false)
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
        viewModel.update()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.d("Bunch List Fragment Destroy")
    }

    private fun setupListAdapter() {
        listAdapter =
            BunchCardListAdapter(viewModel, object : BunchCardListAdapter.OnItemClickListener {
                override fun onItemClick(item: Bunch) {
                    Timber.d("ON CLICK ${item.id}")
                    val mainActivity = activity as MainActivity
                    mainActivity.activeBunchId = item.id
                    findNavController().navigate(R.id.bunch_detail_fragment_dest)
                }
            })
        binding.bunchList.adapter = listAdapter
    }
}

