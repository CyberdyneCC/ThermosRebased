// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.config;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ConfigReader
{
    private static final Charset UTF_8;
    
    public static Map<String, String> read(final InputStream is) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, ConfigReader.UTF_8));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static {
        UTF_8 = Charset.forName("utf-8");
    }
}
