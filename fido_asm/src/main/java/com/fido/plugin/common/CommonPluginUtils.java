package com.fido.plugin.common;

import com.android.build.gradle.AppExtension;
import com.fido.config.CommonPluginConfig;
import com.fido.utils.FileUtils;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: FiDo
 * @date: 2024/6/12
 * @des:
 */
public class CommonPluginUtils {

    public static final String TAG = "FiDoPlugin >>>>> ";

    /**
     * 移除权限
     *
     * @param project             target project
     * @param permissionsToRemove 需要移除的权限
     */
    public static void permissionsToRemove(Project project, List<String> permissionsToRemove) {
        try {
            if (permissionsToRemove.isEmpty()) {
                return;
            }
            AppExtension androidExtension = project.getExtensions().getByType(AppExtension.class);
            androidExtension.getApplicationVariants().all(applicationVariant -> {
                applicationVariant.getOutputs().all(output -> {
//                        output.getProcessManifest().doLast(task -> {
                            output.getProcessManifestProvider().get().doLast(task -> {
                                try {
                                    // 获取manifest文件
                                    File manifestOutFile = output.getProcessResourcesProvider().get().getManifestFile();
                                    // 读取manifest文件
                                    String manifestContent = Files.readString(manifestOutFile.toPath());
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("=======================修改前=========================\n");
                                    sb.append(manifestContent);
                                    sb.append("\n");
                                    // 删除指定权限
                                    for (String permission : permissionsToRemove) {
                                        String replacePermission = "android.permission.INTERNET";
                                        System.out.println(TAG + "permission = " + permission + " is replace to =>" + replacePermission);
                                        manifestContent = manifestContent.replaceAll(permission, replacePermission);
                                    }
                                    // 再写回manifest文件
                                    Files.writeString(manifestOutFile.toPath(), manifestContent);
                                    sb.append("=======================修改后=========================\n");
                                    sb.append(manifestContent);
                                    FileUtils.INSTANCE.writeTextToDesktop(sb.toString(),CommonPluginConfig.NAME_SPACE+"_manifest.txt");
                                } catch (IOException e) {
                                    System.out.println(TAG + "permissionsToRemove 文件操作异常，请检查配置");
                                    throw new RuntimeException(e);
                                }
                            });

                        }
                );
            });
        }catch (Exception e){
            System.out.println("permissionsToRemove was error===>" + e.getMessage());
        }
    }

    /**
     * snapshot版本检查
     * to do ：白名单
     *
     * @param project       target project
     * @param blockSnapshot 是否打断编译
     */
    public static void checkSnapshot(Project project, boolean blockSnapshot) {
        try {
            AppExtension androidExtension = project.getExtensions().getByType(AppExtension.class);
            androidExtension.getApplicationVariants().all(applicationVariant -> {
                // debug/release也可以加配置
                System.out.println(TAG + "applicationVariant.getName() = " + applicationVariant.getName());
                Configuration configuration = project.getConfigurations().getByName(applicationVariant.getName() + "CompileClasspath");

                List<String> snapshotList = new ArrayList<>();

                // 所有的依赖，包括依赖中的依赖
                configuration.getResolvedConfiguration().getLenientConfiguration().getAllModuleDependencies().forEach(resolvedDependency -> {
                    ModuleVersionIdentifier identifier = resolvedDependency.getModule().getId();
                    if (isSnapshot(identifier.getVersion())) {
                        snapshotList.add(identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                    }
                });

                if (snapshotList.size() > 0) {
                    snapshotList.forEach(System.out::println);
                    if (blockSnapshot) {
                        blockBuilding();
                    }
                } else {
                    System.out.println(TAG + "无SNAPSHOT版本依赖");
                }
            });
        }catch (Exception e){
            System.out.println("checkSnapshot was error ==>" + e.getMessage());
        }
    }

    private static void blockBuilding() {
        String errorMassage = "检测到有SNAPSHOT版本依赖";
        throw new GradleException(errorMassage, new Exception(errorMassage));
    }

    private static boolean isSnapshot(String version) {
        String checkRules = "SNAPSHOT";
        return version.endsWith(checkRules) || version.contains(checkRules);
    }

    /**
     * 分析so文件和依赖的关系
     *
     * @param project target project
     */
    public static void doAnalysisSo(Project project) {
        try {
            AppExtension androidExtension = project.getExtensions().getByType(AppExtension.class);
            androidExtension.getApplicationVariants().all(applicationVariant -> {
                System.out.println(TAG + "applicationVariant.getName() = " + applicationVariant.getName());

                Configuration configuration = project.getConfigurations().getByName(applicationVariant.getName() + "CompileClasspath");
                configuration.forEach(file -> {
                    String fineName = file.getName();
                    System.out.println(TAG + "fine name = " + fineName);
                    if (fineName.endsWith(".jar") || fineName.endsWith(".aar")) {
                        try {
                            JarFile jarFile = new JarFile(file);
                            for (Enumeration enums = jarFile.entries(); enums.hasMoreElements(); ) {
                                JarEntry jarEntry = (JarEntry) enums.nextElement();
                                if (jarEntry.getName().endsWith(".so")) {
                                    System.out.println(TAG + "----- so name = " + jarEntry.getName());
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
        }catch (Exception e){
            System.out.println("doAnalysisSo was error=====>" + e.getMessage());
        }
    }

    /**
     * 区分官方库和三方库的依赖
     * to do : group可配
     *
     * @param project target project
     */
    public static void doPrintDependencies(Project project) {
        try {
            AppExtension androidExtension = project.getExtensions().getByType(AppExtension.class);
            List<String> androidLibs = new ArrayList<>();
            List<String> otherLibs = new ArrayList<>();
            androidExtension.getApplicationVariants().all(applicationVariant -> {
                // debug/release也可以加配置
                System.out.println(TAG + "applicationVariant.getName() = " + applicationVariant.getName());
                Configuration configuration = project.getConfigurations().getByName(applicationVariant.getName() + "CompileClasspath");

                // 所有的依赖，包括依赖中的依赖
                configuration.getResolvedConfiguration().getLenientConfiguration().getAllModuleDependencies().forEach(resolvedDependency -> {
                    ModuleVersionIdentifier identifier = resolvedDependency.getModule().getId();
                    if (identifier.getGroup().contains("androidx") || identifier.getGroup().contains("com.google") || identifier.getGroup().contains("org.jetbrains")) {
                        androidLibs.add(identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                    } else {
                        otherLibs.add(identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                    }
                });

//                System.out.println("--------------官方库 start--------------");
//                androidLibs.forEach(System.out::println);
//                System.out.println("--------------官方库 end--------------");
//
//                System.out.println("--------------三方库 start--------------");
//                otherLibs.forEach(System.out::println);
//                System.out.println("--------------三方库 end--------------");
            });

            StringBuilder sb = new StringBuilder();
            if (!androidLibs.isEmpty()) {
                sb.append("--------------官方库 start--------------\n");
                for (String androidLib : androidLibs) {
                    sb.append(androidLib);
                    sb.append("\n");
                }
                sb.append("--------------官方库 end---------------\n");
            }
            if (!otherLibs.isEmpty()) {
                sb.append("--------------三方库 start--------------\n");
                for (String otherLib : otherLibs) {
                    sb.append(otherLib);
                    sb.append("\n");
                }
                sb.append("--------------三方库 end--------------\n");
            }
            FileUtils.INSTANCE.writeTextToDesktop(sb.toString(), CommonPluginConfig.NAME_SPACE +"_dependencies.txt");
        }catch (Exception e){
            System.out.println("doPrintDependencies was error==>" + e.getMessage());
        }
    }

}
