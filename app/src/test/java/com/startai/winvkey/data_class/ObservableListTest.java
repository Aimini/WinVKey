package com.startai.winvkey.data_class;

import com.win_vkey.startai.winvkey.data_class.Key;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 20:39 2018/11/15
 * @Modified By:
 */
public class ObservableListTest {
    ObservableList<Integer> list =new ObservableList<Integer>();
    ListObserver<Integer> ob = new ListObserver<Integer>(){

        @Override
        public void add(List<Integer> source, Integer value) {
            System.out.println("list:" + source.toString());
            System.out.println("add int:" + value.toString());

        }

        @Override
        public void delete(List<Integer> source, Integer value) {
            System.out.println("list:" + source.toString());
            System.out.println("delete int:" + value.toString());

        }

        @Override
        public void set(List<Integer> source, int index, Integer value) {
            System.out.println("list:" + source.toString());
            System.out.println("set int:" + source.get(index).toString() + "->" + value.toString());

        }
    };

    @Before
    public void setUp() throws Exception {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.addObserver(ob);
    }


    @After
    public void  done() throws Exception{
        System.out.println(list.toString());
    }




    @Test
    public void deleteObserver() {
        list.add(10);
        list.deleteObserver(ob);
        list.add(11);
    }

    @Test
    public void deleteObservers() {
        list.add(10);
        list.deleteObservers();
        list.add(11);
    }

    @Test
    public void set() {
        list.set(1,12);
    }

    @Test
    public void add() {
        list.add(4);
    }

    @Test
    public void add1() {
        list.add(1,12);
    }

    @Test
    public void remove() {
        list.remove(1);
    }

    @Test
    public void remove1() {
        list.remove(new Integer(3));
    }

    @Test
    public void clear() {
        list.clear();
    }

    @Test
    public void addAll() {
        list.addAll(Arrays.asList(4,5,6));
    }

    @Test
    public void addAll1() {
        list.addAll(0,Arrays.asList(-2,-1,0));
    }

    @Test
    public void removeRange() {
        list.removeRange(0,2);
    }

    @Test
    public void removeAll() {
        list.removeAll(Arrays.asList(-1,1,2));
    }

    @Test
    public void retainAll() {
        list.retainAll(Arrays.asList(-1,1,2));
    }

    @Test
    public void removeIf() {
        list.removeIf(integer -> integer%2 == 0);
    }

    @Test
    public void replaceAll() {
        list.replaceAll(integer -> {
            if(integer%2 == 0)
               return integer + 2;
            return integer;
        });
    }
}