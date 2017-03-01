# StickyNestedScroll

实现嵌套滑动的两种方式：
第一种，StickyNavLayout.java
    重写 dispatchTouchEvent，onInterceptTouchEvent，onTouchEvent来实现

第二种，StickyNavLayout2.java
    通过实现v4包提供的四个接口：
        NestedScrollingParent
        NestedScrollingChild
        NestedScrollingParentHelper
        NestedScrollingChildHelper
    来处理嵌套，常见的SubView有CoordinatorLayout(配合Behavior的使用)，RecyclerView，NestedScrollView等
