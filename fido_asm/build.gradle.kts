plugins {
    id("java-library")
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {

    implementation(libs.agp.core)
    implementation(libs.agp.api)

    implementation(libs.asm)
    implementation(libs.asm.commons)
}

// 让 Gralde 知道我们定义了这么一个插件 可以通过 java-gradle-plugin 插件来简化这个过程，只需要在 kts 脚本中声明就好了。
gradlePlugin{
    plugins{
        val mPlugin = this.create("FiDoAsmPlugin")
        mPlugin.id = properties["GROUP_ID"].toString()
        mPlugin.implementationClass = "com.fido.plugin.FiDoAsmPlugin"

//        val permissionPlugin = this.create("PermissionCheckPlugin")
//        permissionPlugin.id = properties["PLUGIN_PERMISSION_ID"].toString()
//        permissionPlugin.implementationClass = "com.fido.PermissionCheckPlugin"
    }
}

val localMavenDir = File(rootProject.rootDir,"local_maven")
if (!localMavenDir.exists()) {
    localMavenDir.mkdirs()
}
publishing{
    repositories{
        // Local
        maven {
            name = "LocalMaven"
            url = uri(localMavenDir.canonicalPath)
        }

//        // Remote
//        maven {
//            name = "RemoteMaven"
//            credentials {
//                username = ""
//                password = ""
//            }
//            url = uri("")
//        }

        val sourceJar by tasks.creating(Jar::class.java) {
            archiveClassifier.set("sources")
            from(sourceSets.getByName("main").allSource)
        }

        publications {
            val defaultPublication: MavenPublication = this.create("Default", MavenPublication::class.java)
            with(defaultPublication) {
                groupId = properties["GROUP_ID"].toString()
                artifactId = properties["PLUGIN_ARTIFACT_ID"].toString()
                version = properties["VERSION"].toString()
                // For aar
//            afterEvaluate {
//                artifact(tasks.getByName("bundleReleaseAar"))
//            }
                // jar
//            artifact("${layout.buildDirectory.asFile.get().absolutePath}${File.separator}libs${File.separator}plugin.jar")
                afterEvaluate {
                    artifact(tasks.getByName("jar"))
                }
                // source Code.
                artifact(sourceJar)
//                pom {
//                    name = "FiDo-plugin"
//                    description = "Plugin demo for AGP."
//                    licenses {
//                        license {
//                            name = "The Apache License, Version 2.0"
//                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
//                        }
//                    }
//                    developers {
//                        developer {
//                            id = "FiDo"
//                            name = "Ht"
//                            email = "ilikesevenup@163.com"
//                        }
//                    }
//                }

                pom.withXml {
                    val dependencies = asNode().appendNode("dependencies")
                    configurations.implementation.get().allDependencies.all {
                        val dependency = this
                        if (dependency.group == null || dependency.version == null || dependency.name == "unspecified") {
                            return@all
                        }
                        val dependencyNode = dependencies.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dependency.group)
                        dependencyNode.appendNode("artifactId", dependency.name)
                        dependencyNode.appendNode("version", dependency.version)
                        dependencyNode.appendNode("scope", "implementation")
                    }
                }
            }

//            with(defaultPublication) {
//                groupId = properties["PLUGIN_PERMISSION_ID"].toString()
//                artifactId = properties["PLUGIN_PERMISSION_ARTIFACT_ID"].toString()
//                version = properties["PERMISSION_PLUGIN_VERSION"].toString()
//                afterEvaluate {
//                    artifact(tasks.getByName("jar"))
//                }
//                // source Code.
//                artifact(sourceJar)
//
//                pom.withXml {
//                    val dependencies = asNode().appendNode("dependencies")
//                    configurations.implementation.get().allDependencies.all {
//                        val dependency = this
//                        if (dependency.group == null || dependency.version == null || dependency.name == "unspecified") {
//                            return@all
//                        }
//                        val dependencyNode = dependencies.appendNode("dependency")
//                        dependencyNode.appendNode("groupId", dependency.group)
//                        dependencyNode.appendNode("artifactId", dependency.name)
//                        dependencyNode.appendNode("version", dependency.version)
//                        dependencyNode.appendNode("scope", "implementation")
//                    }
//                }
//            }
        }

    }
}