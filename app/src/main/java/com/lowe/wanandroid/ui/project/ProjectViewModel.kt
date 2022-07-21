package com.lowe.wanandroid.ui.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.lowe.wanandroid.base.http.adapter.getOrElse
import com.lowe.wanandroid.services.model.ProjectTitle
import com.lowe.wanandroid.ui.BaseViewModel
import com.lowe.wanandroid.ui.project.child.ProjectChildFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val repository: ProjectRepository) :
    BaseViewModel() {

    val projectTitleListLiveData = liveData<List<ProjectTitle>> {
        emit(
            mutableListOf<ProjectTitle>().apply {
                addAll(repository.getProjectTitleList().getOrElse { emptyList() })
            }
        )
    }
    val parentRefreshLiveData = MutableLiveData<Int>()
    val scrollToTopLiveData = MutableLiveData<Int>()
}