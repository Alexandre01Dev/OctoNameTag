package be.alexandre01.octosia.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class YamlUtils {
    Plugin plugin;
    File confDataFile;
    YamlConfiguration confDataConfig;
    Utf8YamlConfiguration utf8;

    public static Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public YamlUtils(Plugin plugin, String pathName) {
        this.plugin = plugin;
            confDataFile = new File(plugin.getDataFolder(), pathName);
             confDataConfig = YamlConfiguration.loadConfiguration(confDataFile);
             utf8 = new Utf8YamlConfiguration();
             loadYml();
    }
    public FileConfiguration getConfig(){
        return confDataConfig;
    }
    public File getFile(){
        return confDataFile;
    }
    public void save(){
        try {
           confDataConfig.save(confDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadYml() {
        if(!confDataFile.exists()){
            confDataFile.getParentFile().mkdirs();
            plugin.saveResource(confDataFile.getName(), false);
        }
        confDataConfig= new YamlConfiguration();
        try {
            InputStream inputstream = new FileInputStream(confDataFile);

            utf8.load(inputstream);

            confDataConfig = utf8;



        } catch (Exception e) {
            System.out.println("ERROR LOADING FILE");
        }
    }

}
