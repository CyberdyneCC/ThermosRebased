// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import java.util.HashMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import LZMA.LzmaInputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import pw.prok.imagine.api.Triple;
import pw.prok.imagine.api.Pair;
import java.util.Set;
import java.util.Map;
import com.google.common.collect.BiMap;

public class ImagineRemapper
{
    protected static final BiMap<String, String> sRawClass;
    protected static final BiMap<String, String> sRawField;
    protected static final BiMap<String, String> sRawMethod;
    protected static final Map<String, String> sSrgMethods;
    protected static final Map<String, String> sSrgFields;
    private static Set<Pair<String, String>> sClasses;
    private static Set<Triple<MethodDesc, MethodDesc, MethodDesc>> sMethods;
    private static Set<Triple<FieldDesc, FieldDesc, FieldDesc>> sFields;
    private static boolean sMapsDirty;
    
    public static Triple<String, String, String> createMethodDesc(final String s) {
        return Triple.create(cutMethodClass(s), cutMethod(s), cutMethodDesc(s));
    }
    
    public static String descDev(final String desc) {
        return desc(desc, Mapping.DEV);
    }
    
    public static String descObf(final String desc) {
        return desc(desc, Mapping.OBF);
    }
    
    public static String descSrg(final String desc) {
        return desc(desc, Mapping.SRG);
    }
    
    public static String desc(final String desc, final Mapping mapping) {
        final StringBuilder newDesc = new StringBuilder();
        for (int i = 0; i < desc.length(); ++i) {
            final char c = desc.charAt(i);
            if (c == 'L') {
                final int end = desc.indexOf(59, i + 1);
                newDesc.append('L');
                newDesc.append(mapping.clazz(desc.substring(i + 1, end)));
                newDesc.append(';');
                i = end;
            }
            else {
                newDesc.append(c);
            }
        }
        return newDesc.toString();
    }
    
    public static String clazzDev(final String name) {
        return clazz(name, Mapping.DEV);
    }
    
    public static String clazzObf(final String name) {
        return clazz(name, Mapping.OBF);
    }
    
    public static String clazzSrg(final String name) {
        return clazz(name, Mapping.SRG);
    }
    
    public static String clazz(final String name, final Mapping mapping) {
        return mapping.clazz(name);
    }
    
    private static String methodSrgToDev(final String srg) {
        final String dev = ImagineRemapper.sSrgMethods.get(srg);
        return (dev != null) ? dev : srg;
    }
    
    public static MethodDesc methodDev(final String owner, final String name, final String desc) {
        return method(owner, name, desc, Mapping.DEV);
    }
    
    public static MethodDesc methodObf(final String owner, final String name, final String desc) {
        return method(owner, name, desc, Mapping.OBF);
    }
    
    public static MethodDesc methodSrg(final String owner, final String name, final String desc) {
        return method(owner, name, desc, Mapping.SRG);
    }
    
    public static MethodDesc method(final String owner, final String name, final String desc, final Mapping mapping) {
        return mapping.method(owner, name, desc);
    }
    
    public static String cutMethod(final String fullDesc) {
        final int c = fullDesc.indexOf(40);
        if (c > 0) {
            final int n = fullDesc.lastIndexOf(47, c);
            return fullDesc.substring((n >= 0) ? (n + 1) : 0, c);
        }
        return fullDesc;
    }
    
    public static String cutMethodClass(final String fullDesc) {
        final int c = fullDesc.indexOf(40);
        if (c > 0) {
            final int n = fullDesc.lastIndexOf(47, c);
            if (n > 0) {
                return fullDesc.substring(0, n);
            }
        }
        return fullDesc;
    }
    
    public static String cutDesc(final String fullDesc) {
        final int c = fullDesc.indexOf(40);
        if (c > 0) {
            return fullDesc.substring(c);
        }
        return fullDesc;
    }
    
    public static String cutMethodDesc(final String fullDesc) {
        final int c = fullDesc.indexOf(40);
        if (c > 0) {
            return fullDesc.substring(c);
        }
        return fullDesc;
    }
    
    public static String cutField(final String fullDesc) {
        final int c = fullDesc.lastIndexOf(47);
        if (c > 0) {
            return fullDesc.substring(c + 1);
        }
        return fullDesc;
    }
    
    public static String cutFieldClass(final String fullDesc) {
        final int c = fullDesc.lastIndexOf(47);
        if (c > 0) {
            return fullDesc.substring(0, c);
        }
        return fullDesc;
    }
    
    private static String fieldSrgToDev(final String srg) {
        final String dev = ImagineRemapper.sSrgFields.get(srg);
        return (dev != null) ? dev : srg;
    }
    
    public static FieldDesc fieldDev(final String owner, final String name) {
        return field(owner, name, Mapping.DEV);
    }
    
    public static FieldDesc fieldObf(final String owner, final String name) {
        return field(owner, name, Mapping.OBF);
    }
    
    public static FieldDesc fieldSrg(final String owner, final String name) {
        return field(owner, name, Mapping.SRG);
    }
    
    public static FieldDesc field(final String owner, final String name, final Mapping mapping) {
        return mapping.field(owner, name);
    }
    
    public static void mergeDeobfuscationData(final InputStream is, final MappingType type) throws Exception {
        Map<String, String> srgData = null;
        switch (type) {
            case Method: {
                srgData = ImagineRemapper.sSrgMethods;
                markDirty();
                break;
            }
            case Field: {
                srgData = ImagineRemapper.sSrgFields;
                markDirty();
                break;
            }
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (type == MappingType.Srg) {
            mergeSrgData(reader);
            return;
        }
        boolean firstLine = true;
        String line;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
            }
            else {
                final int c1 = line.indexOf(44);
                if (c1 < 0) {
                    continue;
                }
                final int c2 = line.indexOf(44, c1 + 1);
                if (c2 < 0) {
                    continue;
                }
                final String srgName = line.substring(0, c1);
                final String devName = line.substring(c1 + 1, c2);
                srgData.put(srgName, devName);
            }
        }
        reader.close();
        is.close();
    }
    
    private static void markDirty() {
        ImagineRemapper.sMapsDirty = true;
    }
    
    private static void mergeSrgData(final BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            final String[] parts = line.split("\\s+");
            final String s = parts[0];
            switch (s) {
                case "CL:": {
                    ImagineRemapper.sRawClass.put((Object)parts[1], (Object)parts[2]);
                    continue;
                }
                case "FD:": {
                    ImagineRemapper.sRawField.put((Object)parts[1], (Object)parts[2]);
                    continue;
                }
                case "MD:": {
                    ImagineRemapper.sRawMethod.put((Object)(parts[1] + parts[2]), (Object)(parts[3] + parts[4]));
                    continue;
                }
            }
        }
    }
    
    public static void setupDeobfuscationData(final String srg, final String methodData, final String fieldData) {
        try {
            mergeDeobfuscationData((InputStream)new LzmaInputStream(ImagineRemapper.class.getResourceAsStream(srg)), MappingType.Srg);
            mergeDeobfuscationData((InputStream)new LzmaInputStream(ImagineRemapper.class.getResourceAsStream(methodData)), MappingType.Method);
            mergeDeobfuscationData((InputStream)new LzmaInputStream(ImagineRemapper.class.getResourceAsStream(fieldData)), MappingType.Field);
        }
        catch (Exception e) {
            throw new IllegalStateException("Couldn't load deobfuscation data", e);
        }
    }
    
    private static void ensureMappings() {
        if (!ImagineRemapper.sMapsDirty) {
            return;
        }
        final Set<Pair<String, String>> classes = new HashSet<Pair<String, String>>();
        for (final Map.Entry<String, String> clazz : ImagineRemapper.sRawClass.entrySet()) {
            final String obf = clazz.getKey();
            final String srg = clazz.getValue();
            classes.add(new Pair<String, String>(obf, srg));
        }
        ImagineRemapper.sClasses = (Set<Pair<String, String>>)ImmutableSet.copyOf((Collection)classes);
        final Set<Triple<MethodDesc, MethodDesc, MethodDesc>> methods = new HashSet<Triple<MethodDesc, MethodDesc, MethodDesc>>();
        for (final Map.Entry<String, String> method : ImagineRemapper.sRawMethod.entrySet()) {
            final MethodDesc obf2 = new MethodDesc(method.getKey());
            final MethodDesc srg2 = new MethodDesc(method.getValue());
            final MethodDesc dev = new MethodDesc(((Pair<String, Y>)srg2).first(), methodSrgToDev(((Pair<X, String>)srg2).second()), ((Triple<X, Y, String>)srg2).third());
            methods.add(new Triple<MethodDesc, MethodDesc, MethodDesc>(obf2, srg2, dev));
        }
        ImagineRemapper.sMethods = (Set<Triple<MethodDesc, MethodDesc, MethodDesc>>)ImmutableSet.copyOf((Collection)methods);
        final Set<Triple<FieldDesc, FieldDesc, FieldDesc>> fields = new HashSet<Triple<FieldDesc, FieldDesc, FieldDesc>>();
        for (final Map.Entry<String, String> field : ImagineRemapper.sRawField.entrySet()) {
            final FieldDesc obf3 = new FieldDesc(field.getKey());
            final FieldDesc srg3 = new FieldDesc(field.getValue());
            final FieldDesc dev2 = new FieldDesc(((Pair<String, Y>)srg3).first(), fieldSrgToDev(((Pair<X, String>)srg3).second()));
            fields.add(new Triple<FieldDesc, FieldDesc, FieldDesc>(obf3, srg3, dev2));
        }
        ImagineRemapper.sFields = (Set<Triple<FieldDesc, FieldDesc, FieldDesc>>)ImmutableSet.copyOf((Collection)fields);
        ImagineRemapper.sMapsDirty = false;
    }
    
    public static Set<Pair<String, String>> clazz() {
        ensureMappings();
        return ImagineRemapper.sClasses;
    }
    
    public static Set<Triple<MethodDesc, MethodDesc, MethodDesc>> methods() {
        ensureMappings();
        return ImagineRemapper.sMethods;
    }
    
    public static Set<Triple<FieldDesc, FieldDesc, FieldDesc>> fields() {
        ensureMappings();
        return ImagineRemapper.sFields;
    }
    
    static {
        sRawClass = (BiMap)HashBiMap.create();
        sRawField = (BiMap)HashBiMap.create();
        sRawMethod = (BiMap)HashBiMap.create();
        sSrgMethods = new HashMap<String, String>();
        sSrgFields = new HashMap<String, String>();
        ImagineRemapper.sMapsDirty = true;
    }
    
    public enum MappingType
    {
        Srg, 
        Method, 
        Field;
    }
}
