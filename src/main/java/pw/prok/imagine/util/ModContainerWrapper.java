// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.util;

import java.util.Map;
import java.security.cert.Certificate;
import cpw.mods.fml.common.versioning.VersionRange;
import cpw.mods.fml.common.LoadController;
import com.google.common.eventbus.EventBus;
import java.util.List;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import java.util.Set;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModMetadata;
import java.io.File;
import cpw.mods.fml.common.ModContainer;

public class ModContainerWrapper<T extends ModContainer> implements ModContainer
{
    protected T mContainer;
    
    public ModContainerWrapper(final T container) {
        this.mContainer = container;
    }
    
    protected ModContainerWrapper() {
    }
    
    public String getModId() {
        return this.mContainer.getModId();
    }
    
    public String getName() {
        return this.mContainer.getName();
    }
    
    public String getVersion() {
        return this.mContainer.getVersion();
    }
    
    public File getSource() {
        return this.mContainer.getSource();
    }
    
    public ModMetadata getMetadata() {
        return this.mContainer.getMetadata();
    }
    
    public void bindMetadata(final MetadataCollection metadataCollection) {
        this.mContainer.bindMetadata(metadataCollection);
    }
    
    public void setEnabledState(final boolean enabled) {
        this.mContainer.setEnabledState(enabled);
    }
    
    public Set<ArtifactVersion> getRequirements() {
        return (Set<ArtifactVersion>)this.mContainer.getRequirements();
    }
    
    public List<ArtifactVersion> getDependencies() {
        return (List<ArtifactVersion>)this.mContainer.getDependencies();
    }
    
    public List<ArtifactVersion> getDependants() {
        return (List<ArtifactVersion>)this.mContainer.getDependants();
    }
    
    public String getSortingRules() {
        return this.mContainer.getSortingRules();
    }
    
    public boolean registerBus(final EventBus eventBus, final LoadController loadController) {
        return this.mContainer.registerBus(eventBus, loadController);
    }
    
    public boolean matches(final Object o) {
        return this.mContainer.matches(o);
    }
    
    public Object getMod() {
        return this.mContainer.getMod();
    }
    
    public ArtifactVersion getProcessedVersion() {
        return this.mContainer.getProcessedVersion();
    }
    
    public boolean isImmutable() {
        return this.mContainer.isImmutable();
    }
    
    public String getDisplayVersion() {
        return this.mContainer.getDisplayVersion();
    }
    
    public VersionRange acceptableMinecraftVersionRange() {
        return this.mContainer.acceptableMinecraftVersionRange();
    }
    
    public Certificate getSigningCertificate() {
        return this.mContainer.getSigningCertificate();
    }
    
    public Map<String, String> getCustomModProperties() {
        return (Map<String, String>)this.mContainer.getCustomModProperties();
    }
    
    public Class<?> getCustomResourcePackClass() {
        return (Class<?>)this.mContainer.getCustomResourcePackClass();
    }
    
    public Map<String, String> getSharedModDescriptor() {
        return (Map<String, String>)this.mContainer.getSharedModDescriptor();
    }
    
    public ModContainer.Disableable canBeDisabled() {
        return this.mContainer.canBeDisabled();
    }
    
    public String getGuiClassName() {
        return this.mContainer.getGuiClassName();
    }
    
    public List<String> getOwnedPackages() {
        return (List<String>)this.mContainer.getOwnedPackages();
    }
    
    @Override
    public String toString() {
        return this.mContainer.toString();
    }
}
