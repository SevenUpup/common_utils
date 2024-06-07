package com.fido.plugin.task

import com.fido.constant.PluginConstant
import com.fido.plugin.method_replace.REPLACE_METHOD_ANNO_DES
import com.fido.plugin.method_replace.ReplaceMethodAnnotationItem
import com.fido.plugin.method_replace.asmConfigs
import com.fido.plugin.method_replace.replaceMethodConfigClassList
import com.fido.utils.replaceDotBySlash
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.InputChanges
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * @author: FiDo
 * @date: 2024/5/20
 * @des:  通过注解的方式 替换目标类中的方法
 *        实现方式：Task-> 获取项目所有class文件 -> 匹配对应
 */
//@CacheableTask
//@BuildAnalyzer(primaryTaskCategory = TaskCategory.SOURCE_PROCESSING)
internal abstract class ModifyClassesTask : DefaultTask() {

    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    // Gradle will set this property with all class directories that available in scope
    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    // Task will put all classes from directories and jars after optional modification into single jar
    @get:OutputFile
    abstract val output: RegularFileProperty

    /*@get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val jars: ListProperty<RegularFile>

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val dirs: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty*/

    // 在项目根目录.gradle 加上 ext.fido_asm_replace_method_class = ["xxx"]
    private var replaceMethodClass =
        project.properties[PluginConstant.FIDO_ASM_REPLACE_METHOD_CLASS] as? List<String>
            ?: emptyList()
    private var taskEnable =
        project.properties[PluginConstant.FIDO_ASM_REPLACE_METHOD_ENABLE] as? Boolean ?: false

    @TaskAction
    fun taskAction(inputChanges: InputChanges) {

//        val inputs = (allJars.get() + allDirectories.get()).map { it.asFile.toPath() }
//        val grip: Grip = GripFactory.newInstance(Opcodes.ASM9).create(inputs)
//        val mTargetClass = replaceSlashByDot(TARGET_CLASS)
//        val queryClassName = grip select classes from inputs where name { grip, s ->
//            s == mTargetClass
//        }
//        val mClasses = queryClassName.execute().classes.map { it.name.separator() }
//        println("targetClass =${replaceMethodClass} queryClassName mClasses = $mClasses")

        val closure = { jarOutput: JarOutputStream, inputSource: InputStream ->
            val reader = ClassReader(inputSource)
            val classNode = ClassNode(Opcodes.ASM9)
            reader.accept(classNode, ClassReader.EXPAND_FRAMES)
            classNode.apply {
                if (asmConfigs.isNotEmpty()) {
                    methods?.forEach { methodNode ->
                        //修改方法
                        hookMethodNode(methodNode, name)
                    }
                }
            }
            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            classNode.accept(classWriter)
            jarOutput.write(classWriter.toByteArray())
        }
        // 获取需要替换的类提供两种方式
        // 1.根目录下 ext.fido_asm_replace_method_class = ["xxx"]
        val mutableList = mutableSetOf<String>()
        mutableList.addAll(replaceMethodClass)
        // 2. ReplaceMethodPluginParameters{ replaceClassName=[] }
        if (taskEnable && replaceMethodConfigClassList.isNotEmpty()) {
            mutableList.addAll(replaceMethodConfigClassList)
        }
        val replaceMethodSlashClass = mutableList.map { replaceDotBySlash(it).endAppendClass }
//        println("replaceMethodSlashClass = $replaceMethodSlashClass")
//        println("收集之前====>size=${asmConfigs.size}")
        //先进行收集需要替换方法的注解信息
        collectAnnotationFromDir(replaceMethodSlashClass)
        collectAnnotationFromJars(replaceMethodSlashClass)
//        println("收集完毕====>size=${asmConfigs.size} \nConfigs=${asmConfigs}")

        //配置收集完成 开始修改字节码
        JarOutputStream(BufferedOutputStream(FileOutputStream(output.get().asFile))).use { jarOutput ->
            processJars(jarOutput, replaceMethodSlashClass, closure)
            processDirs(jarOutput, replaceMethodSlashClass, closure)
        }

    }

    private fun hookMethodNode(methodNode: MethodNode?, name: String) {
        methodNode?.instructions?.forEach { insnNode ->
            if (insnNode is MethodInsnNode) {
                asmConfigs.forEach { asmItem ->
                    //INVOKEVIRTUAL android/app/ActivityManager.getRunningAppProcesses ()Ljava/util/List; ->
                    //INVOKESTATIC  com/lanshifu/asm_plugin_library/privacy/PrivacyUtil.getRunningAppProcesses (Landroid/app/ActivityManager;)Ljava/util/List;

                    /*if (
//                        asmItem.oriClass == "com.fido.common.common_utils.asm.AsmHookActivity".replace(".","/") &&
                        (insnNode.name == "privateStaticFun" || insnNode.name == "pubStaticFun")){
                        LogPrint.normal("FiDo"){
                            """
                                asmItem=>${asmItem.toString()}
                                insnNode.desc=>${insnNode.desc}
                                insnNode.name=>${insnNode.name}
                                insnNode.opcode=>${insnNode.opcode}
                                insnNode.owner=>${insnNode.owner}
                            """.trimIndent()
                        }
                    }*/

                    if (asmItem.oriDesc == insnNode.desc && asmItem.oriMethod == insnNode.name
                        && insnNode.opcode == asmItem.oriAccess &&
                        (insnNode.owner == asmItem.oriClass || asmItem.oriClass == "java/lang/Object")
                    ) {
                        /*LogPrint.normal(PluginConstant.REPLACE_METHOD_TAG) {
                            "\nhook:\n" +
                                    "opcode=${insnNode.opcode},owner=${insnNode.owner},desc=${insnNode.desc}name=${name}#${insnNode.name} ->\n" +
                                    "opcode=${asmItem.targetAccess},owner=${asmItem.targetClass},desc=${asmItem.targetDesc},name=${asmItem.targetMethod}\n"
                        }*/
                        insnNode.opcode = asmItem.targetAccess
                        insnNode.desc = asmItem.targetDesc
                        insnNode.owner = asmItem.targetClass
                        insnNode.name = asmItem.targetMethod
                    }
                }
            }
        }
    }

    private fun collectAnnotationFromDir(replaceMethodSlashClass: List<String>) {
        allDirectories.get().forEach { directory ->
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    if (replaceMethodSlashClass.contains(relativePath)) {
//                        println("collectAnnotationFromDir relativePath=${relativePath}")
                        readerTargetClass(file.inputStream())
                    }
                }
            }
        }
    }

    private fun collectAnnotationFromJars(replaceMethodSlashClass: List<String>) {
        allJars.get().forEach { file ->
            JarFile(file.asFile).use { jarFile ->
                jarFile.entries().iterator().forEach { jarEntry ->
                    if (jarEntry.isDirectory.not() && replaceMethodSlashClass.contains(jarEntry.name)) {
//                        println("collectAnnotationFromJars jarFile = ${jarFile} jarEntry = $jarEntry name=${jarEntry.name} realName=${jarEntry.realName}")
                        jarFile.getInputStream(jarEntry).use {
                            readerTargetClass(it)
                        }
                    }
                }
            }
        }
    }

    private fun readerTargetClass(inputSource: InputStream) {
        val classReader = ClassReader(inputSource)
        val classNode = ClassNode(Opcodes.ASM9)
        classReader.accept(classNode, 0)
        classNode.apply {
            collectionHookAnnotation(methods, name)
        }
    }


    private fun collectionHookAnnotation(methods: List<MethodNode>, name: String) {
        methods.forEach { methodNode ->
            //收集需要修改的方法
            methodNode.invisibleAnnotations?.forEach { annotationNode ->
                if (annotationNode.desc == REPLACE_METHOD_ANNO_DES) {
                    val asmItem = ReplaceMethodAnnotationItem(name, methodNode, annotationNode)
                    if (!asmConfigs.contains(asmItem)) {
                        asmConfigs.add(asmItem)
                    }
                }
            }
        }
    }


    private fun processDirs(
        jarOutput: JarOutputStream,
        replaceMethodSlashClass: List<String>,
        block: (JarOutputStream, InputStream) -> Unit
    ) {
        allDirectories.get().forEach { directory ->
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
                    /*val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    jarOutput.putNextEntry(JarEntry(relativePath.replace(File.separatorChar, '/')))
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(jarOutput)
                    }
                    jarOutput.closeEntry()*/
                    // relativePath ===> com/fido/common/common_utils/device_info/PrivacyUtil.class
                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    if (relativePath != "module-info.class" &&
//                        !jarEntry.name.startsWith("androidx") &&
                        !relativePath.startsWith("META-INF") &&
                        !relativePath.startsWith("kotlin/") &&
                        !relativePath.startsWith("org/objectweb/asm") &&
                        relativePath.endsWith(".class") &&
                        !replaceMethodSlashClass.contains(relativePath)
                    ) {
                        jarOutput.putNextEntry(JarEntry(relativePath.replace(File.separatorChar, '/')))
                        file.inputStream().use { inputStream ->
                            block(jarOutput, inputStream)
                        }
                    } else {
                        jarOutput.putNextEntry(JarEntry(relativePath.replace(File.separatorChar, '/')))
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(jarOutput)
                        }
                    }
                    jarOutput.closeEntry()
                }
            }
        }
    }

    private fun processJars(
        jarOutput: JarOutputStream,
        replaceMethodSlashClass: List<String>,
        block: (JarOutputStream, InputStream) -> Unit
    ) {
        allJars.get().forEach { file ->
            JarFile(file.asFile).use { jarFile ->
                jarFile.entries().iterator().forEach { jarEntry ->
                    if (jarEntry.isDirectory.not() &&
                        jarEntry.name != "module-info.class" &&
//                        !jarEntry.name.startsWith("androidx") &&
                        !jarEntry.name.startsWith("META-INF") &&
                        !jarEntry.name.startsWith("kotlin/") &&
                        !jarEntry.name.startsWith("org/objectweb/asm") &&
                        jarEntry.name.endsWith(".class") &&
                        !replaceMethodSlashClass.contains(jarEntry.name)
                    ) {
//                        println("Jars 找到目标 jarEntry = $jarEntry name=${jarEntry.name} realName=${jarEntry.realName}")
                        jarOutput.putNextEntry(JarEntry(jarEntry.name))
                        jarFile.getInputStream(jarEntry).use {
                            // Transforming class with Task
                            block(jarOutput, it)
                        }
                    } else {
                        runCatching {
                            jarOutput.putNextEntry(JarEntry(jarEntry.name))
                            jarFile.getInputStream(jarEntry).use {
                                it.copyTo(jarOutput)
                            }
                        }/*.onFailure { e ->
                            Log.e("Copy jar entry failed. [entry:${jarEntry.name}]", e)
                        }*/
                    }
                    jarOutput.closeEntry()
                }
            }
        }
    }


    @Suppress("SpellCheckingInspection")
    private companion object {
        /**
         * 需要处理的字节码
         */
        private const val TARGET_CLASS = "com/fido/common/common_utils/device_info/PrivacyUtil"

    }

    // 尾部添加.class
    private val String.endAppendClass
        get() = if (this.endsWith(".class")) this else ("$this.class")
}