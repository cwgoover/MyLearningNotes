package kotlinlang

/**
 * Created by i352072(erica.cao@sap.com) on 03/03/2022
 */
object Regex {
    
    fun findGroupValue(text: String): String? {
        val regex = """attachmentId=(\d+)""".toRegex()
        val matchResult = regex.find(text)
        return matchResult?.groupValues?.get(1)
    }
    
}

fun main() {
    val text = "/attachmentdownload?attachmentId=2346&controllerName=dmsAttachmentDownloadController&moduleName=CDP&hashKey=b6e12af8a03620d8df3f1e8c069b2e32be987830411152d48bf42a8dfea88bca&dgp=V4-s7DlQjX4zTdcK77ZQdK1CKVDqHNCq6yJWFhXqaYD-oJDC4A&_s.crb=DlTBTKevrV9mZ%252fHw8zbj8On8egVLv%252b8sEnKVdJIedQw%253d"
    val value = Regex.findGroupValue(text)
    println("matchResult= $value")
}