// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object;

import java.lang.reflect.Method;

public class MethodHandler implements IHandler<Method>
{
    @Override
    public void putData(final Object object, final Method method, final Object value) throws Exception {
        method.invoke(object, value);
    }
    
    @Override
    public Object getData(final Object object, final Method method) throws Exception {
        return method.invoke(object, new Object[0]);
    }
}
