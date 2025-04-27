# MVVMLin

一个基于MVVM用Kotlin+Retrofit+协程+ViewBinding+Flow来封装的快速开发框架：
项目地址：[MVVMLin](https://github.com/AleynP/MVVMLin)

2.0.1 版本后不再支持 DataBinding, 原因是 DataBinding 需要 kapt 插件的支持, 目前有速度更快的 KSP 来替代
kapt,但是 KSP 压根没打算支持DataBinding。Google 也在放弃DataBinding 了。猜测原因是有更好用的
Compose 出来了，就不再对 DataBinding 投入太多工夫了

## 框架简介

- **使用技术**
  基于MVVM模式用了 kotlin+协程+retrofit+livedata+ViewBinding
- **基本封装**
  封装了BaseActivity、BaseFragment、BaseViewModel基于协程的网络请求方式。Retrofit2.6及以上版本提供了对协程的支持，使用起来更加方便.
  增加了 Flow 的转换
- **引入第三方库**

[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)：包含了大量的常用工具类，简直是必备神器啊。

[material-dialogs](https://github.com/afollestad/material-dialogs)：弹窗

[coil](https://github.com/coil-kt/coil)：图片加载(更适合KT的图片加载)

[Retrofit](https://github.com/square/retrofit)：网络请求

## 使用方式

### 启用viewBinding

在主工程app的build.gradle的android {}中加入：

```groovy
    buildFeatures {
        viewBinding = true
    }
```

### 依赖

[![](https://jitpack.io/v/aleyn97/MVVMLin.svg)](https://jitpack.io/#aleyn97/MVVMLin)

在主项目app的build.gradle中依赖

```groovy
dependencies {
    //...
   implementation 'com.github.aleyn97:MVVMLin:lastVersion'
}
```

或者 下载到本地导入Module

#### 添加 FlowAdapter

```kotlin
    Retrofit.Builder()
    .addCallAdapterFactory(FlowAdapterFactory.create(true))
    //...
    .build()
```

如上添加 `FlowAdapter`后， Retrofit 可直接返回 Flow 类型。

## 快速开始

### Activity

1，继承BaseActivity

```kotlin
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val viewModel by viewModels<XXXViewModel>()

	override fun initView(savedInstanceState: Bundle?) {
       //...
       mBinding.tvTest.text = "123456"
    }

    override fun initData() {
      //....
    }
}
```

**initView()** 和 **initData()** 为默认实现，做初始化UI等操作

2，继承BaseVMActivity

```kotlin
class XXXActivity : BaseVmActivity<XXXViewModel,ActivityDetailBinding>() {

	override fun initView(savedInstanceState: Bundle?) {
       //...
    }

    override fun initData() {
      //....
    }
}
```

不推荐继承 BaseVmActivity 此种方式，后续会移除掉

### Fragment

1，继承BaseFragment

```kotlin
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

        private val viewModel by viewModels<HomeViewModel>()
        
        //private val mainViewModel by activityViewModels<HomeViewModel>() // Activity 共享
        
        //private val viewModel3 by viewModels<HomeViewModel>({ requireParentFragment() }) // 父Fragmeng共享

		override fun initView(savedInstanceState: Bundle?) {
		    //...
           registerDefUIChange(viewModel)//绑定默认UI 事件(可选)
		}
		
		override fun lazyLoadData() {
			//....
		}
}
```

2，继承BaseVMFragment

```kotlin
class HomeFragment : BaseVmFragment<HomeViewModel,HomeFragmentBinding>() {

		override fun initView(savedInstanceState: Bundle?) {
		    //...
		}
		
		override fun lazyLoadData() {
			//...
		}
}
```

不推荐再使用这种写法，这种只适合页面简单只有一个ViewModel，如果有多个页面有多个viewModel,或者有共享
viewModel。使用第1种

### ViewModel

继承BaseViewModel

```kotlin
class HomeViewModel : BaseViewModel() {

  /**
   * 示例
   * 使用 MutableSharedFlow, MutableStateFlow。 或者 LiveData 都可以
   
   * MutableSharedFlow 只有目前存在观察都才会通知，没有缓存
   * 
   * MutableStateFlow 每次注册都会触发通知，是有缓存值在的，所以 MutableStateFlow 初始化是要给默认值的
   * 
   */
  private val _banners = MutableSharedFlow<List<BannerBean>>()
  val banners = _banners.asSharedFlow()

  //private val _banners = MutableStateFlow<List<BannerBean>>(emptyList())
  //val banners = _banners.asStateFlow()

    init {
      repository.getBannerData(refresh)
        .asResponse()
        .netCache {
          //异常处理
        }
        .collect(_banners)
    }
  
    //...
}
```

### 更新UI

```kotlin
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override fun initObserve() {
        launch {
          viewModel.banners
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect {
                  // Update Banner UI
                  // mBinding.banner.setList(it)
                }
        }
      
      /**
       * 这种写法等同于上边 flowWithLifecycle 写法
       */
        flowLaunch {
            viewModel.banners.collect {
                // Update Banner UI
                // mBinding.banner.setList(it)
            }
        }
      
      /**
       * 可指定 state, 默认是 Lifecycle.State.STARTED
       * 不同的 state 观察者移除的时机不同，根据业务场景自行选择
       */
      flowLaunch(state = Lifecycle.State.CREATED) {
        viewModel.banners.collect {
          // Update Banner UI
          // mBinding.banner.setList(it)
        }
      }
    }

}
```

#### 异常设置
在Application 中设置全局网络异常处理(可选)

```kotlin

MVVMLin.setNetException(CoroutineExceptionHandler { context, e ->
    //...
})

```

**ViewModel** 中所有网络请求都写要 BaseViewModel 的 `launch` 作用域中，如下：

#### Flow 方式

```kotlin
// Service 用 Flow 接收
@GET("xxx/xxx")
fun getBannerData(): Flow<BaseResult<XXX>>
```

```kotlin
class XXXViewModel : BaseViewModel() {
    
    private val xxxRepository by lazy { InjectorUtil.getHomeRepository() }
  
    private val _banners = MutableSharedFlow<List<BannerBean>>()
    val mBanners: SharedFlow<List<BannerBean>> = _banners
  
    fun getBanner(refresh: Boolean = false) {
        //Flow 方式示例
        launch { 
          xxxRepository.getBannerData(refresh)
                .asResponse()
                .bindLoading(this@HomeViewModel)//绑定Lodaing
                .collect(_banners)
        }
    }
}
```

##### 操作符

以下几个是用 Flow 时用到的操作符：

- asResponse: 只取成功结果其他全抛异常
- asSuccess:只做成功处理，不关心结果
- bindLoading: 绑定Loading
- netCache: 自定义错误处理,返回的是 ResponseThrowable

#### 普通方式(不使用Flow)

注意不使用 Flow 时， Service 返回值直接用基类,并添加上 `suspend`

```kotlin
@GET("xxx/xxx")
suspend fun getBannerData(): BaseResult<XXX>
```

```kotlin
class ProjectViewModel : BaseViewModel() {

    private val _projectList = MutableStateFlow<List<XXX>>(emptyList())
    val projectList = _project.asStateFlow()

    fun getProjectList(cid: Int) {
        launch {
            //普通方式示例
            val list = homeRepository.getProjectList(page, cid).getOrThrow()
            //val list = homeRepository.getProjectList(page, cid).check() //不关心返回值
            _projectList.update { list }// Update UI
        }
    }
}
```

##### 转换符

以下几个是不使用 Flow 时用到的转换符：

- getOrThrow: 只取成功结果其他全抛异常
- check:检测是否成功

#### IBaseResponse

由于请求中依赖了数据基类，我们每个项目的基类字段都不相同，所以我们根据后台的字段定义完基类以后，要实现IBaseResponse接口,如下：

```kotlin
data class BaseResult<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
) : IBaseResponse<T> {

    override fun code() = errorCode

    override fun msg() = errorMsg

    override fun data() = data

    override fun isSuccess() = errorCode == 0
}
```

## 最后

这个框架是 19 年底开始搞的，那时候协程刚出来大家都不熟悉，以及 Jetpack 相关的东西。最初目的也是给大家提供一个参考方式，如何入门使用。如今 2025 年了，回过头来再看这些东西，会感觉这些已经是很基础的了。
所以这套东西，比较适合一些快速开发的小项目使用。如果满足不了使用需求的话，大家稍微动下手，自己封装下，只当做一个参考就可以了。


QQ群：(791382057)