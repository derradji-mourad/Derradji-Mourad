package AppTracker;
//le Adaptateur pour avoir le nom,le package et le temps d'utilisation de l'application.
public class AllAppInfo {
    private String appName;
    private String packageName;
    private int timeSpent;


    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public AllAppInfo(String appName, String packageName, int timeSpent) {
        this.appName = appName;
        this.packageName = packageName;
        this.timeSpent = timeSpent;
    }

}
