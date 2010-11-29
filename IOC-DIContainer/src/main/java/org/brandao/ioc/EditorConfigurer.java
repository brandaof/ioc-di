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

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;
import org.brandao.ioc.editors.*;

/**
 *
 * @author Afonso Brandao
 */
public class EditorConfigurer {

    private final static Map editors;

    static{
        editors = new HashMap();
        editors.put( boolean.class.getName(),   new BooleanEditorSupport() );
        editors.put( Boolean.class.getName(),   new BooleanEditorSupport() );
        editors.put( byte.class.getName(),      new ByteEditorSupport() );
        editors.put( Byte.class.getName(),      new ByteEditorSupport() );
        editors.put( Character.class.getName(), new CharacterEditorSupport() );
        editors.put( char.class.getName(),      new CharacterEditorSupport() );
        editors.put( Class.class.getName(),     new ClassEditorSupport() );
        editors.put( Double.class.getName(),    new DoubleEditorSupport() );
        editors.put( double.class.getName(),    new DoubleEditorSupport() );
        editors.put( Float.class.getName(),     new FloatEditorSupport() );
        editors.put( float.class.getName(),     new FloatEditorSupport() );
        editors.put( Integer.class.getName(),   new IntegerEditorSupport() );
        editors.put( int.class.getName(),       new IntegerEditorSupport() );
        editors.put( Long.class.getName(),      new LongEditorSupport() );
        editors.put( long.class.getName(),      new LongEditorSupport() );
        editors.put( Short.class.getName(),     new ShortEditorSupport() );
        editors.put( short.class.getName(),     new ShortEditorSupport() );
        editors.put( String.class.getName(),    new StringEditorSupport() );
    }

    public static void registerPropertyEditor( String name, PropertyEditor editor ){
        editors.put(name, editor);
    }
    
    public static PropertyEditor getPropertyEditor( String name ){
        PropertyEditor editor = (PropertyEditor) editors.get(name);
        return editor;
    }
    
}
