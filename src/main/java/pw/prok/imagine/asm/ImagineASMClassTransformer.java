// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.pool.Pools;
import pw.prok.imagine.util.Array;
import pw.prok.imagine.pool.Pool;
import net.minecraft.launchwrapper.IClassTransformer;

public class ImagineASMClassTransformer implements IClassTransformer
{
    private static Transformer[] sTransformers;
    private static Pool<ImagineASM> sAsmPool;
    
    public static void addTransformer(final Transformer transformer) {
        ImagineASMClassTransformer.sTransformers = Array.appendToArray(ImagineASMClassTransformer.sTransformers, transformer);
    }
    
    public byte[] transform(final String s, final String s1, byte[] bytes) {
        final ImagineASM asm = ImagineASMClassTransformer.sAsmPool.obtain();
        asm.loadClass(s, s1, bytes);
        for (final Transformer transformer : ImagineASMClassTransformer.sTransformers) {
            transformer.transform(asm);
        }
        bytes = asm.build();
        asm.clear();
        ImagineASMClassTransformer.sAsmPool.release(asm);
        return bytes;
    }
    
    static {
        ImagineASMClassTransformer.sTransformers = new Transformer[0];
        ImagineASMClassTransformer.sAsmPool = Pools.create(ImagineASM.class, new Object[0]);
    }
}
