
class IOTest{
    static void main(String[] args) {
        def srcFile = new File("build.gradle")

        def targetFile = new File("copy.txt")

        targetFile.withObjectOutputStream {os->
            srcFile.withInputStream {is->
                //利用 OutputStream 的<<操作符重载，完成从 inputstream 到 OutputStream //的输出
                os<<is
            }
        }
    }

}