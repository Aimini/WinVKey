package com.startai.winvkey.data_class;

import android.util.Log;

import java.util.List;

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 1:20 2018/11/16
 * @Modified By:
 */
public abstract class  ObservableListObserver<T>  implements java.util.Observer{
    @Override
    public void update(java.util.Observable o, Object arg) {
        try{
            if(arg instanceof ObservableList.NotifyInfo){
                ObservableList<T> source =  ((ObservableList<T>.NotifyInfo<T>) arg).source;
                ObservableList.MODIFY_TYPE type = ((ObservableList<T>.NotifyInfo<T>) arg).type;
                T value =  ((ObservableList<T>.NotifyInfo<T>) arg).value;
                switch (type) {
                    case ADD:
                        this.add(source,value);
                        break;
                    case DEL:
                        this.delete(source,value);
                        break;
                    case SET:
                        this.set(source, ((ObservableList.NotifyInfo) arg).index, value);
                        break;
                }
            }
        }catch (ClassCastException e){
            Log.e(getClass().toString(),"not suitable Observable's info");
        }
    }

    // all these function will called before data change
    abstract public void add(ObservableList<T> source,T value);
    abstract public void delete(ObservableList<T> source,T value);
    abstract public void set(ObservableList<T> source,int index,T value);
}
