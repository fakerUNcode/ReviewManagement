# APP介绍

## 主要功能

本APP主要是用于复习题库的管理，可以根据不同科目来分类进行题库管理。具有课程和题库的增删改查功能，且具有题目的检索功能。

## 自定义风格

自定义了APP的某些提示吐司、按钮、字体、对话框、开屏动画和过渡效果等。

使用了[OppoSans](https://font.download/font/opposans)提供的字体风格。













# 功能介绍和操作说明

## 分辨率适配问题

以下测试均基于Pixel 7 pro 虚拟机（412dp * 892dp或1440px * 3120px），由于安卓布局的分辨率适配问题往往需要根据不同的手机定制，因此如遇到分辨率不适配以至于按钮等元素无法显示请更换分辨率设备测试，该应用在安卓设备三星Galaxy S10+(测试分辨率1520px * 720px , 2280px * 1080px，3040px * 1440px)和荣耀50pro（2676px * 1236px）均正常运行，一般来说在标准的720p,1080p,2k,4k,分辨率设备上均可正常运行，如无法正常运行请使用标准分辨率的手机或借用Android Studio的谷歌Pixel 7 pro等虚拟机测试。









## 开屏过渡动画



如果用户在应用的进程未运行（[冷启动](https://developer.android.com/topic/performance/vitals/launch-time?hl=zh-cn#cold)）或未创建 `Activity`（[温启动](https://developer.android.com/topic/performance/vitals/launch-time?hl=zh-cn#warm)）的情况下启动应用，则会发生以下事件：

1. 系统启动自定义的动画显示启动画面。
2. 当应用准备就绪时，会关闭启动画面并显示应用。

![Screenshot 2024-05-25 at 11.39.08](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405251139877.png?sleflearingnotes)











## 起始页

![Screenshot 2024-05-25 at 11.49.57](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405251150087.png?sleflearingnotes)动开屏动画结束后会进入起始页，点击”课程管理“按钮即可进入课程管理界面。













## 课程管理

### 课程的**CRUD**（增删改查）

如下多图所示，在**课程管理界面**，可以进行课程的**增删改查**。

并且在添加课程成功、编辑课程并保存成功、删除课程成功时，均有自定义风格的Toast提示。

#### 课程添加

![Screenshot 2024-05-24 at 18.51.53](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241851918.png?sleflearingnotes)

![image-20240524185625839](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241856864.png?sleflearingnotes)



#### 课程编辑

![Screenshot 2024-05-24 at 18.57.22](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241857108.png?sleflearingnotes)



#### 课程删除（会把课程中的所有题目一同删除）

![Screenshot 2024-05-24 at 18.58.50](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241858178.png?sleflearingnotes)

![Screenshot 2024-05-24 at 18.59.43](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241859093.png?sleflearingnotes)











## 根据课程分类的题库管理

### 题库管理的进入

在课程管理界面点击对应课程的”管理题库“按钮，即可进入该课程的独立题库界面。

![Screenshot 2024-05-24 at 19.05.31](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241905600.png?sleflearingnotes)











## 题库管理的CRUD（增删改查）功能

### 添加问题

点击右上角的icon图标即可添加题目（如果当前选中了一种类型的题目，会根据选中类型刷新界面，无需用户再次点击题目分类按钮）

用户可自定义问题的：

- 题目内容
- 问题答案
- 题目类型
  - 在选择题目类型的同时会提示用户刚才选择的题型

![Screenshot 2024-05-24 at 19.13.50](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241913583.png?sleflearingnotes)



保存成功，弹出提示。

![](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OSOS202405241917879.png?sleflearingnotes)







### 编辑问题

点击题库列表项中的编辑icon，即可进入编辑问题对话，和添加问题时的功能基本类似，保存成功同样有Toast提示，不再列出。（如果当前选中了一种类型的题目，会根据选中类型刷新界面，无需用户再次点击题目分类按钮）

![Screenshot 2024-05-24 at 19.19.25](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241919445.png?sleflearingnotes)







### 题目删除

点击题库管理列表项中的删除icon并在弹出的对话框中确认，即可删除对应的题目，同样有对应的Toast提示，不再列出。（如果当前选中了一种类型的题目，会根据选中类型刷新界面，无需用户再次点击题目分类按钮）

![Screenshot 2024-05-24 at 19.20.58](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241921238.png?sleflearingnotes)









## 题目的筛选

在题库管理界面的上方定义了按钮组，点击对应的按钮即可切换对应类型的题目界面。

### 筛选了单选题

![Screenshot 2024-05-24 at 19.24.22](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241924636.png?sleflearingnotes)

### 筛选了判断题

![Screenshot 2024-05-24 at 19.25.05](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241925206.png?sleflearingnotes)









## 题目的详细信息界面

在任意列表项中点击”详细信息“按钮，即可进入问题详细信息界面。

问题详细界面会列出如下信息：

- 问题内容
  - 设置了滚动视窗效果，当容器无法容纳全部文字信息时可以进行横向和纵向的滚动以查看所有的信息
- 问题的答案
  - 设置了滚动视窗效果，当容器无法容纳全部文字信息时可以进行横向和纵向的滚动以查看所有的信息
- 问题的类型

![Screenshot 2024-05-24 at 19.27.23](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241927005.png?sleflearingnotes)









## 题目的搜索

在课程题库管理界面点击”查找习题“按钮，即可进入题目搜索界面。用户输入需要检索的关键词后，会弹出搜索列表，列出搜索信息。该搜索不区分字母大小写，且与题目的筛选功能不同，会
检索数据库中的所有题目。

在这个界面的列表，也同样实现了查看问题信息、编辑、删除的操作，不再列出。

![Screenshot 2024-05-24 at 19.37.24](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405241937617.png?sleflearingnotes)











## 测试所需的用户权限

经测试，APP运行在Android12设备三星Galaxy S10+和虚拟机Android14设备上时无需任何权限即可运行。

![f313aec80eaeebaf9278f992db37c371_720](https://fakercodes.oss-cn-hangzhou.aliyuncs.com/sl/OS202405251200302.jpg?sleflearingnotes)

如果在其他安卓版本上无法运行请告知。









