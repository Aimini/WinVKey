package com.startai.winvkey.data_class

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 18:45 2018/11/15
 * @Modified By:
 */
open class PausableObservable : java.util.Observable(){
    var stopNotify:Boolean = false


    inline fun pause(inline:()->Unit){
        this.stopNotify = true
        inline()
        this.stopNotify = false
    }

    override fun notifyObservers(arg: Any) {
        if(!stopNotify)
            super.notifyObservers(arg)
    }

}
