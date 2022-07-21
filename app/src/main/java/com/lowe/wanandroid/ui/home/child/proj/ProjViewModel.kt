package com.lowe.wanandroid.ui.home.child.proj

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lowe.wanandroid.ui.BaseViewModel
import com.lowe.wanandroid.ui.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description: 热门项目列表
 * @Author: Kirito qinglingou@gmail.com
 * @CreateDate: 2022/7/21 10:26
 */
@HiltViewModel
class ProjViewModel @Inject constructor(private val repository: HomeRepository) :
    BaseViewModel() {

    val getProjListFlow = repository.getProjPageList().cachedIn(viewModelScope)
}