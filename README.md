<div align="center">
<h1 align="center">Bilibili助手</h1>
<img src="https://img.shields.io/github/issues/yanghaa/BiliBiliTask_rebase?color=green">
<img src="https://img.shields.io/github/stars/yanghaa/BiliBiliTask_rebase?color=yellow">
<img src="https://img.shields.io/github/forks/yanghaa/BiliBiliTask_rebase?color=orange">
<img src="https://img.shields.io/github/license/yanghaa/BiliBiliTask_rebase?color=ff69b4">
<img src="https://img.shields.io/github/search/yanghaa/BiliBiliTask_rebase/main?color=blue">
<img src="https://img.shields.io/github/v/release/yanghaa/BiliBiliTask_rebase?color=blueviolet">
<img src="https://img.shields.io/github/languages/code-size/yanghaa/BiliBiliTask_rebase?color=critical">
</div>

# 简介

Rebase From srcrs/BilibiliTask

哔哩哔哩(`B`站)自动完成每日任务，
投币，点赞，直播签到，自动兑换银瓜子为硬币，自动送出即将过期礼物，漫画`App`签到，大会员领取`B`币卷等。每天获得`65`点经验，助你快速升级到`Lv6`。

开源不易，如果本项目对你有帮助，那么就请给个`star`吧。😄

重要提示，如果收到了`b`站的账号安全通知，可以考虑将`Actions`禁用一段时间，观望一段时间再进行使用，具体禁用步骤。

# 功能

* [x] 自动获取经验(投币、点赞、分享视频)
* [x] 直播辅助(直播签到，自动送出即将过期的礼物)
* [x] 自动兑换银瓜子为硬币
* [x] 自动领取年度大会员每月权益(每月`1`号领取`B`币劵、权益礼包)
* [x] 月底自动用B币卷给自己充电(每月`28`号)
* [x] 月底自动用B币卷兑换金瓜子(每月`28`号)
* [x] 漫画辅助脚本(漫画`APP`签到)
* [x] 支持功能自定义(自定义投币数量，银瓜子兑换硬币开关等)
* [x] 账户失效提醒(发送到你的微信或者钉钉提醒、邮箱提醒)
* [x] 支持多种方式推送运行结果(钉钉、微信)
* [x] 支持Docker自己部署

# 目录

- [简介](#简介)
- [功能](#功能)
- [目录](#目录)
- [Github Actions部署方法](#github-actions-部署方法)
  - [1.fork本项目](#1fork本项目)
  - [2.准备需要的参数](#2准备需要的参数)
  - [3.将获取到参数填到Secrets](#3将获取到参数填到secrets)
  - [4.开启actions](#4开启actions)
  - [5.进行一次push操作](#5进行一次push操作)
- [Docker部署方法](#Docker部署方法)
- [进阶使用](#进阶使用)
  - [1.配置文件说明](#1配置文件说明)
  - [2.推送运行结果到微信](#2推送运行结果到微信)
  - [3.推送运行结果到钉钉](#3推送运行结果到钉钉)
  - [4.使用`Telegram bot`推送运行结果到`Telgram`群组](#4使用telegram-bot推送运行结果到telgram群组)
  - [5.自定义程序运行时间](#5自定义程序运行时间)
- [如何拉取最新代码](#如何拉取最新代码)
- [更新日志](#更新日志)
- [参考项目](#参考项目)

关于日志中的 ✔ 和 ❌ 说明

符号 | 说明
-|-
✔ | 本次程序运行，成功的执行了代码，并完成了任务(例如，分享视频，今日未分享过，那么程序就应该请求分享视频的接口，协助完成分享视频的任务)。
❌ | 可能两种操作会出现这个符号。1.程序成功的执行了，尝试去完成任务，但是中途遇到了未知的失败。2.程序成功的执行了，检测到此类任务已经完成(例如，分享视频，今日已经分享过，那么程序不应该请求分享视频的接口，无需协助完成分享视频的任务)，就无需再去完成。可以理解为跳过或遇到错误。

# Github Actions 部署方法

## 1.fork本项目

项目地址：[yanghaa/BiliBiliTask_rebase](https://github.com/yanghaa/BiliBiliTask_rebase)

## 2.准备需要的参数

本项目成功运行需要三个参数，分别是`SESSDATA`，`bili_jct`，`DedeUserID`

- 打开`b`站首页（任意一个页面都行）--> 按下`F12` --> `Application` --> `Cookies` --> `https://www.bilibili.com`

- 找到所需要参数对应的数据，找不到可能是你的账号没有登录。

![](img/获取Cookie.png)

## 3.将获取到参数填到Secrets

在`Secrets`中的`Name`和`Value`格式如下：

Name | Value
-|-
BILI_JCT | xxxxx
DEDEUSERID | xxxxx
SESSDATA | xxxxx

将上一步获取的参数，填入到Secrets中，一共需要添加三个键值对。

![](img/添加Secrets.png)

## 4.开启actions

默认`actions`处于禁止状态，在`Actions`选项中开启`Actions`功能，把那个绿色的长按钮点一下。如果看到左侧工作流上有黄色`!`号，还需继续开启。

![](img/开启actions.gif)

## 5.进行一次push操作

默认`push`操作会触发工作流运行。

+ 打开`README.md`，将里面的 😄 删除一个即可。

![](img/进行一次push操作.gif)

+ 查看`actions`，显示对勾就说明运行成功了。以后会在每天的`10：30`执行，自动完成每日任务。

![](img/运行结果.gif)

# 进阶使用

## 1.配置文件说明

配置文件的位置在`src/main/resource/config.yml`。

重要提示！！！

程序检测到礼物有效期还剩`1`天，将会自动随机送出，部分朋友包裹里可能会有贵重礼物，你可以手动关闭即将过期礼物送出功能。
需要在`config.yml`中，将`gift`项设置为`false`。详情见下方[配置文件说明](#配置文件说明)。

符号 | 说明
-|-
coin | 代表投币的数量 [0,5]
gift | 是否需要送出即将过期礼物 [true,false]
s2c | 是否需要将银瓜子兑换硬币 [true,false]
autoBiCoin | 月底自动使用B币卷 [{0,自己有其他用途},{1,给自己充电},{2,兑换成金瓜子}]
platform | 用户设备的标识[android,ios]
upList | up 主列表,优先给这些 up 主投币[uid]
manga | 是否自动进行漫画签到 [true,false]
upLive | 即将过期礼物给此up的直播间,填写其 uid
selectLike | 对于进行投币的视频选择是否点赞 , 默认不点赞 [0,1]

```yml
#每天需要投币的数量 [0,5]。
coin: 5
#送出即将过期礼物 [true,false]
gift: true
#银瓜子兑换为硬币 [true,false]
s2c: true
#月底自动使用B币卷 [{0,自己会使用},{1,给自己充电},{2,兑换成金瓜子}]
autoBiCoin: 1
#用户设备的标识 [android,ios]
platform: android
#自定义优先给这些 up 的视频投币 , 以yml数组的形式 , 填写其 uid (mid)
upList:
  - 477137547
  - 14602398
#进行漫画签到任务 [true,false]
manga: true
#优先送出即将过期礼物给此up的直播间,填写其 uid
upLive: 477137547
#对于进行投币的视频选择是否点赞 , 默认不点赞 [0,1]
selectLike: 0
```

如实在没有想给他投币的up主，可以考虑把我填上哦 `477137547` 😄

# Docker部署方法

* 使用如下命令请自行替换所需的环境变量
* **每天00:01分运行程序**
```(shell script)
docker run -d \
  --env BILI_JCT=自行填写 \
  --env DEDEUSERID=自行填写 \
  --env SESSDATA=自行填写 \
  timmyovo/bilibilitask
```

## 2.推送运行结果到微信

### 使用`server`酱将程序运行结果推送到微信

`server`酱官网：`http://sc.ftqq.com`

+ 按照`server`酱官网使用教程，用`github`登录并绑定微信。

+ 获得`SCKEY`并将其填入到`Secrets`中。

在`Secrets`中的`Name`和`Value`格式如下：

Name | Value
-|-
SCKEY | xxxxx

这样就可以在微信接收到运行结果了。

![](img/server酱推送的结果.jpg)

### 使用`server`酱测试号版推送运行结果到微信

`server`酱测试号版官网：`https://sct.ftqq.com/`

+ 按照`server`酱测试号版官网使用教程，用微信扫码登录。

+ 获得`SENDKEY`并将其填入到`Secrets`中。

Name | Value
-|-
SENDKEY | xxxxx

### 使用`push+`推送运行结果到微信

`push+`官网：`https://pushplus.hxtrip.com`

+ 进入官网首页点击`一对一推送`，用微信扫描其二维码，进行关注

+ 关注后，即可看到`token`，将其添加到`Secrets`中即可

在`Secrets`中的`Name`和`Value`格式如下：

Name | Value
-|-
PUSHPLUSTK | xxxxx

## 3.推送运行结果到钉钉

1.首先，你需要在钉钉创建一个群聊，可以拉去两个人创建一个，再把他们踢出去。

2.获取钉钉自定义机器人的`Webhook`，将其填写到`Secrets`中

键值对如下格式:

Name | Value
-|-
DINGTALK | <https://oapi.dingtalk.com/robot/send?access_token=064559acaa666c43d5ba197656594f288f3acef9a64f4f43218beddd1c7b7050>

![](img/获取钉钉Webhook.gif)

## 4.使用`Telegram bot`推送运行结果到`Telgram`群组

1. 首先需要创建 telegram bot 并获取 telegram bot token，可以参考[文档](https://docs.microsoft.com/en-us/azure/bot-service/bot-service-channel-connect-telegram)；
2. 将机器人加入群组中，并获取群组ID，查看[Stackoverflow问答](https://stackoverflow.com/a/32572159)；
3. 将 telegram bot token 和群组ID添加到`Secrets`中即可。

在`Secrets`中的`Name`和`Value`格式如下：

Name               | Value
-------------------|------
TELEGRAM_BOT_TOKEN | xxxxx
TELEGRAM_CHAT_ID   | xxxxx

![](img/TgBot运行结果.jpg)

## 5.自定义程序运行时间

在`.github/workflows/Bilibili.yml`修改`cron`表达式，需要注意的是，`cron`表达式是国际时间，
需要换算到国内时间，往后推8个小时，例如国际时间是12点钟，则在国内是20点钟。

![](img/自定义程序运行时间.png)

# Docker

* **每天00:01分运行程序**
```
docker run -d --env BILI_JCT=自行填写 --env DEDEUSERID=自行填写 --env SESSDATA=自行填写 timmyovo/bilibilitask
```

# 如何拉取最新代码

## 方法一

在`github`安装`pull`，会自动帮你检测上游仓库，并帮助你更新代码

地址在这: <https://github.com/apps/pull>

由于添加有配置文件`config.yml`，有可能会覆盖你自定义的`config.yml`，需要注意。

## 方法二

1、查看是否有源头仓库的别名和地址

```sh
$ git remote -v
origin  https://github.com/yanghaa/BiliBiliTask_rebase.git (fetch)
origin  https://github.com/yanghaa/BiliBiliTask_rebase.git (push)
upstream  https://github.com/yanghaa/BiliBiliTask_rebase (fetch)
upstream  https://github.com/yanghaa/BiliBiliTask_rebase (push)
```

`origin`是你的仓库地址，`upstream`是你`fork`的源头仓库。通常第一次是没有`upstream`的。

2、添加源头仓库

```sh
git remote add upstream https://github.com/yanghaa/BiliBiliTask_rebase
```

3、把上游仓库`main`分支的更新拉取到本地

```sh
git pull upstream main
```

4、将更新后的代码推送到你的仓库

```sh
git push origin main
```

由于添加有配置文件`config.yml`，有可能会覆盖你自定义的`config.yml`，需要注意。

# 更新日志

## 2020-02-06

+ 避免投币给单一`up`主

+ 更新`b`币卷充电接口

+ 修复投币计算(投币数为负数)

+ 修复近30天未投币出现的bug

## 2020-12-17

+ 增加`server`酱测试号版推送，感谢[sh4wnzec](https://github.com/sh4wnzec)

+ 增加`Telegram bot`推送，感谢[qiwihui](https://github.com/qiwihui)

## 2020-12-07

+ 发布1.0.8版本

+ 修复用户无动态获取视频信息错误

+ 修复从动态列表中获取到自己投稿视频的错误

+ 增加一些自定义配置点赞

## 2020-11-28

+ 去除UA(貌似是没有影响的)

+ 设置API请求间的缓冲时间(5 秒钟)

## 2020-11-22

感谢[东酱](https://github.com/qq523407234)在此项目中做出的巨大贡献。

+ 增加`push+`推送方式

+ 将项目管理工具改为`gradle`

+ 优化代码结构，优化日志输出格式(高亮)

## 2020-11-17

+ 增加钉钉推送方式

+ 优化投币给视频的策略 , 自定义`up`主视频 > 当前用户动态列表中的视频投稿 > 随机视频列表

+ 投币后等待`1-2`秒钟 , 降低访问投币`API`的速度

+ 优化模拟观看视频 , 获取视频时常，随机上报视频观看进度

+ 优化日志输出，提示更加友好，更加贴近用户(如增加了还剩多少天升级的提示)

+ 增加账号失效提醒(发送到微信或者钉钉)

## 2020-11-05

+ 根据阿里巴巴代码规范优化代码

+ 增加用户标识配置项

## 2020-11-03

+ 将自动使用B币卷开关，更改为自动配置用途，可以选择不使用、充电、兑换金瓜子。

+ 增加B币卷兑换金瓜子功能

## 2020-10-22

+ 增加用server酱推送运行结果到微信功能

## 2020-10-19

+ 增加年度大会员每月`1`号领取`B`币卷

+ 月底自动用`B`币卷给自己充电

+ 在配置项中添加是否月底用`B`币卷给自己充电开关，默认开启

由于我本身不是年度大会员，无法测试是否可以正常领取年度大会员权益和`B`币卷，
出错的时候麻烦给我提一个`issues`，我会及时解决的。

## 2020-10-17

+ 优化日志显示

+ 增加账户失效提醒

账户失效会导致任务流运行失败，github会下发运行失败邮件提醒。

## 2020-10-13

+ 重构代码，功能不变

+ 采用反射实现自动加载`task`包功能任务代码。

+ 加入配置文件，用户可自定义一些配置

## 2020-10-08

+ 增加自动送出即将过期的礼物

+ 增加漫画`APP`签到

+ 增加一些`api`

## 2020-10-07

+ 增添银瓜子自动兑换硬币功能

## 2020-10-06

+ 增添B站直播签到

+ 继续增添`API`

## 2020-10-05

+ 完成了自动获取经验功能

每日登录、每日观看视频、每日投币、每日分享

+ 完善对接`api`接口

# 参考项目

[happy888888/BiliExp](https://github.com/happy888888/BiliExp)
