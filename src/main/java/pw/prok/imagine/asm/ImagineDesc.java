// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import java.util.Collection;
import pw.prok.imagine.util.Array;
import java.util.LinkedList;
import java.util.Iterator;
import org.objectweb.asm.Type;
import com.google.common.collect.Lists;
import java.util.List;

public class ImagineDesc
{
    private SubDesc mReturnType;
    private List<SubDesc> mParameterTypes;
    
    public ImagineDesc(final SubDesc returnType, final SubDesc... parameterTypes) {
        assert returnType != null;
        assert parameterTypes != null;
        this.mReturnType = returnType;
        this.mParameterTypes = (List<SubDesc>)Lists.newArrayList((Object[])parameterTypes);
    }
    
    public SubDesc returnType() {
        return this.mReturnType;
    }
    
    public ImagineDesc returnType(final SubDesc returnType) {
        this.mReturnType = returnType;
        return this;
    }
    
    public List<SubDesc> parameters() {
        return this.mParameterTypes;
    }
    
    public SubDesc parameter(final int i) {
        return this.mParameterTypes.get(i);
    }
    
    public ImagineDesc insert(final SubDesc sub, final int pos) {
        this.mParameterTypes.add(pos, sub);
        return this;
    }
    
    public ImagineDesc insert(final Type type, final int level, final int pos) {
        return this.insert(SubDesc.create(type, level), pos);
    }
    
    public ImagineDesc first(final SubDesc sub) {
        return this.insert(sub, 0);
    }
    
    public ImagineDesc first(final Type type, final int level) {
        return this.first(SubDesc.create(type, level));
    }
    
    public ImagineDesc last(final SubDesc sub) {
        this.mParameterTypes.add(sub);
        return this;
    }
    
    public ImagineDesc last(final Type type, final int level) {
        return this.last(SubDesc.create(type, level));
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (final SubDesc sub : this.mParameterTypes) {
            builder.append(sub.toString());
        }
        builder.append(')');
        builder.append(this.mReturnType.toString());
        return builder.toString();
    }
    
    public int returnOpcode() {
        return this.mReturnType.opcodeReturn();
    }
    
    public static ImagineDesc parse(final String desc) {
        boolean returnTypeFlag = false;
        SubDesc returnType = null;
        final List<SubDesc> parameterTypes = new LinkedList<SubDesc>();
        int level = 0;
        for (int i = 0; i < desc.length(); ++i) {
            final char c = desc.charAt(i);
            if (c != '(') {
                if (c == ')') {
                    returnTypeFlag = true;
                }
                else if (c == '[') {
                    ++level;
                }
                else {
                    String name;
                    if (c == 'L') {
                        final int end = desc.indexOf(59, i + 1);
                        name = desc.substring(i, end + 1);
                        i = end;
                    }
                    else {
                        name = new String(new char[] { c });
                    }
                    final Type type = Type.getType(name);
                    if (returnTypeFlag) {
                        returnType = new SubDesc(type, level);
                    }
                    else {
                        parameterTypes.add(new SubDesc(type, level));
                    }
                    level = 0;
                }
            }
        }
        return new ImagineDesc(returnType, (SubDesc[])Array.asArray(parameterTypes, SubDesc.class));
    }
    
    public static class SubDesc
    {
        public final Type type;
        public final int level;
        
        public static SubDesc create(final Type type, final int level) {
            assert level >= 0;
            assert type != null;
            return new SubDesc(type, level);
        }
        
        private SubDesc(final Type type, final int level) {
            this.type = type;
            this.level = level;
        }
        
        public int opcodeLoad() {
            if (this.level >= 1) {
                return 25;
            }
            return this.type.getOpcode(21);
        }
        
        public int opcodeReturn() {
            if (this.level >= 1) {
                return 176;
            }
            return this.type.getOpcode(172);
        }
        
        @Override
        public String toString() {
            final String desc = this.type.getDescriptor();
            if (this.level == 0) {
                return desc;
            }
            final StringBuilder b = new StringBuilder(desc.length() + this.level);
            for (int i = 0; i < this.level; ++i) {
                b.append('[');
            }
            b.append(desc);
            return b.toString();
        }
    }
}
