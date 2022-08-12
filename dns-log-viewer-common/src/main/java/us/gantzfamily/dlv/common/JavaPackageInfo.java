package us.gantzfamily.dlv.common;

public class JavaPackageInfo {

    private final Class<?> targetClass;
    private final Package packageData;
    private final String title;
    private final String version;
    private final String vendor;
    
    public JavaPackageInfo(final Class<?> targetClass) {
        this.targetClass = targetClass;
        packageData = targetClass.getPackage();
        title = packageData.getImplementationTitle();
        version = packageData.getImplementationVersion();
        vendor = packageData.getImplementationVendor();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
    
    public Package getPackageData() {
        return packageData;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getVendor() {
        return vendor;
    }
    
}
