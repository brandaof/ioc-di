/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2009 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * This library is free software. You can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License (GPL) version 3.0 or (at your option) any later
 * version.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Distributed WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 */

package org.brandao.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Afonso Brandao
 */
public class TestHelper {

    public static enum MyEnum{
        VALUE1,
        VALUE2,
        VALUE3
    }

    public static class MyBeanByContructor{
        
        private int intValue;
        private String stringValue;
        private MySimpleBean bean;

        public MyBeanByContructor( String arg0, int arg1, MySimpleBean arg3 ){
            this.intValue = arg1;
            this.stringValue = arg0;
            this.bean = arg3;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public MySimpleBean getBean() {
            return bean;
        }

        public void setBean(MySimpleBean bean) {
            this.bean = bean;
        }

    }

    public static class MySimpleBean{
    }

    public static class MyBean{

        private MySimpleBean bean;

        public MyBean( MySimpleBean bean ){
            this.bean = bean;
        }

        public MyBean(){
        }

        public MySimpleBean getBean() {
            return bean;
        }

        public void setBean(MySimpleBean bean) {
            this.bean = bean;
        }
        

    }

    public static class MyFactory{

        public MySimpleBean getInstance(){
            return new MySimpleBean();
        }
    }

    public static class CustomScope implements Scope{

        private final Map<String,Object> data;

        public CustomScope() {
            this.data = new HashMap<String,Object>();
        }

        public void put(String name, Object value) {
            data.put( name, value );
        }

        public Object get(String name) {
            return data.get( name );
        }

        public String getName(){
            return "customScope";
        }

    }
}
