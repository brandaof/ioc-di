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
public class ScopeType {

    public static final ScopeType SINGLETON = new ScopeType( "singleton" );
    public static final ScopeType PROTOTYPE = new ScopeType( "prototype" );
    public static final ScopeType GLOBAL = new ScopeType( "global" );
    public static final ScopeType REQUEST = new ScopeType( "request" );
    public static final ScopeType SESSION = new ScopeType( "session" );

    private static Map defaultScopes = new HashMap();

    static{
        defaultScopes.put( SINGLETON.toString() , SINGLETON );
        defaultScopes.put( PROTOTYPE.toString() , PROTOTYPE );
        defaultScopes.put( GLOBAL.toString() , GLOBAL );
        defaultScopes.put( REQUEST.toString() , REQUEST );
        defaultScopes.put( SESSION.toString() , SESSION );
    }

    private String name;

    public ScopeType( String name ){
        this.name = name;
    }
            
    public String toString(){
        return this.name;
    }

    public static ScopeType valueOf( String value ){
        if( defaultScopes.containsKey(value) )
            return (ScopeType)defaultScopes.get( value );
        else
            return new ScopeType( value );
    }
}
