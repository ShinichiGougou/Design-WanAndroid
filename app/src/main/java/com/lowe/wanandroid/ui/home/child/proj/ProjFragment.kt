package com.lowe.wanandroid.ui.home.child.proj

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.lowe.multitype.PagingLoadStateAdapter
import com.lowe.multitype.PagingMultiTypeAdapter
import com.lowe.wanandroid.R
import com.lowe.wanandroid.base.app.AppViewModel
import com.lowe.wanandroid.compat.BundleCompat
import com.lowe.wanandroid.databinding.FragmentHomeChildProjBinding
import com.lowe.wanandroid.services.model.Article
import com.lowe.wanandroid.services.model.CollectEvent
import com.lowe.wanandroid.ui.ArticleDiffCalculator
import com.lowe.wanandroid.ui.BaseFragment
import com.lowe.wanandroid.ui.SimpleFooterItemBinder
import com.lowe.wanandroid.ui.home.HomeChildFragmentAdapter
import com.lowe.wanandroid.ui.home.HomeFragment
import com.lowe.wanandroid.ui.home.HomeTabBean
import com.lowe.wanandroid.ui.home.HomeViewModel
import com.lowe.wanandroid.ui.home.item.ArticleAction
import com.lowe.wanandroid.ui.home.item.HomeArticleItemBinderV2
import com.lowe.wanandroid.ui.project.child.item.ProjectChildItemBinder
import com.lowe.wanandroid.ui.web.WebActivity
import com.lowe.wanandroid.utils.Activities
import com.lowe.wanandroid.utils.isEmpty
import com.lowe.wanandroid.utils.isRefreshing
import com.lowe.wanandroid.utils.launchRepeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Description: java类作用描述
 * @Author: Kirito qinglingou@gmail.com
 * @CreateDate: 2022/7/21 10:26
 */
@AndroidEntryPoint
class ProjFragment :
    BaseFragment<ProjViewModel, FragmentHomeChildProjBinding>(R.layout.fragment_home_child_proj) {

    companion object {
        fun newInstance(homeTabBean: HomeTabBean) = with(ProjFragment()) {
            arguments = bundleOf(
                HomeFragment.KEY_CHILD_HOME_TAB_PARCELABLE to homeTabBean
            )
            this
        }
    }

    @Inject
    lateinit var appViewModel: AppViewModel

    private val homeViewModel by viewModels<HomeViewModel>(this::requireParentFragment)
    private val squareTabBean by lazy(LazyThreadSafetyMode.NONE) {
        BundleCompat.getParcelable(arguments, HomeFragment.KEY_CHILD_HOME_TAB_PARCELABLE)
            ?: HomeTabBean(HomeChildFragmentAdapter.HOME_TAB_PROJ)
    }
    private val projAdapter =
        PagingMultiTypeAdapter(ArticleDiffCalculator.getCommonDiffItemCallback()).apply {
            register(ProjectChildItemBinder(this@ProjFragment::onItemClick))
        }

    override val viewModel: ProjViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvents()
    }

    private fun initView() {
        viewDataBinding.apply {
            with(projList) {
                layoutManager = LinearLayoutManager(context)
                adapter = projAdapter.withLoadStateFooter(
                    PagingLoadStateAdapter(
                        SimpleFooterItemBinder(),
                        projAdapter.types
                    )
                )
                setHasFixedSize(true)
            }
        }
    }

    private fun initEvents() {
        launchRepeatOnStarted {
            launch {
                viewModel.getProjListFlow.collectLatest(projAdapter::submitData)
            }
            launch {
                projAdapter.loadStateFlow.collect(this@ProjFragment::updateLoadState)
            }
        }
        homeViewModel.apply {
            scrollToTopLiveData.observe(viewLifecycleOwner) {
                if (it.title == squareTabBean.title) scrollToTop()
            }
            refreshLiveData.observe(viewLifecycleOwner) {
                if (it.title == squareTabBean.title) projAdapter.refresh()
            }
        }
        appViewModel.collectArticleEvent.observe(viewLifecycleOwner) {
            projAdapter.snapshot().run {
                val index = indexOfFirst { it is Article && it.id == it.id }
                if (index >= 0) {
                    (this[index] as? Article)?.collect = it.isCollected
                    index
                } else null
            }?.apply(projAdapter::notifyItemChanged)
        }
    }

    private fun scrollToTop() {
        viewDataBinding.projList.scrollToPosition(0)
    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> {
                WebActivity.loadUrl(
                    requireContext(), Activities.Web.WebIntent(
                        articleAction.article.link,
                        articleAction.article.id,
                        articleAction.article.collect
                    )
                )
            }
            is ArticleAction.CollectClick -> {
                appViewModel.articleCollectAction(
                    CollectEvent(
                        articleAction.article.id,
                        articleAction.article.link,
                        articleAction.article.collect.not()
                    )
                )
            }
            else -> {}
        }
    }

    private fun updateLoadState(loadStates: CombinedLoadStates) {
        viewDataBinding.loadingContainer.apply {
            emptyLayout.isVisible =
                loadStates.refresh is LoadState.NotLoading && projAdapter.isEmpty()
            loadingProgress.isVisible = loadStates.isRefreshing && projAdapter.isEmpty()
        }
    }
}