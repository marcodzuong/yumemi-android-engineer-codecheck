/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

class TwoFragment : Fragment(R.layout.fragment_two) {

    private val args: TwoFragmentArgs by navArgs()

    private var binding: FragmentTwoBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("検索した日時", lastSearchDate.toString())
        binding = FragmentTwoBinding.bind(view)
        val item: Item = args.item
        item.run {
            _binding.ownerIconView.load(item.ownerIconUrl)
            _binding.nameView.text = item.name
            _binding.languageView.text = item.language
            _binding.starsView.text = getString(R.string.txt_count_stars, stargazersCount.toString())
            _binding.watchersView.text = getString(R.string.txt_count_watchers, watchersCount.toString())
            _binding.forksView.text = getString(R.string.txt_count_forks, forksCount.toString())
            _binding.openIssuesView.text = getString(R.string.txt_count_open_issues, openIssuesCount.toString())
        }

    }
}
