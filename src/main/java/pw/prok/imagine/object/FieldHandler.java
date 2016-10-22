// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object;

import java.lang.reflect.Field;

public class FieldHandler implements IHandler<Field>
{
    @Override
    public void putData(final Object object, final Field field, final Object value) throws Exception {
        field.set(object, value);
    }
    
    @Override
    public Object getData(final Object object, final Field field) throws Exception {
        return field.get(object);
    }
}
