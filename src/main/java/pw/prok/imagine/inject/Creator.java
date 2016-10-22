// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

import java.lang.reflect.Constructor;
import java.util.Stack;

public class Creator<D> implements IConstructorBuilder<D, ConstructorBuilder<D>>
{
    private static final IFilter INT_FILTER;
    private final Class<D> mClass;
    
    public static <D> Creator<D> creator(final Class<D> clazz) {
        return new Creator<D>(clazz);
    }
    
    public static <D> Creator<D> creator(final String className) {
        return creator(Creator.class.getClassLoader(), className);
    }
    
    public static <D> Creator<D> creator(final ClassLoader classLoader, final String className) {
        try {
            final Class<D> clazz = (Class<D>)Class.forName(className, true, classLoader);
            return creator(clazz);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public Creator(final Class<D> clazz) {
        assert clazz != null;
        this.mClass = clazz;
    }
    
    public ConstructorBuilder<D> builder() {
        return new ConstructorBuilder<D>(this.mClass);
    }
    
    @Override
    public ConstructorBuilder<D> atLeast(final Class<?> clazz) {
        return this.builder().atLeast(clazz);
    }
    
    @Override
    public <T, V extends T> ConstructorBuilder<D> arg(final Class<T> clazz, final V value) {
        return this.builder().arg(clazz, value);
    }
    
    @Override
    public <T, V extends T> ConstructorBuilder<D> arg(final int pos, final Class<T> clazz, final V value) {
        return this.builder().arg(pos, clazz, value);
    }
    
    @Override
    public <V> ConstructorBuilder<D> arg(final V value) {
        return this.builder().arg(value);
    }
    
    @Override
    public <V> ConstructorBuilder<D> arg(final int pos, final V value) {
        return this.builder().arg(pos, value);
    }
    
    @Override
    public ConstructorBuilder<D> arg(final int value) {
        return this.builder().arg(value);
    }
    
    @Override
    public ConstructorBuilder<D> arg(final int pos, final int value) {
        return this.builder().arg(pos, value);
    }
    
    @Override
    public D build() {
        return this.builder().build();
    }
    
    @Override
    public <V> ConstructorBuilder<D> args(final V... args) {
        return this.builder().args(args);
    }
    
    @Override
    public Class<? extends D> clazz() {
        return (Class<? extends D>)this.mClass;
    }
    
    static {
        INT_FILTER = new DynamicClassFilter(Integer.TYPE);
    }
    
    public static class ConstructorBuilder<D> implements IConstructorBuilder<D, ConstructorBuilder<D>>
    {
        private final Class<D> mClass;
        private final Stack<IFilter> mFilters;
        private final Stack<Object> mArgStack;
        private Constructor<D> mConstructor;
        private boolean mConstructorFound;
        private Object[] mConstructorArgs;
        private Class<?> mAtLeast;
        
        public ConstructorBuilder(final Class<D> clazz) {
            this.mConstructorFound = false;
            this.mClass = clazz;
            this.mFilters = new Stack<IFilter>();
            this.mArgStack = new Stack<Object>();
        }
        
        public boolean valid() {
            return this.constructor() != null;
        }
        
        public Constructor<D> constructor() {
            if (!this.mConstructorFound) {
                Class<? extends D> clazz = (Class<? extends D>)this.mClass;
                if (this.mAtLeast != null && clazz.isAssignableFrom(this.mAtLeast)) {
                    clazz = (Class<? extends D>)this.mAtLeast;
                }
                final int argSize = this.mArgStack.size();
                if (argSize == 0) {
                    try {
                        this.mConstructor = (Constructor<D>)clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
                        this.mConstructorFound = true;
                        return this.mConstructor;
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Default constructor not found", e);
                    }
                }
                for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    final Class<?>[] types = constructor.getParameterTypes();
                    if (types.length == argSize) {
                        boolean constructorMatching = true;
                        for (int i = types.length - 1; i >= 0; --i) {
                            final IFilter filter = this.mFilters.get(i);
                            if (!filter.match(types[i])) {
                                constructorMatching = false;
                                break;
                            }
                        }
                        if (constructorMatching) {
                            this.mConstructor = (Constructor<D>)constructor;
                        }
                    }
                }
                this.mConstructorFound = true;
            }
            return this.mConstructor;
        }
        
        @Override
        public D build() {
            final Constructor<D> constructor = this.constructor();
            if (constructor == null) {
                return null;
            }
            if (this.mConstructorArgs == null) {
                this.mConstructorArgs = this.mArgStack.toArray();
            }
            try {
                return constructor.newInstance(this.mConstructorArgs);
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to instantiate", e);
            }
        }
        
        @Override
        public <V> ConstructorBuilder<D> args(final V... args) {
            if (args == null || args.length == 0) {
                return this;
            }
            for (final V arg : args) {
                this.arg(arg);
            }
            return this;
        }
        
        @Override
        public Class<? extends D> clazz() {
            return (Class<? extends D>)this.mClass;
        }
        
        private ConstructorBuilder<D> iArg(final int pos, final Object value, final IFilter filter) {
            this.mArgStack.setElementAt(value, pos);
            this.mFilters.setElementAt(filter, pos);
            this.mConstructor = null;
            this.mConstructorFound = false;
            this.mConstructorArgs = null;
            return this;
        }
        
        private ConstructorBuilder<D> iArg(final Object value, final IFilter filter) {
            this.mArgStack.push(value);
            this.mFilters.push(filter);
            this.mConstructor = null;
            this.mConstructorFound = false;
            this.mConstructorArgs = null;
            return this;
        }
        
        @Override
        public ConstructorBuilder<D> atLeast(final Class<?> clazz) {
            this.mAtLeast = clazz;
            return this;
        }
        
        @Override
        public <T, V extends T> ConstructorBuilder<D> arg(final Class<T> clazz, final V value) {
            return this.iArg(value, new FixedClassFilter<Object>(clazz));
        }
        
        @Override
        public <T, V extends T> ConstructorBuilder<D> arg(final int pos, final Class<T> clazz, final V value) {
            return this.iArg(pos, value, new FixedClassFilter<Object>(clazz));
        }
        
        @Override
        public <V> ConstructorBuilder<D> arg(final V value) {
            return this.iArg(value, new DynamicClassFilter(value.getClass()));
        }
        
        @Override
        public <V> ConstructorBuilder<D> arg(final int pos, final V value) {
            return this.iArg(pos, value, new DynamicClassFilter(value.getClass()));
        }
        
        @Override
        public ConstructorBuilder<D> arg(final int value) {
            return this.iArg(value, Creator.INT_FILTER);
        }
        
        @Override
        public ConstructorBuilder<D> arg(final int pos, final int value) {
            return this.iArg(pos, value, Creator.INT_FILTER);
        }
    }
    
    private static class FixedClassFilter<T> implements IFilter
    {
        private final Class<T> mClass;
        
        public FixedClassFilter(final Class<T> clazz) {
            this.mClass = clazz;
        }
        
        @Override
        public boolean match(final Class<?> argType) {
            return argType == this.mClass;
        }
    }
    
    private static class DynamicClassFilter implements IFilter
    {
        private final Class<?> mClass;
        
        public DynamicClassFilter(final Class<?> clazz) {
            this.mClass = clazz;
        }
        
        @Override
        public boolean match(final Class<?> argType) {
            return argType.isAssignableFrom(this.mClass);
        }
    }
    
    private interface IFilter
    {
        boolean match(final Class<?> p0);
    }
}
