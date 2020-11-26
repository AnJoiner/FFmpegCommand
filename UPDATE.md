## v1.2.0-beta1
1. 启用kotlin替代java
2. 取消`runCmdAsync`和`runCmdSync`方法，统一更改为`runCmd`执行`FFmpeg`命令
3. 取消多命令`runMoreAsync`和`runMoreSync`方法，`runCmd`内部自动实现同步顺序执行
4. 新增错误日志提示，发生错误时使用`ffmpeg-cmd`进行筛选错误日志

## v1.1.7
1. 新增获取支持的编解码
2. 新增获取支持的封装格式
3. 新增amr转码格式
4. 新增自定义错误提示
5. 修复部分机型“GetObjectClass JNI DETECTED ERROR IN APPLICATION”问题

## v1.1.6
1. 修复在部分机型上获取媒体信息问题
2. 修复在极限情况下进度返回问题

## v1.1.5
1. 新增多任务同时处理（异步和同步）
2. 修复armeabi-v7a进度不显示问题

## v1.1.4
1. 兼容 miniSdkVersion 1.1.4

## v1.1.3
1. 新增执行进度
2. 新增取消执行

## v1.1.2
1. 新增ffmpeg-mini库
2. 修复bug