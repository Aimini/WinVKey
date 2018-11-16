package com.startai.winvkey.data_class;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 16:35 2018/11/16
 * @Modified By:
 */
public abstract class ObservableDataListObserver<E extends ObservableData> extends ObservableListObserver<E>{
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ObservableData.ChangeInfo) {
            try {
                ObservableData.ChangeInfo<E> castObj = (ObservableData.ChangeInfo) arg;
                this.itemChanged(castObj.getObj$app_debug(), castObj.getProperty$app_debug(), castObj.getOldVaule$app_debug(), castObj.getNewVaule$app_debug());
            } catch (ClassCastException e) {
                System.out.println("not a suitable arg for DataObservable: " + arg.getClass().getName());
            }
        }else{
            super.update(o, arg);
        }
    }

    public void itemChanged(E obj, @NotNull KProperty<?> property, @NotNull Object oldValue, @NotNull Object newValue){}
}
