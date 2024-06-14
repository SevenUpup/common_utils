package com.fido.common.common_utils.design_pattern.strategy

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:  规则执行器
 */
class RuleExecute<T>(private val check: T, private val map: MutableMap<String, List<BaseRule<T>>>) {

    companion object {
        @JvmStatic
        fun <T> create(check: T): Builder<T> {
            return Builder(check)
        }

        private const val AND = "&&"
        private const val OR = "||"
    }

    //构造者模式 构建执行器
    open class Builder<T>(private val check: T) {

        private val rulesMap = mutableMapOf<String, List<BaseRule<T>>>()
        private var indexSuffix = 0

        //并且逻辑
        fun and(rules: List<BaseRule<T>>?) = apply {
            rules?.let {
                val key = AND + (indexSuffix++).toString()
                rulesMap[key] = rules
            }
        }

        //或的逻辑
        fun or(rules: List<BaseRule<T>>?) = apply {
            rules?.let {
                val key = OR + (indexSuffix++).toString()
                rulesMap[key] = rules
            }
        }

        fun build():RuleExecute<T>{
            return RuleExecute(check,rulesMap)
        }

    }

    //执行任务
    fun execute():RuleResult{
        for ((key, ruleList) in map) {
            when(key.substring(0,2)){
                AND ->{
                    val result = and(check,ruleList)
                    if (!result.success) {
                        return result
                    }
                }
                OR -> {
                    val result = or(check,ruleList)
                    if (!result.success) {
                        return result
                    }
                }
            }
        }
        return RuleResult(true)
    }

    private fun and(check: T, ruleList: List<BaseRule<T>>): RuleResult  {
        for (baseRule in ruleList) {
            val execute  = baseRule.execute(check)
            if (!execute .success) {
                //失败一次就要return
                return execute
            }
        }
        // 全部匹配成功，返回 true
        return RuleResult(true)
    }

    private fun or(check: T,ruleList: List<BaseRule<T>>):RuleResult{
        val sb = StringBuilder()
        for (baseRule in ruleList) {
            val execute = baseRule.execute(check)
            if (execute.success) {
                // 从前往后遍历，只要一个满足条件就return
                return RuleResult(true)
            } else {
                sb.append(execute.message)
            }
        }
        return RuleResult(false,sb.toString())
    }

}