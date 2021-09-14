/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.databinding.FragmentOneBinding

class OneFragment: Fragment(R.layout.fragment_one){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val binding= FragmentOneBinding.bind(view)
        val viewModel= OneViewModel(app = requireActivity().application)
        val layoutManager= LinearLayoutManager(requireContext())
        val dividerItemDecoration=
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter= CustomAdapter {
            gotoRepositoryFragment(it)
        }
        observerViewModel(viewModel,adapter)
        binding.searchInputText
            .setOnEditorActionListener{ editText, action, _ ->
                if (action== EditorInfo.IME_ACTION_SEARCH){
                    editText.text.toString().let {
                        viewModel.searchResults(it)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also{
            it.layoutManager= layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter= adapter
        }
    }

    private fun observerViewModel(viewModel: OneViewModel, adapter: CustomAdapter) {
        viewModel.searchResult.observe(viewLifecycleOwner, {
             adapter.submitList(it)
        })
        viewModel.searchError.observe(viewLifecycleOwner,{
            if (it == true){
                Toast.makeText(requireContext(),"Net work error!!",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun gotoRepositoryFragment(Item: Item)
    {
        val action= OneFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item= Item)
        findNavController().navigate(action)
    }
}

val diffUtil= object: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem.name== newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem== newItem
    }

}

class CustomAdapter(
   private val itemClick: (Item)->Unit,
) : ListAdapter<Item, CustomAdapter.ViewHolder>(diffUtil){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
    	val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
    	return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
    	val item= getItem(position)
        item.name.also { (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text = it }

    	holder.itemView.setOnClickListener{
     		itemClick(item)
    	}
    }
}
