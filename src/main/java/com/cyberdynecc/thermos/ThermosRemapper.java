package com.cyberdynecc.thermos;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

/**
 * Created by robotia on 10/15/16.
 */
public class ThermosRemapper extends JarRemapper {

    public ThermosRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Throwable t) {
            return signature;
        }
    }
}
