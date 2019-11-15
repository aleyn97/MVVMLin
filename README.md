# MVVMLin
一个基于MVVM用Kotlin+Retrofit+协程+Databinding+LiveData来封装的快速开发框架：
项目地址：[MVVMLin](https://github.com/AleynP/MVVMLin)

Github上关于MVVM的框架也不少，之前一直在用RxJava +Retrofit 用MVP模式来做项目，现在AndroidX 是大势所趋，Kotlin已经成官方语言两年了，今年GoogleIO大会又出了新东西，哎~~~~学不动了呀。近期项目不太忙，把这几个新东西结合起来，封装了一个MVVM的框架，分享出来给大家献丑了。
抛弃了强大的RxJava，心里还是有点虚的。
### 框架简介
 - **使用技术**
 基于MVVM模式用了 kotlin+协程+retrofit+livedata+DataBinding
 - **基本封装**
 封装了BaseActivity、BaseFragment、BaseViewModel基于协和的网络请方式更加方便，考虑到有些小伙伴不太喜欢用DataBinding在xml中绑定数据的方式，也提供了相应的适配，两种方式自行选择。Retrofit2.6提供了对协程的支持，使用起来更加方便，不用考虑类型的转换了。
- **特点**
使用Rxjava 处理不好的话会有内存泄露的风险，我们会用使用**AutoDispose、RxLifecycle**等方式来处理，但是使用协程来请求数据，完全不用担心这个问题，所有请求都是在**viewModelScope**中启动，当页面销毁的时候，会统一取消，不用关心这个问题了。用Kotlin封装，大量语法糖可使用。
- 	**引入第三方库**
[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)：包含了大量的常用工具类，简直是必备神器啊。
[material-dialogs](https://github.com/afollestad/material-dialogs)：弹窗
[glide](https://github.com/bumptech/glide)：图片加载
[Retrofit](https://github.com/bumptech/glide)：网络请求
### 1，如何使用
##### 1.1 启用databinding
在主工程app的build.gradle的android {}中加入：
```
dataBinding {
    enabled true
}
```
#### 1.2 依赖
在主项目app的build.gradle中依赖
```
dependencies {
    ...
   implementation 'me.aleyn:MVVMLin:1.0'
}
```
或者 下载到本地导入Module
#### 1.3 配置依赖版本文件 config.gradle
复制Demo的 config,gradle 到要目录，在项目的build.gradle 中加入：
```
apply from: "config.gradle"
```
### 2，快速开始
#### 2.1Activity
继承BaseActivity
```
class DetailActivity : BaseActivity<NoViewModel, ViewDataBinding>() {
	override fun layoutId() = R.layout.activity_detail

	override fun initView(savedInstanceState: Bundle?) {
       ....
    }

    override fun initData() {
      ....
    }
}
```
第一个泛型是VIewModel,如果页面很简单不需要ViewModel，直接传入NoViewModel即可。
第二个泛型是Databinding,如果页面使用Databinding的话，就要传对应生成的Binding类，如果这个页面不使用DataBinding，传ViewDataBinding基类不用初始化mBinding而会使用常规方式。
 **layoutId()** 方法返回对应布局
 **initView()** 和 **initData()** 为默认实现，做初始化UI等操作
##### 2.2 Fragment
继承BaseFragment
```
class HomeFragment : BaseFragment<HomeViewModel, ViewDataBinding>() {
		override fun layoutId() = R.layout.home_fragment
		override fun initView(savedInstanceState: Bundle?) {  }
		override fun lazyLoadData() {
			....
		}
}
```
实现方法同Activity一样，Fragment多了懒加载方法**lazyLoadData()** 可选择性重写。
 **setUserVisibleHint()** 方法已经被弃用，懒加载使用新的方式实现。

同样Fragment中如果想不使用Databinding，泛型传**ViewDataBinding**：

2，使用DataBinding,布局文件：
```
<layout>
    <data>
    .....
    </data>
    .....
</layout>
```

