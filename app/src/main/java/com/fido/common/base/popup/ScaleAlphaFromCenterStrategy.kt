package com.fido.common.base.popup

import com.fido.common.base.popup.interf.IPopupAnimationStrategy
import com.lxj.xpopup.enums.PopupAnimation

/**
 * @author: FiDo
 * @date: 2024/6/11
 * @des:
 */
class ScaleAlphaFromCenterStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.ScaleAlphaFromCenter
    }
}

class ScaleAlphaFromLeftTopStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.ScaleAlphaFromLeftTop
    }
}

class ScaleAlphaFromRightTopStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.ScaleAlphaFromRightTop
    }
}

class ScaleAlphaFromLeftBottomStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.ScaleAlphaFromLeftBottom
    }
}

class ScaleAlphaFromRightBottomStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.ScaleAlphaFromRightBottom
    }
}

class TranslateAlphaFromLeftStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateAlphaFromLeft
    }
}

class TranslateAlphaFromRightStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateAlphaFromRight
    }
}

class TranslateAlphaFromTopStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateAlphaFromTop
    }
}

class TranslateAlphaFromBottomStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateAlphaFromBottom
    }
}

class TranslateFromLeftStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateFromLeft
    }
}

class TranslateFromRightStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateFromRight
    }
}

class TranslateFromTopStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateFromTop
    }
}

class TranslateFromBottomStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.TranslateFromBottom
    }
}

class NoAnimationStrategy : IPopupAnimationStrategy {
    override fun getAnimation(): PopupAnimation {
        return PopupAnimation.NoAnimation
    }
}

