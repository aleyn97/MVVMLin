# MVVMLin

一个基于MVVM用Kotlin+Retrofit+协程+Databinding(ViewBinding)+LiveData来封装的快速开发框架：
项目地址：[MVVMLin](https://github.com/AleynP/MVVMLin)

## 框架简介

- **使用技术**
  基于MVVM模式用了 kotlin+协程+retrofit+livedata+DataBinding
- **基本封装**
  封装了BaseActivity、BaseFragment、BaseViewModel基于协和的网络请方式更加方便，考虑到有些小伙伴不太喜欢用DataBinding在xml中绑定数据的方式，也提供了相应的适配，两种方式自行选择。Retrofit2.6及以上版本提供了对协程的支持，使用起来更加方便.
  增加了 Flow 的转换
- **引入第三方库**

[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)：包含了大量的常用工具类，简直是必备神器啊。

[material-dialogs](https://github.com/afollestad/material-dialogs)：弹窗

[coil](https://github.com/coil-kt/coil)：图片加载(更适合KT的图片加载)

[Retrofit](https://github.com/square/retrofit)：网络请求

## 使用方式

### 启用dataBinding

在主工程app的build.gradle的android {}中加入：

``` groovy
    buildFeatures {
        //dataBinding = true
        viewBinding = true
    }
```

### 依赖

在主项目app的build.gradle中依赖

``` groovy
dependencies {
    ...
   implementation 'com.github.AleynP:MVVMLin:2.0.0'
}
```

或者 下载到本地导入Module

#### 配置 buildSrc (1.0.6 版本改为 butkdSrc 方式构建)

复制Demo的 buildSrc 到根目录,用Gradle 同步。AS会自动识别 buildSrc目录 (远程依赖可以忽略)

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

继承BaseVMActivity

``` kotlin
class DetailActivity : 继承BaseVMActivity<NoViewModel, ViewBinding>() {

	override val layoutId() = R.layout.activity_detail

	override fun initView(savedInstanceState: Bundle?) {
       ....
    }

    override fun initData() {
      ....
    }
}
```

第一个泛型是ViewModel,如果页面很简单不需要ViewModel，可以继承BaseActivity。 第二个泛型是Databinding 或者
ViewBinding
的生成类，如果页面使用Databinding或者ViewBinding的话，就要传对应的Binding生成类，如果不使用DataBinding或者ViewBinding，传
ViewBinding
。基类不用初始化mBinding而会使用常规方式。
** layoutId ** 方法返回对应布局。当使用ViewBinding时 不用重写此方法，其余都要重写 返回布局 Id
**initView()** 和 **initData()** 为默认实现，做初始化UI等操作

### Fragment

继承BaseVMFragment

``` kotlin
class HomeFragment : 继承BaseVMFragment<HomeViewModel, ViewBinding>() {

		override fun layoutId() = R.layout.home_fragment
		
		override fun initView(savedInstanceState: Bundle?) {  }
		
		override fun lazyLoadData() {
			....
		}
}
```

实现方法同Activity一样，Fragment多了懒加载方法**lazyLoadData()** 可选择性重写。

### ViewModel

继承BaseViewModel

``` kotlin
class HomeViewModel : BaseViewModel() {
		.........
}
```

在Application 中设置全局网络异常处理(可选)

```kotlin

MVVMLin.setNetException(CoroutineExceptionHandler { context, e ->
    //...
})

```

**ViewModel** 中所有网络请求都写要 BaseViewModel 的 `launch` 作用域中，如下：

#### Flow 方式(同 RxJava 相似)

```
// Service 用 Flow 接收
@GET("xxx/xxx")
fun getBannerData(): Flow<BaseResult<XXX>>>
```

``` kotlin
class HomeViewModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    private val _banners = MutableSharedFlow<List<BannerBean>>()
    val mBanners: SharedFlow<List<BannerBean>> = _banners
    fun getBanner(refresh: Boolean = false) {
        //只返回结果，其他全抛自定义异常
        launch {
            homeRepository.getBannerData(refresh)
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

注意不使用Flow时， Service 返回值直接用基类,并添加上 `suspend`

```
@GET("xxx/xxx")
suspend fun getBannerData(): BaseResult<XXX>
```

```
class ProjectViewModel : BaseViewModel() {
    fun getProjectList(cid: Int) {
        launch {
            homeRepository.getProjectList(page, cid).getOrThrow()
            .let {
                items.clear()
                items.addAll(it.datas)
            }
        }
    }
}
```

##### 转换符

以下几个是不使用 Flow 时用到的转换符：

- getOrThrow: 只取成功结果其他全抛异常
- check:检测是否成功

#### IBaseResponse

由于请求中依赖了数据基类，我们每个项目的基类字段都不相同，所以我们根据后台的字段定义完基类以后，要实现IBaseResponde接口,如下：

```
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

来保证过滤请求结果的正常使用

### 例子

Demo中只展示了三种列表使用方式

#### 不使用Databinging,结合[BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

详见Demo的 **HomeFragment**

#### 使用Databinging,结合[bindingcollectionadapter](https://github.com/evant/binding-collection-adapter)

结合bindingcollectionadapter不用写Adapter适配器了，详见Demo的 **ProjectFragment**

#### 使用Databinging,结合[BRVAH](https://github.com/evant/binding-collection-adapter)

[BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
对DataBinding也做了支持，详见Demo的 **
MeFragment**

## 最后

QQ群：(791382057)