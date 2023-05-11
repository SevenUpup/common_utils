import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {

        println('MyPlugin 执行了')

        project.task("MyTask").doLast {
            println("MyTask 执行了")
        }

    }
}