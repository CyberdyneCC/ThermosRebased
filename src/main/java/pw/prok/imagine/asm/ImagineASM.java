// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.api.Triple;
import pw.prok.imagine.api.Pair;
import java.util.ArrayList;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.List;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.FieldNode;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import pw.prok.imagine.ImagineLoadingPlugin;
import org.objectweb.asm.tree.ClassNode;

public class ImagineASM
{
    private static final Filter CONSTRUCTOR_FILTER;
    protected byte[] mClass;
    protected boolean mDev;
    protected ClassNode mClassNode;
    protected String mName;
    protected String mDevName;
    
    public ImagineASM() {
        this.mDev = ImagineLoadingPlugin.DEV;
    }
    
    public static ImagineASM create() {
        return new ImagineASM();
    }
    
    public ImagineASM loadClass(final byte[] bytes) {
        return this.loadClass(null, null, bytes);
    }
    
    public ImagineASM loadClass(final String name, final String devName, final byte[] bytes) {
        this.mClass = bytes;
        this.mClassNode = null;
        this.mName = name;
        this.mDevName = devName;
        if (this.mName == null) {
            this.reset();
            this.mName = toDesc(this.mClassNode.name);
            this.mClassNode = null;
        }
        if (this.mDevName == null) {
            this.mDevName = toDesc(ImagineRemapper.clazzDev(this.mName));
        }
        return this;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getDevName() {
        return this.mDevName;
    }
    
    public boolean is(final String name) {
        return toDesc(this.mDevName).equals(toDesc(ImagineRemapper.clazzDev(name)));
    }
    
    public boolean isDev() {
        return this.mDev;
    }
    
    protected void readClass() {
        if (this.mClassNode != null) {
            return;
        }
        this.reset();
    }
    
    public ImagineASM reset() {
        if (this.mClass == null) {
            this.mClassNode = null;
            return this;
        }
        this.mClassNode = new ClassNode(327680);
        final ClassReader reader = new ClassReader(this.mClass);
        reader.accept((ClassVisitor)this.mClassNode, 8);
        return this;
    }
    
    public ImagineASM clear() {
        this.mClass = null;
        this.mClassNode = null;
        this.mName = null;
        this.mDevName = null;
        return null;
    }
    
    public byte[] build() {
        if (this.mClassNode == null) {
            return this.mClass;
        }
        final ClassWriter writer = new ClassWriter(1);
        this.mClassNode.accept((ClassVisitor)writer);
        return writer.toByteArray();
    }
    
    public ImagineASM renameClass(final String name) {
        this.readClass();
        this.mClassNode.name = toDesc(name);
        return this;
    }
    
    public ImagineASM renameClass(final String obfName, final String devName) {
        return this.renameClass(this.mDev ? devName : obfName);
    }
    
    public ImagineASM renameMappedClass(final String name) {
        return this.renameClass(this.mapClazz(name));
    }
    
    public ImagineMethod method(final String name, final String desc) {
        return this.method(name, desc, true);
    }
    
    public ImagineMethod method(String name, String desc, final boolean map) {
        this.readClass();
        final MethodDesc m = this.mapMethod(toDesc(this.mName), name, desc, map);
        for (final MethodNode method : this.mClassNode.methods) {
            if (((Pair<X, String>)m).second().equals(method.name) && ((Triple<X, Y, String>)m).third().equals(method.desc)) {
                return new ImagineMethod(this, method);
            }
        }
        name = ((Pair<X, String>)m).second();
        desc = ((Triple<X, Y, String>)m).third();
        throw new RuntimeException(new NoSuchMethodException(name + desc));
    }
    
    public ImagineMethod addMethod(final int flags, final String name, final String desc) {
        this.readClass();
        final MethodNode node = new MethodNode(327680, flags, name, desc, (String)null, (String[])null);
        this.mClassNode.methods.add(node);
        return new ImagineMethod(this, node);
    }
    
    public static String toDesc(final String name) {
        return (name == null) ? null : name.replace('.', '/');
    }
    
    public static String toName(final String desc) {
        return (desc == null) ? null : desc.replace('/', '.');
    }
    
    public String mapClazz(final String name) {
        return ImagineRemapper.clazz(toDesc(name), this.mapping());
    }
    
    public Mapping mapping() {
        return this.mDev ? Mapping.DEV : Mapping.OBF;
    }
    
    public String mapDesc(final String desc) {
        return this.mapDesc(desc, true);
    }
    
    public String mapDesc(final String desc, final boolean map) {
        if (!map) {
            return desc;
        }
        return ImagineRemapper.desc(desc, this.mapping());
    }
    
    public ImagineField field(final String name) {
        return this.field(name, null, true);
    }
    
    public ImagineField field(final String name, final String desc) {
        return this.field(name, desc, true);
    }
    
    public ImagineField field(String name, String desc, final boolean map) {
        this.readClass();
        final FieldDesc m = this.mapField(toDesc(this.mName), name, map);
        desc = ((desc != null) ? this.mapDesc(desc, map) : null);
        for (final FieldNode field : this.mClassNode.fields) {
            if (((Pair<X, String>)m).second().equals(field.name) && (desc == null || desc.equals(field.desc))) {
                return new ImagineField(this, field);
            }
        }
        name = ((Pair<String, Y>)m).first();
        desc = ((Pair<X, String>)m).second();
        throw new RuntimeException(new NoSuchFieldException((desc == null) ? name : (desc + name)));
    }
    
    public ImagineField addField(final String name, final String desc, final Object value) {
        this.readClass();
        final FieldNode node = new FieldNode(327680, 0, name, desc, (String)null, value);
        this.mClassNode.fields.add(node);
        return new ImagineField(this, node);
    }
    
    public FieldDesc mapField(final String owner, final String name) {
        return this.mapField(owner, name, true);
    }
    
    public FieldDesc mapField(final String owner, final String name, final boolean map) {
        if (!map) {
            return new FieldDesc(owner, name);
        }
        return ImagineRemapper.field(owner, name, this.mapping());
    }
    
    public MethodDesc mapMethod(final String owner, final String method, final String desc) {
        return this.mapMethod(owner, method, desc, true);
    }
    
    public MethodDesc mapMethod(final String owner, final String method, final String desc, final boolean map) {
        if (!map) {
            return new MethodDesc(owner, method, desc);
        }
        return ImagineRemapper.method(owner, method, desc, this.mapping());
    }
    
    public String getActualName() {
        if (this.mClassNode != null) {
            return toName(this.mClassNode.name);
        }
        return this.mName;
    }
    
    public ImagineMethod constructor(final String desc) {
        return this.method("<init>", desc);
    }
    
    public Iterable<ImagineMethod> constructors() {
        return this.filter(ImagineASM.CONSTRUCTOR_FILTER);
    }
    
    public ImagineASM constructors(final Action<ImagineMethod> action) {
        return this.action(this.constructors(), action);
    }
    
    public Iterable<ImagineMethod> filter(final Filter filter) {
        return new MethodFilter(this, filter);
    }
    
    public ImagineASM action(final Iterable<ImagineMethod> methods, final Action<ImagineMethod> action) {
        for (final ImagineMethod method : methods) {
            action.action(method);
        }
        return this;
    }
    
    public ImagineASM action(final Filter filter, final Action<ImagineMethod> action) {
        return this.action(this.filter(filter), action);
    }
    
    public static List<AbstractInsnNode> asList(final InsnList instructions) {
        final List<AbstractInsnNode> list = new ArrayList<AbstractInsnNode>(instructions.size());
        final Iterator<AbstractInsnNode> iterator = (Iterator<AbstractInsnNode>)instructions.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
    
    public ClassNode getClassNode() {
        this.readClass();
        return this.mClassNode;
    }
    
    static {
        CONSTRUCTOR_FILTER = new Filter() {
            @Override
            public boolean matching(final ImagineMethod method) {
                return "<init>".equals(method.getName());
            }
        };
    }
}
