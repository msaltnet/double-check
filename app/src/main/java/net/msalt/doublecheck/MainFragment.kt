package net.msalt.doublecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.msalt.doublecheck.databinding.MainFragBinding

class MainFragment : Fragment() {

    private var _binding: MainFragBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = MainFragBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditBunchFragment("")
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}