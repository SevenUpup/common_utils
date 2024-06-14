package com.fido.config;

import com.fido.constant.PluginConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: FiDo
 * @date: 2024/6/12
 * @des:
 */
public class CommonPluginConfig {

    public static String NAME_SPACE = PluginConstant.FIDO_COMMON_PLUGIN;

    /**
     * 是否打印输出所有依赖并区分二方三方，默认关闭
     */
    public boolean printDependencies = false;

    /**
     * 是否打印输出so和依赖的关系，默认关闭
     */
    public boolean analysisSo = false;

    /**
     * snapshot版本检查，默认关闭
     */
    public boolean checkSnapshot = false;

    /**
     * snapshot版本检查 如有，打断编译，默认关闭
     */
    public boolean blockSnapshot = false;

    /**
     * 需要移除的权限
     */
    public List<String> permissionsToRemove = new ArrayList<>();

}
