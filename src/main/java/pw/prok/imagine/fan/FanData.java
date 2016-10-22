// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

public class FanData implements Fan
{
    private String mName;
    private String mId;
    private String mVersion;
    private boolean mServerRequired;
    private boolean mClientRequired;
    
    public FanData() {
    }
    
    public FanData(final Fan fan) {
        this.mName = fan.name();
        this.mId = fan.id();
        this.mVersion = fan.version();
        this.mServerRequired = fan.serverRequired();
        this.mClientRequired = fan.clientRequired();
    }
    
    @Override
    public String name() {
        return this.mName;
    }
    
    public FanData name(final String name) {
        this.mName = name;
        return this;
    }
    
    @Override
    public String id() {
        return this.mId;
    }
    
    public FanData id(final String id) {
        this.mId = id;
        return this;
    }
    
    @Override
    public String version() {
        return this.mVersion;
    }
    
    public FanData version(final String version) {
        this.mVersion = version;
        return this;
    }
    
    @Override
    public boolean serverRequired() {
        return this.mServerRequired;
    }
    
    public FanData serverRequired(final boolean serverRequired) {
        this.mServerRequired = serverRequired;
        return this;
    }
    
    @Override
    public boolean clientRequired() {
        return this.mClientRequired;
    }
    
    public FanData clientRequired(final boolean clientRequired) {
        this.mClientRequired = clientRequired;
        return this;
    }
    
    @Override
    public Class<? extends Fan> annotationType() {
        return Fan.class;
    }
}
