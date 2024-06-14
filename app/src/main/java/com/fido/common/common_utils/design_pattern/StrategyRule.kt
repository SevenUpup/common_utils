package com.fido.common.common_utils.design_pattern

import com.fido.common.common_utils.design_pattern.strategy.BaseRule
import com.fido.common.common_utils.design_pattern.strategy.RuleResult

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:
 */


data class JobCheck(
    //人物要求
    val nric: String,
    val visa: String,
    val nationality: String,
    var age: Float,
    val workingYear: Float,
    var isFillProfile: Boolean,

    var hasCovidTest: Boolean,

    //工作要求
    val language: String,
    val gender: Int,
    val deposit: String,
    val isSelfProvide: Boolean,
    val isTrained: Boolean,
)

class COVIDRule(private val failedBlock:()->Unit):BaseRule<JobCheck>{
    override fun execute(rule: JobCheck): RuleResult {
        return if (rule.hasCovidTest) {
            RuleResult(true)
        } else {
            failedBlock.invoke()
            RuleResult(false,"你没有做检查")
        }
    }
}

class AgeRule(private val youngerBlock:()->Unit):BaseRule<JobCheck>{
    override fun execute(rule: JobCheck): RuleResult {
        return if (rule.age > 18) {
            RuleResult(true)
        } else {
            youngerBlock.invoke()
            return RuleResult(false,"太年轻啦老弟")
        }
    }

}

class GenderRule:BaseRule<JobCheck>{
    override fun execute(rule: JobCheck): RuleResult {
        return RuleResult(rule.gender !=0 )
    }
}

class FillProfileRule(val action:()->Unit) : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.isFillProfile) {
            RuleResult(true)
        } else {
            action.invoke()
            RuleResult(false, "请完善用户详细信息")
        }

    }

}
class LanguageRule(private val requirLanguages: List<String>) :BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (requirLanguages.contains(rule.language)) {
            RuleResult(true)
        } else {
            RuleResult(false, "语言不符合此工作")
        }

    }

}

class NationalityRule(private val requirNationalitys: List<String>) : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (requirNationalitys.contains(rule.nationality)) {
            RuleResult(true)
        } else {
            RuleResult(false, "国籍不符合此工作")
        }

    }

}

class VisaRule : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.visa.lowercase() == "Singapore".lowercase()) {
            RuleResult(true)
        } else {
            RuleResult(false, "签证不满足工作需求")
        }


    }

}


