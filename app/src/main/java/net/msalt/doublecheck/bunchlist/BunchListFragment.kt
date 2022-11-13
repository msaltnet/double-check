package net.msalt.doublecheck.bunchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.msalt.doublecheck.DoubleCheckViewModelFactory
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.BunchListFragBinding
import net.msalt.doublecheck.editbunch.CheckItemListAdapter
import net.msalt.doublecheck.editbunch.EditBunchViewModel
import timber.log.Timber

class BunchListFragment : Fragment() {

    private var _binding: BunchListFragBinding? = null

    private val viewModel by viewModels<BunchListViewModel> { DoubleCheckViewModelFactory }

    private lateinit var listAdapter: BunchCardListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = BunchListFragBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            val action = BunchListFragmentDirections.actionBunchListFragmentToEditBunchFragment("")
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel
        setupListAdapter()
        viewModel.update()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListAdapter() {
        listAdapter = BunchCardListAdapter(viewModel)
        binding.bunchList.adapter = listAdapter
    }
}
