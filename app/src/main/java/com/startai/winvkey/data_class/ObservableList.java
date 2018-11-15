package com.startai.winvkey.data_class;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 23:23 2018/11/14
 * @Modified By:
 */
public class ObservableList<E> extends ArrayList<E> {
    private Observable<E> mObservable = new Observable<E>(this);

    public void addObserver(ListObserver<E> ob){
        mObservable.addObserver(ob);
    }

    public void deleteObserver(ListObserver<E> ob){
        mObservable.deleteObserver(ob);
    }

    public void deleteObservers(){
        mObservable.deleteObservers();
    }

    @Override
    public E set(int index, E element) {
        mObservable.set(index,element);
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        mObservable.add(e);
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        mObservable.add(element);
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        mObservable.delete(this.get(index));
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        try {
            mObservable.delete((E) o);
        }catch (ClassCastException e){
            System.out.println("how can you put a Object value to generic type list");
        }
        return super.remove(o);
    }

    @Override
    public void clear() {
        for (E e : this) {
            mObservable.delete(e);
        }
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            mObservable.add(e);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            mObservable.add(e);
        }
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            mObservable.delete(this.get(i));
        }
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object e : c) {
            if(this.contains(e)) {
                try {
                    mObservable.delete((E) e);
                }catch (ClassCastException ex){
                    System.out.println("how can you put a Object value to generic type list");
                }
            }
        }
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (E e : this) {
            if(!c.contains(e)) {
                    mObservable.delete(e);
            }
        }
        return super.retainAll(c);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        for(E e:this){
            if(filter.test(e)){
                mObservable.delete(e);
            }
        }
        return super.removeIf(filter);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        ArrayList<E> calculateCache = new ArrayList<E>(ObservableList.this.size());
        UnaryOperator<E> cacheOperator = new UnaryOperator<E>(){
            int index = 0;
            @Override
            public E apply(E e) {
                return calculateCache.get(index++);
            }
        };

        for (int i = 0; i < this.size(); i++) {
            E raw = this.get(i);
            E result = operator.apply(raw);
            calculateCache.add(result);
            if(result != raw)
                mObservable.set(i,result);
        }
        super.replaceAll(cacheOperator);


    }

     enum MODIFY_TYPE{
        ADD,
        DEL,
        SET
    }

     class Observable<T> extends PausableObservable{
        public List<T> mSource;

        public Observable(List<T> mSource) {
            this.mSource = mSource;
        }

        public void add(T value){
            this.modify(MODIFY_TYPE.ADD, value);
        }

        public void delete(T value){
            this.modify(MODIFY_TYPE.DEL, value);
        }

        public void set(int index,T value){
            NotifyInfo<T> notify = new NotifyInfo<T>(this.mSource, MODIFY_TYPE.SET, value);
            notify.index = index;
            setChanged();
            super.notifyObservers(notify);
        }

        public void modify(MODIFY_TYPE type, T value) {
            setChanged();
            super.notifyObservers(new NotifyInfo<T>(this.mSource,type,value));
        }
    }

     class NotifyInfo<T>{
        List<T> source;
        MODIFY_TYPE type;
        int index;// for set(index,value)
        T value;

        NotifyInfo(List<T> source, MODIFY_TYPE type, T value) {
            this.source = source;
            this.type = type;
            this.value = value;
        }
    }
}

 abstract class ListObserver<T> implements java.util.Observer{
    @Override
    public void update(java.util.Observable o, Object arg) {
        try{
            if(arg instanceof ObservableList.NotifyInfo){
                List<T> source =  ((ObservableList<T>.NotifyInfo<T>) arg).source;
                ObservableList.MODIFY_TYPE type = ((ObservableList<T>.NotifyInfo<T>) arg).type;
                T value = (T) ((ObservableList<T>.NotifyInfo<T>) arg).value;
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
     abstract public void add(List<T> source,T value);
     abstract public void delete(List<T> source,T value);
     abstract public void set(List<T> source,int index,T value);
}
