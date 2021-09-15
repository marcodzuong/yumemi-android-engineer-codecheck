package jp.co.yumemi.android.code_check.common.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.navigation.NavigationCommand

abstract class BaseFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation(getViewModel())
    }
    open fun getViewModel(): BaseViewModel?= null

    private fun observeNavigation(viewModel: BaseViewModel?) {
        viewModel?.navigation?.observe(viewLifecycleOwner, {
            it?.let { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(
                        command.directions,getExtras())
                    is NavigationCommand.Back -> findNavController().navigateUp()
                }
            }
        })
    }

    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()
}