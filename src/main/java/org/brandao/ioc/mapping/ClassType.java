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
package org.brandao.ioc.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 *
* @author Afonso Brandao
 */
public final class ClassType {
    
    private final static Map primitiveType;
    
    static{
        primitiveType = new HashMap();
        primitiveType.put("boolean",java.lang.Boolean.TYPE);
        primitiveType.put("byte",java.lang.Byte.TYPE);
        primitiveType.put("char",java.lang.Character.TYPE);
        primitiveType.put("double",java.lang.Double.TYPE);
        primitiveType.put("float",java.lang.Float.TYPE);
        primitiveType.put("int",java.lang.Integer.TYPE);
        primitiveType.put("long",java.lang.Long.TYPE);
        primitiveType.put("short",java.lang.Short.TYPE);
        primitiveType.put("void",java.lang.Void.TYPE);
        
        primitiveType.put(boolean.class,java.lang.Boolean.class);
        primitiveType.put(byte.class,java.lang.Byte.class);
        primitiveType.put(char.class,java.lang.Character.class);
        primitiveType.put(double.class,java.lang.Double.class);
        primitiveType.put(float.class,java.lang.Float.class);
        primitiveType.put(int.class,java.lang.Integer.class);
        primitiveType.put(long.class,java.lang.Long.class);
        primitiveType.put(short.class,java.lang.Short.class);
        primitiveType.put(void.class,java.lang.Void.class);
    }
    
    public static Class getWrapper( Class clazz ){
        Class classe = (Class) primitiveType.get( clazz );
        
        return classe == null? clazz : classe;
        
    }
    
    public static Class get( String name ) throws ClassNotFoundException{
        Class classe = (Class) primitiveType.get( name );
        
        return classe == null? getClasse( name ) : classe;
        
    }
    
    private static Class getClasse( String name ) throws ClassNotFoundException{
        return Class.forName( name, true, Thread.currentThread().getContextClassLoader() );
    }
}
