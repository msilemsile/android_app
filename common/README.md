//初始化

1.克隆当前项目到新建的App下作为module使用

2.将当前项目下的"ext.common.gradle"文件引入到新建的App的根build.gradle文件中

代码如下:
apply from:("./common/ext.common.gradle")

示例如下:

[build.gradle](https://github.com/msilemsile/android_app/blob/master/build.gradle)

[示例参考项目](https://github.com/msilemsile/android_app/)
