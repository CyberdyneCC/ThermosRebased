// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.api.Triple;
import java.util.Iterator;
import pw.prok.imagine.api.Pair;

public enum Mapping
{
    OBF {
        @Override
        public String clazz(final String name) {
            for (final Pair<String, String> mapping : ImagineRemapper.clazz()) {
                if (name.equals(mapping.first()) || name.equals(mapping.second())) {
                    return mapping.first();
                }
            }
            return name;
        }
        
        @Override
        public FieldDesc field(final String owner, final String name) {
            final String obfOwner = Mapping$1.OBF.clazz(owner);
            final String srgOwner = Mapping$1.SRG.clazz(owner);
            for (final Triple<FieldDesc, FieldDesc, FieldDesc> mapping : ImagineRemapper.fields()) {
                if (mapping.first().equals(obfOwner, name) || mapping.second().equals(srgOwner, name) || mapping.third().equals(srgOwner, name)) {
                    return mapping.first();
                }
            }
            return new FieldDesc(obfOwner, name);
        }
        
        @Override
        public MethodDesc method(final String owner, final String name, final String desc) {
            final String obfOwner = Mapping$1.OBF.clazz(owner);
            final String obfDesc = ImagineRemapper.descObf(desc);
            final String srgOwner = Mapping$1.SRG.clazz(owner);
            final String srgDesc = ImagineRemapper.descSrg(desc);
            for (final Triple<MethodDesc, MethodDesc, MethodDesc> mapping : ImagineRemapper.methods()) {
                if (mapping.first().equals(obfOwner, name, obfDesc) || mapping.second().equals(srgOwner, name, srgDesc) || mapping.third().equals(srgOwner, name, srgDesc)) {
                    return mapping.first();
                }
            }
            return new MethodDesc(obfOwner, name, obfDesc);
        }
    }, 
    SRG {
        @Override
        public String clazz(final String name) {
            for (final Pair<String, String> mapping : ImagineRemapper.clazz()) {
                if (name.equals(mapping.first()) || name.equals(mapping.second())) {
                    return mapping.second();
                }
            }
            return name;
        }
        
        @Override
        public FieldDesc field(final String owner, final String name) {
            final String obfOwner = Mapping$2.OBF.clazz(owner);
            final String srgOwner = Mapping$2.SRG.clazz(owner);
            for (final Triple<FieldDesc, FieldDesc, FieldDesc> mapping : ImagineRemapper.fields()) {
                if (mapping.first().equals(obfOwner, name) || mapping.second().equals(srgOwner, name) || mapping.third().equals(srgOwner, name)) {
                    return mapping.second();
                }
            }
            return new FieldDesc(srgOwner, name);
        }
        
        @Override
        public MethodDesc method(final String owner, final String name, final String desc) {
            final String obfOwner = Mapping$2.OBF.clazz(owner);
            final String obfDesc = ImagineRemapper.descObf(desc);
            final String srgOwner = Mapping$2.SRG.clazz(owner);
            final String srgDesc = ImagineRemapper.descSrg(desc);
            for (final Triple<MethodDesc, MethodDesc, MethodDesc> mapping : ImagineRemapper.methods()) {
                if (mapping.first().equals(obfOwner, name, obfDesc) || mapping.second().equals(srgOwner, name, srgDesc) || mapping.third().equals(srgOwner, name, srgDesc)) {
                    return mapping.second();
                }
            }
            return new MethodDesc(srgOwner, name, srgDesc);
        }
    }, 
    DEV {
        @Override
        public String clazz(final String name) {
            return Mapping$3.SRG.clazz(name);
        }
        
        @Override
        public FieldDesc field(final String owner, final String name) {
            final String obfOwner = Mapping$3.OBF.clazz(owner);
            final String srgOwner = Mapping$3.SRG.clazz(owner);
            for (final Triple<FieldDesc, FieldDesc, FieldDesc> mapping : ImagineRemapper.fields()) {
                if (mapping.first().equals(obfOwner, name) || mapping.second().equals(srgOwner, name) || mapping.third().equals(srgOwner, name)) {
                    return mapping.third();
                }
            }
            return new FieldDesc(srgOwner, name);
        }
        
        @Override
        public MethodDesc method(final String owner, final String name, final String desc) {
            final String obfOwner = Mapping$3.OBF.clazz(owner);
            final String obfDesc = ImagineRemapper.descObf(desc);
            final String srgOwner = Mapping$3.SRG.clazz(owner);
            final String srgDesc = ImagineRemapper.descSrg(desc);
            for (final Triple<MethodDesc, MethodDesc, MethodDesc> mapping : ImagineRemapper.methods()) {
                if (mapping.first().equals(obfOwner, name, obfDesc) || mapping.second().equals(srgOwner, name, srgDesc) || mapping.third().equals(srgOwner, name, srgDesc)) {
                    return mapping.third();
                }
            }
            return new MethodDesc(srgOwner, name, srgDesc);
        }
    };
    
    public abstract String clazz(final String p0);
    
    public abstract FieldDesc field(final String p0, final String p1);
    
    public abstract MethodDesc method(final String p0, final String p1, final String p2);
}
