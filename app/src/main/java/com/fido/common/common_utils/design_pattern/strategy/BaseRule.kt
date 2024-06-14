package com.fido.common.common_utils.design_pattern.strategy

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:
 */
interface BaseRule<T> {

    fun execute(rule:T):RuleResult

}