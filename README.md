##目录结构说明
java目录下
* agnomen验证码破解
* haha web服务模块
* spider 爬虫模块

resouces目录下
* data.txt是用来破解验证码的钥匙，使用agnomen.DataGenerator类进行可视化鼠标点击生成
* checkcodes目录存放验证码，使用agnomen.ImageDownloader类进行生成
* static存放静态资源
* templates存放模板，使用freemarker模板引擎