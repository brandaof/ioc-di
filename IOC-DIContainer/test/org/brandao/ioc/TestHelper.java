/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2010 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

        public MyBeanByContructor( String arg0, Integer arg1, MySimpleBean arg3 ){
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

        public String getName(){
            return "customScope";
        }

        public Object get(String beanName, ObjectFactory factory) {

            if( data.containsKey(beanName) )
                return data.get(beanName);
            else{
                Object value = factory.getObject();
                data.put( beanName, value );
                return value;
            }

        }

        public void remove(String name) {
            data.remove(name);
        }

    }
}
