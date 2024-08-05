package ku.cs.services;

import ku.cs.models.Setting;

public class DataApplication {
    private Setting setting;


    DataApplication() {
        setting = new Setting();
    }

    public Setting getSetting() {
        return setting;
    }

    public void saveSetting() {
        
    }

}
