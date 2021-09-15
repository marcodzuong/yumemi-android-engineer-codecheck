/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.features.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

class TwoFragment : Fragment() {

    private val args: TwoFragmentArgs by navArgs()
    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("検索した日時", lastSearchDate.toString())
        val item: Item = args.item
        item.run {
            binding.ownerIconView.load(ownerIconUrl)
            binding.nameView.text = name
            binding.languageView.text = getString(R.string.written_language,language)
            binding.starsView.text = getString(R.string.txt_count_stars, stargazersCount.toString())
            binding.watchersView.text =
                getString(R.string.txt_count_watchers, watchersCount.toString())
            binding.forksView.text = getString(R.string.txt_count_forks, forksCount.toString())
            binding.openIssuesView.text =
                getString(R.string.txt_count_open_issues, openIssuesCount.toString())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
