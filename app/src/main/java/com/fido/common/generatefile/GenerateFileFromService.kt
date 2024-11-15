package com.fido.common.generatefile

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.w3c.dom.Document
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * @author: FiDo
 * @date: 2024/11/12
 * @des:
 */

fun main(){

    val colorsMap = mutableMapOf<String,String>()

    val url = "https://raw.githubusercontent.com/motaionx/figmaStyle/main/tokens.json"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    client.newCall(request).enqueue(object :Callback{
        override fun onFailure(call: Call, e: IOException) {
            println("error=${e.message}")
            doParse(TestJson.TestJsonStr)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val body = response.body()
                val json = body?.string() ?: ""
                println(json)
                doParse(json)
            }
        }

        private fun doParse(json: String) {
            val firstLevel = Gson().fromJson<Map<Any, Any>>(json, Map::class.java)
            val secondLevel = firstLevel["global"] as Map<*, *>
            secondLevel.forEach {
                val key = it.key.toString()
                val thirdLevel = it.value as? Map<*,*>
                val type = thirdLevel?.get("type").toString()
                when(type){
                    "color"->{
                        colorsMap[key] = thirdLevel?.get("value").toString()
                    }
                }
            }
            //生成xml文件
            val factory = DocumentBuilderFactory.newInstance()

            //创建doc
            val colorBuilder = factory.newDocumentBuilder()
            val colorDoc :Document = colorBuilder.newDocument()
            //创建根结点
            val colorResourceElement = colorDoc.createElement("resources")
            colorDoc.appendChild(colorResourceElement)
            //添加选项
            colorsMap.forEach {
                val colorElement = colorDoc.createElement("color")
                colorElement.setAttribute("name",it.key)
                colorElement.textContent = it.value
                colorResourceElement.appendChild(colorElement)
            }
            // 将 XML 写入文件
            // 5、将 XML 写入文件
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT,"yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")

            val colorSource = DOMSource(colorDoc)
            val colorResult = StreamResult(File("app/src/main/res/values/atom_colors.xml"))
            transformer.transform(colorSource,colorResult)

            println("XML 文件生成成功")
        }

    })
}
