import groovy.json.JsonSlurper
import java.util.concurrent.CompletableFuture

def query = "爱情"  // 输入的中文参数
def zh_pattern = /[\u4e00-\u9fa5]+/    // 匹配中文的正则

// 百度翻译相关
def appId = '20230420001648963' // 替换成自己的 App ID
def appKey = 'FGUzmEFUWCUw674dpJyB' // 替换成自己的 App Key
def from = "zh" // 原始语言
def to = "en"   // 转换后的语言

// 开启异步任务避免堵塞主线程
def task = CompletableFuture.supplyAsync({
    // 构造salt参数
    def salt = System.currentTimeMillis().toString()
    // 构造Sign参数
    def sign = "${appId}${query}${salt}${appKey}".md5()
    // 拼接请求URL
    def url = "http://api.fanyi.baidu.com/api/trans/vip/translate" +
            "?q=${URLEncoder.encode(query, "UTF-8")}" +
            "&from=${from}" +
            "&to=${to}" +
            "&appid=${appId}" +
            "&salt=${salt}" +
            "&sign=${sign}"

    // 获取响应文本，转换为Json对象
    def json = new JsonSlurper().parseText(new URL(url).text)

    // 异常直接返回，正常返回翻译结果：转小写 + 空格转换为下划线 + 替换,
    return json.error_code ? "${json}" : json.trans_result[0].dst.toLowerCase().replaceAll(" ", "_").replace(",", "")
})

// 输入内容不为空 + 字符串长度大于1 + 且由中文组成 → 才触发转换，减少无效调用引起的卡顿
if (query != null && query.toString().length() > 1 && query.toString().matches(zh_pattern)) {
    task.thenAccept({ result -> return result })
    task.join()
} else {
    return ""
}