package net.msalt.doublecheck.edittask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.EditTaskFragBinding

class EditBunchFragment : Fragment() {

    private val viewModel by viewModels<EditTaskViewModel>()

    private val args: EditBunchFragmentArgs by navArgs()

    private var _binding: EditTaskFragBinding? = null

    private lateinit var listAdapter: CheckItemListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = EditTaskFragBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        Log.d("JSM", "Bunch ID: ${args.bunchId}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.title.observe(this.viewLifecycleOwner) {
            Log.d("JSM", "Changed ${viewModel.title.value}")
        }
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListAdapter() {
        listAdapter = CheckItemListAdapter(viewModel)
        binding.checkitemList.adapter = listAdapter
        binding.button.setOnClickListener() {
            val item = CheckItem()
            item.contents_data.value = item.id
            item.contents_data.observe(this.viewLifecycleOwner) {
                Log.d("JSM", "Changed item contents ${item.id} : ${item.contents_data.value}")
            }
            viewModel.items.add(item)
            listAdapter.notifyItemInserted(viewModel.items.size - 1)
        }
    }
}