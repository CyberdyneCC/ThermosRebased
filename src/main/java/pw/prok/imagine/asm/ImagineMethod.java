// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.api.Triple;
import pw.prok.imagine.api.Pair;
import java.util.Collections;
import java.util.List;
import pw.prok.imagine.collections.CuttableList;
import pw.prok.imagine.collections.LazyIterable;
import java.util.ListIterator;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ImagineMethod extends ImagineAccess<ImagineMethod>
{
    private final ImagineASM mAsm;
    private final MethodNode mMethod;
    
    ImagineMethod(final ImagineASM asm, final MethodNode method) {
        this.mAsm = asm;
        this.mMethod = method;
    }
    
    public ImagineMethod exception(final String... exceptions) {
        if (exceptions == null) {
            return this;
        }
        for (final String exception : exceptions) {
            this.mMethod.exceptions.add(this.mAsm.mapClazz(exception));
        }
        return this;
    }
    
    public ImagineMethod constMethod(final int value) {
        final InsnList instructions = this.mMethod.instructions;
        instructions.clear();
        pushInt(value, instructions);
        instructions.add((AbstractInsnNode)new InsnNode(172));
        return this;
    }
    
    private static void pushInt(final int value, final InsnList instructions) {
        switch (value) {
            case -1: {
                instructions.add((AbstractInsnNode)new InsnNode(2));
                break;
            }
            case 0: {
                instructions.add((AbstractInsnNode)new InsnNode(3));
                break;
            }
            case 1: {
                instructions.add((AbstractInsnNode)new InsnNode(4));
                break;
            }
            case 2: {
                instructions.add((AbstractInsnNode)new InsnNode(5));
                break;
            }
            case 3: {
                instructions.add((AbstractInsnNode)new InsnNode(6));
                break;
            }
            case 4: {
                instructions.add((AbstractInsnNode)new InsnNode(7));
                break;
            }
            case 5: {
                instructions.add((AbstractInsnNode)new InsnNode(8));
                break;
            }
            default: {
                if (value >= -128 && value <= 127) {
                    instructions.add((AbstractInsnNode)new IntInsnNode(16, value));
                    break;
                }
                if (value >= -32768 && value <= 32767) {
                    instructions.add((AbstractInsnNode)new IntInsnNode(17, value));
                    break;
                }
                instructions.add((AbstractInsnNode)new LdcInsnNode((Object)value));
                break;
            }
        }
    }
    
    public ImagineMethod push(final int value) {
        pushInt(value, this.mMethod.instructions);
        return this;
    }
    
    public ImagineMethod constMethod(final boolean value) {
        return this.constMethod(value ? 1 : 0);
    }
    
    public ImagineMethod constMethod(final double value) {
        final InsnList instructions = this.mMethod.instructions;
        instructions.clear();
        pushDouble(value, instructions);
        instructions.add((AbstractInsnNode)new InsnNode(175));
        return this;
    }
    
    private static void pushDouble(final double value, final InsnList instructions) {
        if (value == 0.0) {
            instructions.add((AbstractInsnNode)new InsnNode(14));
        }
        else if (value == 1.0) {
            instructions.add((AbstractInsnNode)new InsnNode(15));
        }
        else {
            instructions.add((AbstractInsnNode)new LdcInsnNode((Object)value));
        }
    }
    
    public ImagineMethod push(final double value) {
        pushDouble(value, this.mMethod.instructions);
        return this;
    }
    
    public ImagineMethod constMethod(final float value) {
        final InsnList instructions = this.mMethod.instructions;
        instructions.clear();
        pushFloat(value, instructions);
        instructions.add((AbstractInsnNode)new InsnNode(174));
        return this;
    }
    
    public ImagineMethod push(final float value) {
        pushFloat(value, this.mMethod.instructions);
        return this;
    }
    
    private static void pushFloat(final float value, final InsnList instructions) {
        if (value == 0.0f) {
            instructions.add((AbstractInsnNode)new InsnNode(11));
        }
        else if (value == 1.0f) {
            instructions.add((AbstractInsnNode)new InsnNode(12));
        }
        else if (value == 2.0f) {
            instructions.add((AbstractInsnNode)new InsnNode(13));
        }
        else {
            instructions.add((AbstractInsnNode)new LdcInsnNode((Object)value));
        }
    }
    
    public static InsnList methodCall(final ImagineMethod img, String owner, final String methodName, boolean staticForward, final boolean needReturn) {
        if (!staticForward || owner == null) {
            owner = img.mAsm.getActualName();
        }
        owner = ImagineASM.toDesc(owner);
        final ImagineDesc desc = ImagineDesc.parse(img.mMethod.desc);
        if (!needReturn) {
            desc.returnType(ImagineDesc.SubDesc.create(Type.VOID_TYPE, 0));
        }
        final InsnList instructions = new InsnList();
        if (!img.isStatic()) {
            staticForward = true;
            instructions.add((AbstractInsnNode)new IntInsnNode(25, 0));
        }
        int i = img.isStatic() ? 0 : 1;
        for (final ImagineDesc.SubDesc sub : desc.parameters()) {
            instructions.add((AbstractInsnNode)new IntInsnNode(sub.opcodeLoad(), i++));
        }
        if (staticForward && !img.isStatic()) {
            desc.first(Type.getType("L" + ImagineASM.toDesc(img.mAsm.getActualName()) + ";"), 0);
        }
        final MethodDesc method = img.mAsm.mapMethod(owner, methodName, desc.toString());
        instructions.add((AbstractInsnNode)new MethodInsnNode(staticForward ? 184 : 185, (String)((Pair<String, Y>)method).first(), (String)((Pair<X, String>)method).second(), (String)((Triple<X, Y, String>)method).third(), false));
        if (needReturn) {
            instructions.add((AbstractInsnNode)new InsnNode(desc.returnOpcode()));
        }
        return instructions;
    }
    
    private static void forward(final ImagineMethod img, final String owner, final String methodName, final boolean staticForward) {
        final InsnList list = img.mMethod.instructions;
        list.clear();
        list.add(methodCall(img, owner, methodName, staticForward, true));
    }
    
    public ImagineMethod forward(final String owner, final String methodName) {
        forward(this, owner, methodName, owner != null);
        return this;
    }
    
    public ImagineMethod forward(final String methodName) {
        return this.forward(null, methodName);
    }
    
    public ImagineMethod callFirst(final String owner, final String methodName) {
        final InsnList instructions = this.mMethod.instructions;
        instructions.insertBefore(instructions.getFirst(), methodCall(this, owner, methodName, owner != null, false));
        return this;
    }
    
    public ImagineMethod callFirst(final String methodName) {
        return this.callFirst(null, methodName);
    }
    
    public ImagineMethod callLast(final String owner, final String methodName) {
        final InsnList instructions = this.mMethod.instructions;
        final InsnList methodCall = methodCall(this, owner, methodName, owner != null, false);
        for (final AbstractInsnNode node : instructions) {
            switch (node.getOpcode()) {
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177: {
                    instructions.insertBefore(node, methodCall);
                    continue;
                }
            }
        }
        return this;
    }
    
    public ImagineMethod callLast(final String methodName) {
        return this.callLast(null, methodName);
    }
    
    public InsnList instructions() {
        return this.mMethod.instructions;
    }
    
    public String getName() {
        return this.mAsm.getActualName();
    }
    
    public Iterable<ImagineMethodPosition> find(final InsnList patternRaw) {
        final List<AbstractInsnNode> pattern = ImagineASM.asList(patternRaw);
        return new LazyIterable<ImagineMethodPosition>(new LazyIterable.LazyAction<ImagineMethodPosition>() {
            private int mPosition = 0;
            private CuttableList<AbstractInsnNode> mInstructions = new CuttableList<AbstractInsnNode>(ImagineASM.asList(ImagineMethod.this.mMethod.instructions));
            
            @Override
            public ImagineMethodPosition acquire() {
                final int index = Collections.indexOfSubList(this.mInstructions, pattern);
                if (index >= 0) {
                    this.mPosition += index;
                    for (int i = 0; i < index; ++i) {}
                }
                final Iterator<AbstractInsnNode> patternIterator = pattern.iterator();
                return null;
            }
        });
    }
    
    public ImagineMethod find(final InsnList pattern, final Action<ImagineMethodPosition> action) {
        for (final ImagineMethodPosition position : this.find(pattern)) {
            action.action(position);
        }
        return this;
    }
    
    public boolean isStatic() {
        return (this.mMethod.access & 0x8) != 0x0;
    }
    
    @Override
    public ImagineMethod addAccess(final int modifiers) {
        final MethodNode mMethod = this.mMethod;
        mMethod.access |= modifiers;
        return this;
    }
    
    @Override
    public ImagineMethod limitAccess(final int modifiers) {
        final MethodNode mMethod = this.mMethod;
        mMethod.access &= modifiers;
        return this;
    }
}
