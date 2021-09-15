/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.features.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.common.base.BaseFragment
import jp.co.yumemi.android.code_check.common.base.BaseViewModel
import jp.co.yumemi.android.code_check.features.screen.search.adapter.CustomAdapter
import jp.co.yumemi.android.code_check.databinding.FragmentOneBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OneFragment : BaseFragment() {

    private var _binding: FragmentOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: OneViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter {
            viewModel.gotoRepositoryFragment(it)
            findNavController().navigate(
                OneFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(it))
        }
        observerViewModel(adapter)
        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        viewModel.searchResults(it)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }


    override fun getViewModel(): BaseViewModel = viewModel

    private fun observerViewModel(adapter: CustomAdapter) {
        viewModel.search.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.searchError.observe(viewLifecycleOwner, {
            if (it == true) {
                Toast.makeText(requireContext(), "Net work error!!", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


