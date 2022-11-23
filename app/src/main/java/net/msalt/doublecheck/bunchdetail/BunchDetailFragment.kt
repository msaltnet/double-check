package net.msalt.doublecheck.bunchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.msalt.doublecheck.DoubleCheckViewModelFactory
import net.msalt.doublecheck.MainActivity
import net.msalt.doublecheck.R
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.BunchDetailFragBinding
import net.msalt.doublecheck.editbunch.EditBunchFragment
import timber.log.Timber

class BunchDetailFragment : Fragment() {

    private val viewModel by viewModels<BunchDetailViewModel> { DoubleCheckViewModelFactory }

    private var _binding: BunchDetailFragBinding? = null

    private lateinit var listAdapter: BunchItemListAdapter

    private lateinit var bunchId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BunchDetailFragBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel

        val mainActivity = activity as MainActivity

        if (mainActivity.activeBunchId == "") {
            val newBunch = Bunch()
            mainActivity.activeBunchId = newBunch.id
            bunchId = mainActivity.activeBunchId
            viewModel.start(newBunch)
            val action =
                BunchDetailFragmentDirections.actionBunchDetailFragmentToEditBunchFragment(bunchId)
            findNavController().navigate(action)
        } else {
            bunchId = mainActivity.activeBunchId
            viewModel.start(bunchId)
        }

        Timber.d("[Detail] Bunch ID: $bunchId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.d("Bunch Detail Fragment Destroy")
    }

    private fun setupListAdapter() {
        listAdapter =
            BunchItemListAdapter(viewModel, object : BunchItemListAdapter.OnItemClickListener {
                override fun onItemClick(item: CheckItem) {
                    Timber.d("ON CLICK ${item.id}")
                    val action =
                        BunchDetailFragmentDirections.actionBunchDetailFragmentToEditBunchFragment(
                            bunchId
                        )
                    findNavController().navigate(action)
                }
            })
        binding.bunchItemList.adapter = listAdapter
    }
}

