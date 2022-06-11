package com.lowe.wanandroid.ui.navigator.child.tutorial

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.lowe.wanandroid.services.success
import com.lowe.wanandroid.ui.BaseViewModel
import com.lowe.wanandroid.ui.launch
import com.lowe.wanandroid.ui.navigator.NavigatorDiffCalculator
import com.lowe.wanandroid.ui.navigator.NavigatorRepository

class TutorialChildViewModel : BaseViewModel() {

    val tutorialListLiveData = MutableLiveData<Pair<List<Any>, DiffUtil.DiffResult>>()

    override fun start() {
        fetchTutorialList()
    }

    fun fetchTutorialList() {
        launch({
            val tutorials = NavigatorRepository.getTutorialList().success()?.data ?: emptyList()
            tutorialListLiveData.value =
                getDiffResultPair(tutorialListLiveData.value?.first ?: emptyList(), tutorials)
        })
    }

    private fun getDiffResultPair(oldList: List<Any>, newList: List<Any>) =
        newList to DiffUtil.calculateDiff(
            NavigatorDiffCalculator.getNavigatorDiffCalculator(
                oldList,
                newList
            )
        )
}