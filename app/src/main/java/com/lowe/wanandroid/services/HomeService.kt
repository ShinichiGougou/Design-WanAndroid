package com.lowe.wanandroid.services

import com.lowe.wanandroid.base.http.adapter.NetworkResponse
import com.lowe.wanandroid.services.model.Article
import com.lowe.wanandroid.services.model.Banner
import com.lowe.wanandroid.services.model.PageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService : BaseService {

    /**
     * 首页banner
     */
    @GET("banner/json")
    suspend fun getBanner(): NetworkResponse<List<Banner>>

    /**
     * 首页文章
     */
    @GET("article/list/{pageNo}/json")
    suspend fun getArticlePageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageResponse<Article>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    suspend fun getArticleTopList(): NetworkResponse<List<Article>>

    /**
     * 广场文章
     */
    @GET("user_article/list/{pageNo}/json")
    suspend fun getSquarePageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageResponse<Article>>

    /**
     * 问答列表
     */
    @GET("wenda/list/{pageNo}/json")
    suspend fun getAnswerPageList(@Path("pageNo") pageNo: Int): NetworkResponse<PageResponse<Article>>

    /**
     * 项目列表
     */
    @GET("article/listproject/{pageNo}/json")
    suspend fun getProjPageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageResponse<Article>>
}