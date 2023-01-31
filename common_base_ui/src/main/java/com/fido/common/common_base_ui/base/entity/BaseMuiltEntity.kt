package com.fido.common.common_base_ui.base.entity


open class BaseMuiltEntity{

    var itemType:Int = 0
    var itemLayoutRes:Int = 0

    constructor()

    constructor(itemType: Int, itemLayoutRes: Int) {
        this.itemType = itemType
        this.itemLayoutRes = itemLayoutRes
    }

}
