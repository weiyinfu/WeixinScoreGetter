![event.jpg]
公元2017年9月21日，北航研究生教务系统被某在校生攻破。从此教务系统无法被外网访问。

----------------------------------------

![扫描二维码关注公众号](qrcode_for_gh_a525ae64e89d_430.jpg)

## 原理
用户绑定提供用户名和密码，本程序模拟登录北航教务处网站，通过爬虫爬取成绩信息。
**声明**：本公众号绝对不做坏事，本公众号的目的是方便大家查成绩，毕竟登录教务处很费事并且教务处界面low到爆、丑到爆。

## 目录结构说明
java目录下
* agnomen验证码破解
* web web服务模块
* spider 爬虫模块

## resouces目录下
* data.txt是用来破解验证码的钥匙，使用agnomen.DataGenerator类进行可视化鼠标点击生成
* checkcodes目录存放验证码，使用agnomen.ImageDownloader类进行生成
* static存放静态资源
* templates存放模板，使用freemarker模板引擎

## Java中的序列化
最好是用字符串形式，不要用ObjectStream，因为那样会导致莫名其妙的错误。
