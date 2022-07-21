package com.lowe.wanandroid.services.impl

import com.lowe.wanandroid.base.http.RetrofitManager
import com.lowe.wanandroid.services.HomeService
import javax.inject.Inject

class HomeServiceImpl @Inject constructor() : HomeService {

    private val service by lazy { RetrofitManager.getService(HomeService::class.java) }

    override suspend fun getBanner() = service.getBanner()

    override suspend fun getArticleTopList() = service.getArticleTopList()

    override suspend fun getArticlePageList(pageNo: Int, pageSize: Int) =
        service.getArticlePageList(pageNo, pageSize)

    override suspend fun getSquarePageList(pageNo: Int, pageSize: Int) =
        service.getSquarePageList(pageNo, pageSize)

    override suspend fun getAnswerPageList(pageNo: Int) = service.getAnswerPageList(pageNo)

    override suspend fun getProjPageList(pageNo: Int, pageSize: Int) = service.getProjPageList(pageNo, pageSize)
}