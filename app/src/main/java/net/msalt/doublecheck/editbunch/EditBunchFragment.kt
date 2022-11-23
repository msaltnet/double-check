package net.msalt.doublecheck.editbunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.msalt.doublecheck.DoubleCheckViewModelFactory
import net.msalt.doublecheck.R
import net.msalt.doublecheck.bunchdetail.BunchDetailFragmentDirections
import net.msalt.doublecheck.bunchdetail.BunchItemListAdapter
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.EditBunchFragBinding
import timber.log.Timber

class EditBunchFragment : Fragment() {

    private val viewModel by viewModels<EditBunchViewModel> { DoubleCheckViewModelFactory }

    private val args: EditBunchFragmentArgs by navArgs()

    private var _binding: EditBunchFragBinding? = null

    private lateinit var listAdapter: CheckItemListAdapter

    private lateinit var navigationLisner: NavController.OnDestinationChangedListener

    private lateinit var bunchId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditBunchFragBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        bunchId = args.bunchId
        viewModel.start(bunchId)
        Timber.d("[Edit] Load Bunch ID: $bunchId")

        navigationLisner = NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.d("OnDestinationChangedListener to ${destination.label} : ${destination.id}")
            if (destination.id == R.id.bunch_detail_fragment_dest)
                viewModel.flushUpdate()
        }
        findNavController().addOnDestinationChangedListener(navigationLisner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.title.observe(this.viewLifecycleOwner) {
            Timber.d("Changed to $it @${lifecycle.currentState}")
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                viewModel.updateTitle(it, UPDATE_DEFERRED_TIME, false)
        }
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("Edit Task Fragment Destroy")
        findNavController().removeOnDestinationChangedListener(navigationLisner)
        _binding = null
    }

    private fun setupListAdapter() {
        listAdapter =
            CheckItemListAdapter(
                viewModel,
                object : CheckItemListAdapter.OnItemDeleteClickListener {
                    override fun onItemDeleteClick(item: CheckItem) {
                        Timber.d("ON CLICK ${item.id}")
                    }
                })
        binding.checkitemList.adapter = listAdapter
        binding.button.setOnClickListener() {
            val item = CheckItem()
            item.contents_data.observe(this.viewLifecycleOwner) {
                Timber.d("Changed item contents ${item.id} : $it")
                viewModel.updateItem(item, UPDATE_DEFERRED_TIME, false)
            }
            viewModel.appendItem(item)
            listAdapter.notifyItemInserted(viewModel.items.size - 1)
        }
        viewModel.loaded.observe(this.viewLifecycleOwner) {
            if (!it)
                return@observe

            for (item in viewModel.items) {
                item.contents_data.observe(this.viewLifecycleOwner) {
                    Timber.d("Changed loaded item contents ${item.id} : $it")
                    viewModel.updateItem(item, UPDATE_DEFERRED_TIME, false)
                }
            }
            listAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val UPDATE_DEFERRED_TIME = 2000L
    }
}
