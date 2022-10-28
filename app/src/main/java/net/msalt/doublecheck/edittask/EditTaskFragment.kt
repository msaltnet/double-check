package net.msalt.doublecheck.edittask

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import net.msalt.doublecheck.R
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.databinding.EditTaskFragBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditTaskFragment : Fragment() {

    private val viewModel by viewModels<EditTaskViewModel>()

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
        binding.fab2.setOnClickListener {
            repeat(999) {
                addNewItem()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListAdapter() {
        if (viewModel != null) {
            listAdapter = CheckItemListAdapter(viewModel)
            binding.checkitemList.adapter = listAdapter
            binding.button.setOnClickListener() {
                addNewItem()
            }
        } else {
            Log.w("JSM_TEST", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun addNewItem() {
        val item = CheckItem()
        item.contents.value = item.id
        item.contents.observe(this.viewLifecycleOwner) {
            Log.d("JSM", "Changed item contents ${item.id} : ${item.contents.value}")
        }
        viewModel.items.add(item)
        listAdapter?.notifyItemInserted(viewModel.items.size - 1)
    }
}