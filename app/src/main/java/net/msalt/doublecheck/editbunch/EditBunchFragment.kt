package net.msalt.doublecheck.editbunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import net.msalt.doublecheck.DoubleCheckViewModelFactory
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.EditBunchFragBinding
import timber.log.Timber

class EditBunchFragment : Fragment() {

    private val viewModel by viewModels<EditBunchViewModel> { DoubleCheckViewModelFactory }

    private val args: EditBunchFragmentArgs by navArgs()

    private var _binding: EditBunchFragBinding? = null

    private lateinit var listAdapter: CheckItemListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = EditBunchFragBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        Timber.d("Bunch ID: ${args.bunchId}")
        viewModel.start(args.bunchId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.title.observe(this.viewLifecycleOwner) {
            Timber.d("Changed ${viewModel.title.value}")
        }
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("Edit Task Fragment Destroy")
        _binding = null
    }

    private fun setupListAdapter() {
        listAdapter = CheckItemListAdapter(viewModel)
        binding.checkitemList.adapter = listAdapter
        binding.button.setOnClickListener() {
            val item = CheckItem()
            item.contents_data.value = item.id
            item.contents_data.observe(this.viewLifecycleOwner) {
                Timber.d("Changed item contents ${item.id} : ${item.contents_data.value}")
            }
            viewModel.items.add(item)
            listAdapter.notifyItemInserted(viewModel.items.size - 1)
        }
    }
}
