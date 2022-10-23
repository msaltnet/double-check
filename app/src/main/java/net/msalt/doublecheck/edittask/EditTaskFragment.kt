package net.msalt.doublecheck.edittask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.msalt.doublecheck.R
import net.msalt.doublecheck.databinding.EditTaskFragBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditTaskFragment : Fragment() {

    private val viewModel by viewModels<EditTaskViewModel>()

    private var _binding: EditTaskFragBinding? = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}