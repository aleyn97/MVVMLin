package com.pcl.mvvm.network.entity

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */

data class ArticlesBean(
    /**
     * apkLink : http://www.wanandroid.com/blogimgs/e8faab6b-ecb1-4bc2-af96-f7e5039032b3.apk
     * author : GcsSloop
     * chapterId : 294
     * chapterName : 完整项目
     * collect : false
     * courseId : 13
     * desc : Diycode 社区客户端，可以更方便的在手机上查看社区信息。应用采用了数据多级缓存，并且实现了离线浏览(访问过一次的页面会被缓存下来，没有网络也可查看)，相比于网页版，在一定程度上可以减少在手机上访问的流量消耗。由于目前功能尚未完善，还存在一些已知或未知的bug，所以当前版本仅为 beta 测试版。
     * envelopePic : http://www.wanandroid.com/blogimgs/8876bcc1-7d12-4443-bf95-3f9a698685a6.png
     * id : 2241
     * link : http://www.wanandroid.com/blog/show/2033
     * niceDate : 2018-01-29
     * origin :
     * projectLink : https://github.com/GcsSloop/diycode
     * publishTime : 1517236491000
     * title : 【开源完整项目】diycode客户端
     * visible : 1
     * zan : 0
     */
    var apkLink: String? = null,
    var author: String? = null,
    var chapterId: Int = 0,
    var chapterName: String? = null,
    var isCollect: Boolean = false,
    var courseId: Int = 0,
    var desc: String? = null,
    var envelopePic: String? = null,
    var id: Int = 0,
    var originId: Int = -1,    // 收藏文章列表里面的原始文章id
    var link: String? = null,
    var niceDate: String? = null,
    var origin: String? = null,
    var projectLink: String? = null,
    var publishTime: Long = 0,
    var title: String? = null,
    var visible: Int = 0,
    var zan: Int = 0,
    var isFresh: Boolean = false,
    var isShowImage: Boolean = true,
    // 分类name
    var navigationName: String? = null
)