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

package org.brandao.ioc.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Afonso Brandao
 */
public class SetterProperty {

    /**
     * @deprecated
     */
    private Field field;
    private Object object;
    private Method method;

    /**
     * @deprecated 
     * @param field
     * @param object
     */
    public SetterProperty( Field field, Object object ){
        this.field = field;
        this.object = object;
    }

    public SetterProperty( Method method, Object object ){
        this.method = method;
        this.object = object;
    }

    public void set( Object value ) throws Exception{
        /*
        String fieldName = field.getName();
        String methodName = "set" +
                            String.valueOf( fieldName.charAt( 0 ) ).toUpperCase() +
                            fieldName.substring( 1, fieldName.length() );

        Method setter = field.getDeclaringClass().getMethod( methodName, field.getType() );
        */
        method.invoke( object, new Object[]{value} );
    }

    public Method getMethod(){
        return method;
    }

}
