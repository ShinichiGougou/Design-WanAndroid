package com.lowe.wanandroid.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lowe.wanandroid.MainViewModel
import com.lowe.wanandroid.R
import com.lowe.wanandroid.databinding.FragmentHomeBinding
import com.lowe.wanandroid.ui.BaseFragment
import com.lowe.wanandroid.ui.search.SearchActivity

/**
 * 首页Tab
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {

        const val KEY_CHILD_HOME_TAB_PARCELABLE = "key_child_tab_parcelable"

    }

    private lateinit var childAdapter: HomeChildFragmentAdapter

    private val mainViewModel by activityViewModels<MainViewModel>()

    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvents()
    }

    private fun initView() {
        childAdapter =
            HomeChildFragmentAdapter(generateHomeTabs(), this.childFragmentManager, lifecycle)
        viewDataBinding.apply {
            with(homeViewPager2) {
                adapter = childAdapter
            }
            with(searchIcon) {
                setOnClickListener {
                    startActivity(Intent(this@HomeFragment.context, SearchActivity::class.java))
                }
            }
            TabLayoutMediator(
                homeTabLayout,
                homeViewPager2
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = childAdapter.items[position].title
            }.apply(TabLayoutMediator::attach)
            with(swipeRefreshLayout) {
                setOnRefreshListener {
                    this@HomeFragment.viewModel.refreshEvent(childAdapter.items[viewDataBinding.homeViewPager2.currentItem])
                    this.isRefreshing = false
                }
            }
        }
    }

    private fun initEvents() {
        mainViewModel.apply {
            mainTabDoubleClickLiveData.observe(viewLifecycleOwner) {
                if (it == this@HomeFragment.tag){
                    viewModel.scrollToTopEvent(childAdapter.items[viewDataBinding.homeViewPager2.currentItem])
                }
            }
        }
    }

    private fun generateHomeTabs() = listOf(
        HomeTabBean(HomeChildFragmentAdapter.HOME_TAB_EXPLORE),
        HomeTabBean(HomeChildFragmentAdapter.HOME_TAB_SQUARE),
        HomeTabBean(HomeChildFragmentAdapter.HOME_TAB_ANSWER),
        HomeTabBean(HomeChildFragmentAdapter.HOME_TAB_PROJ),
    )
}