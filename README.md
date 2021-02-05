# MVVMLin
一个基于MVVM用Kotlin+Retrofit+协程+Databinding(ViewBinding)+LiveData来封装的快速开发框架：
项目地址：[MVVMLin](https://github.com/AleynP/MVVMLin)

### 框架简介
 - **使用技术**
 基于MVVM模式用了 kotlin+协程+retrofit+livedata+DataBinding
 - **基本封装**
 封装了BaseActivity、BaseFragment、BaseViewModel基于协和的网络请方式更加方便，考虑到有些小伙伴不太喜欢用DataBinding在xml中绑定数据的方式，也提供了相应的适配，两种方式自行选择。Retrofit2.6及以上版本提供了对协程的支持，使用起来更加方便，不用考虑类型的转换了。
- **特点**
使用Rxjava 处理不好的话会有内存泄露的风险，我们会用使用**AutoDispose、RxLifecycle**等方式来处理，但是使用协程来请求数据，完全不用担心这个问题，所有请求都是在**viewModelScope**中启动，当页面销毁的时候，会统一取消，不用关心这个问题了。用Kotlin封装，大量语法糖可心使用。
- 	**引入第三方库**

[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)：包含了大量的常用工具类，简直是必备神器啊。

[material-dialogs](https://github.com/afollestad/material-dialogs)：弹窗

~~[glide](https://github.com/bumptech/glide)：图片加载(弃用1.0.5)~~

[coil](https://github.com/coil-kt/coil)：图片加载(更适合KT的图片加载)

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
   implementation 'me.aleyn:MVVMLin:1.0.6'
}
```
或者 下载到本地导入Module
#### 1.3 配置 buildSrc (1.0.6 版本改为 butkdSrc 方式构建)
复制Demo的 buildSrc 到根目录,用Gradle 同步。AS会自动识别 buildSrc目录 (远程依赖可以忽略)

### 2，快速开始
#### 2.1Activity
继承BaseActivity
```
class DetailActivity : BaseActivity<NoViewModel, ViewBinding>() {
	override fun layoutId() = R.layout.activity_detail

	override fun initView(savedInstanceState: Bundle?) {
       ....
    }

    override fun initData() {
      ....
    }
}
```
第一个泛型是ViewModel,如果页面很简单不需要ViewModel，直接传入NoViewModel即可。
第二个泛型是Databinding 或者 ViewBinding 的生成类，如果页面使用Databinding或者ViewBinding的话，就要传对应的Binding生成类，如果不使用DataBinding或者ViewBinding，传 ViewBinding 。基类不用初始化mBinding而会使用常规方式。
 **layoutId()** 方法返回对应布局。当使用ViewBinding时 不用重写此方法，其余都要重写 返回布局 Id
 **initView()** 和 **initData()** 为默认实现，做初始化UI等操作
##### 2.2 Fragment
继承BaseFragment
```
class HomeFragment : BaseFragment<HomeViewModel, ViewBinding>() {
		override fun layoutId() = R.layout.home_fragment
		override fun initView(savedInstanceState: Bundle?) {  }
		override fun lazyLoadData() {
			....
		}
}
```
实现方法同Activity一样，Fragment多了懒加载方法**lazyLoadData()** 可选择性重写。
 **setUserVisibleHint()** 方法已经被弃用，懒加载使用新的方式实现。

同样Fragment中如果想不使用Databinding或者ViewBinding，泛型传**ViewBinding**：

2，使用DataBinding,布局文件：
```
<layout>
    <data>
    .....
    </data>
    .....
</layout>
```


泛型传对应生成Binding类：
```
class ProjectFragment : BaseFragment<ProjectViewModel, ProjectFragmentBinding>() {
		.........
}
```
##### 2.3 ViewModel
继承BaseViewModel
```
class HomeViewModel : BaseViewModel() {
		.........
}
```
如果一个页面内容很少，不需要ViewModel，我们可能不想再建一个ViewModel类，泛型传**NoViewModel**即可。
**BaseVIewModel** 中对协程进行了简单封装，**BaseViewMode** 已经做了对网络请求异常的统一处理。比如我们的网络请求可以这样写：
```
class HomeViewModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    val mBanners = MutableLiveData<List<BannerBean>>()
    fun getBanner() {
    	//只返回结果，其他全抛自定义异常
        launchOnlyresult({ homeRepository.getBannerData() }, {
            mBanners.value = it
        })
    }
}
```
那如果我们想自己处理错误怎么办？
```
class HomeViewModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    val mBanners = MutableLiveData<List<BannerBean>>()
    fun getBanner() {
        launchOnlyresult({ homeRepository.getBannerData() }, {
            mBanners.value = it
        },{
         // 这里是Error 返回   ()
        })
    }
}
```
只需要加一个方法参数就行了。

另一种不过滤返回结果的方式：
```
class MeViewModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    var popularWeb = MutableLiveData<List<UsedWeb>>()
    fun getPopularWeb() {
        launch({
            val result = homeRepository.getPopularWeb()  //
            if (result.isSuccess()) {
                popularWeb.value = result.data
            }
        })
    }
}
```
要自己处理Error 跟上面一样，加一个方法参数就行了。

每个网络请求都会加等待框，如果我们不想要等待框：
```
 fun getProjectType() {
        launchOnlyresult({
            homeRepository.getNaviJson()
        }, {
            navData.addAll(it)
            it.forEach { item ->
                navTitle.add(item.name)
            }
        }, isShowDialog = false)
 }
```
isShowDialog 传false，默认是true

用Flow流的方式：
```
fun getFirstData() {
        launchUI {
            launchFlow { homeRepository.getNaviJson() }
                .flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        navData.addAll(it.data)
                        it.data.forEach { item -> navTitle.add(item.name) }
                        launchFlow { homeRepository.getProjectList(page, it.data[0].id) }
                    } else throw ResponseThrowable(it.errorCode, it.errorMsg)
                }
                .onStart { defUI.showDialog.postValue(null) }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.dismissDialog.call() }
                .catch {
                    // 错误处理
                    val err = ExceptionHandle.handleException(it)
                    LogUtils.d("${err.code}: ${err.errMsg}")
                }
                .collect {
                    if (it.isSuccess()) items.addAll(it.data.datas)
                }
        }

    }
```
##### 2.4 IBaseResponse
由于请求中依赖了基类，我们每个项目的基类字段都不相同，所以我们根据后台的字段定义完基类以后，要实现IBaseResponde接口,如下：
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

### 3，例子
Demo中只展示了三种列表使用方式
##### 3.1 不使用Databinging,结合[BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
详见Demo的 **HomeFragment**
##### 3.2 使用Databinging,结合[bindingcollectionadapter](https://github.com/evant/binding-collection-adapter)
结合bindingcollectionadapter不用写Adapter适配器了，详见Demo的 **ProjectFragment**

##### 3.2 使用Databinging,结合[BRVAH](https://github.com/evant/binding-collection-adapter)
[BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper) 对DataBinding也做了支持，详见Demo的 **MeFragment**

### 4，关于框架
后续会进一步完善，大家多提意见

QQ群：(791382057)