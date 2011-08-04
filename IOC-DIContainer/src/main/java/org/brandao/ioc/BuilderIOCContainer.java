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

/**
 *
 * @author Brandao
 */
public class BuilderIOCContainer extends IOCContainer{

    public BeanBuilder addBean( Class clazz ){
        return addBean( clazz.getName(), clazz );
    }

    public BeanBuilder addBean( Class clazz, Class classType ){
        return addBean(clazz.getName(),classType);
    }
    
    public BeanBuilder addBean( String name, Class classType ){
        return addBean( name, classType, ScopeType.PROTOTYPE, false, null );
    }

    public BeanBuilder addBean( Class clazz, Class classType, ScopeType scope ){
        return addBean( clazz.getName(), classType, scope );
    }
    
    public BeanBuilder addBean( String name, Class classType, ScopeType scope ){
        return addBean( name, classType, scope, false, null );
    }

    public BeanBuilder addSingleton( String name, String factory ){
        return addBean( name, null, null, true, factory );
    }

    public BeanBuilder addSingleton( String name, Class classType ){
        return addBean( name, classType, null, true, null );
    }

    public BeanBuilder addBean( String name, Class classType, ScopeType scope,
            boolean singleton, String factory ){
        return super.addBean(name, classType, scope, singleton, factory);
    }

}
