package net.msalt.doublecheck.bunchdetail

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
        setupMenuProvider()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListAdapter() {
        listAdapter =
            BunchItemListAdapter(viewModel, object : BunchItemListAdapter.OnItemClickListener {
                override fun onItemClick(item: CheckItem) {
                    Timber.d("ON CLICK ${item.id}")
                    viewModel.items.value?.let {
                        val pos = it.indexOf(item)
                        viewModel.toggleCheck(item)
                        listAdapter.notifyItemRangeChanged(pos, 1)
                    }
                }
            })
        binding.bunchItemList.adapter = listAdapter
    }

    private fun setupMenuProvider() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_bunch_detail, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.editBunch -> {
                            val action =
                                BunchDetailFragmentDirections.actionBunchDetailFragmentToEditBunchFragment(
                                    bunchId
                                )
                            findNavController().navigate(action)
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }
}

