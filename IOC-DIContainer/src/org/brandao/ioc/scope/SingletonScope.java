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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package org.brandao.ioc.scope;

import java.util.HashMap;
import java.util.Map;
import org.brandao.ioc.ObjectFactory;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class SingletonScope implements Scope{

    private final Map<String,Object> data;

    public SingletonScope() {
        this.data = new HashMap<String,Object>();
    }

    public void put(String name, Object value) {
        synchronized( data ){
            if(!data.containsKey(name) )
                data.put( name, value );
        }
    }

    public Object get(String name) {
        return data.get( name );
    }

    public String getName(){
        return ScopeType.SINGLETON.toString();
    }

    public Object get(String beanName, ObjectFactory factory) {
        
        if( data.containsKey(beanName) )
            return data.get(beanName);
        else
            return get0(beanName, factory);
        
    }

    protected Object get0(String beanName, ObjectFactory factory){
        synchronized( data ){
            if(!data.containsKey(beanName) ){
                Object value = factory.getObject();
                data.put( beanName, value );
                return value;
            }
            else
                return data.get(beanName);
        }
    }

    public void remove(String name) {
        data.remove(name);
    }

}
