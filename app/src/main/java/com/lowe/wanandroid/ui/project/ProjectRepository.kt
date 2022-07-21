package com.lowe.wanandroid.ui.project

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.lowe.wanandroid.base.IntKeyPagingSource
import com.lowe.wanandroid.base.http.adapter.getOrNull
import com.lowe.wanandroid.services.BaseService
import com.lowe.wanandroid.services.ProjectService
import javax.inject.Inject

class ProjectRepository @Inject constructor(private val service: ProjectService) {

    suspend fun getProjectTitleList() = service.getProjectTitleList()

    /**
     * 项目列表Flow
     */
    fun getProjectListFlow(pageSize: Int, categoryId: Int) =
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                service = service
            ) { service, page, size ->
                service.getProjectPageList(page, size, categoryId).getOrNull()?.datas ?: emptyList()
            }
        }.flow
}