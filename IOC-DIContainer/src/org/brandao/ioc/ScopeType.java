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
