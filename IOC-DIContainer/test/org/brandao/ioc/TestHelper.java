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
    
}
