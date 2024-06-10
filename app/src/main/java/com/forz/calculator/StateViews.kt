package com.forz.calculator

object StateViews{
    var currentItemPager = 1
    var pagerIsRecreated = false

    var oldSizeRecyclerViewHistory = 0
    var newSizeRecyclerViewHistory = 0
    var currentPositionRecyclerViewHistory = 0
    var recyclerViewHistoryElementIsAdded = false
    var recyclerViewHistoryElementIsDeleted = false
    var firstStartRecyclerViewHistory = true

    var expressionEditTextIsRecreated = false
}