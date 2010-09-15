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
package org.brandao.ioc.mapping;

/**
 *
 * @author Afonso Brandao
 */
public class SetInject extends ComplexObjectInject{
    
    public SetInject( String name, Class<?> valueType, Class<?> type, String factory, Property ... props ) {
        //super( type == null? java.util.HashSet.class : type, name, props );
        super( name, null, valueType, java.util.Set.class, factory, props );
        setType( type == null? java.util.HashSet.class : type );
    }
    
}
