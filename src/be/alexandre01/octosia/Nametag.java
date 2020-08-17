package be.alexandre01.octosia;

import be.alexandre01.octosia.configs.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Nametag extends JavaPlugin {
    private static Nametag plugin;
    YamlUtils temp = new YamlUtils(this,"temp.yml");
    YamlUtils conf = new YamlUtils(this,"config.yml");
    YamlUtils data = new YamlUtils(this,"data.yml");
    private ArrayList<RankProperty> ranks = new ArrayList<>();
    private RankProperty defaultRank;
    public HashMap<Player, RankProperty> getRankPlayer() {
        return rankPlayer;
    }

    private HashMap<String, RankProperty> rankIndex = new HashMap<>();
    private HashMap<Player, RankProperty> rankPlayer = new HashMap<>();
    private HashMap<String,RankProperty> forcedRank = new HashMap<>();

    public ArrayList<RankProperty> getRanks() {
        return ranks;
    }

    public HashMap<String, RankProperty> getRankIndex() {
        return rankIndex;
    }

    public HashMap<String, RankProperty> getForcedRanks() {
        return forcedRank;
    }

    @Override
    public void onEnable(){
        plugin = this;
        registerCommand("octonametag", new NameTagCommand());
        RankUpdater rankUpdater = new RankUpdater();
        if(temp.getConfig().contains("temps")){
            for(String string : temp.getConfig().getStringList("temps")){
                if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(string)!= null){
                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam(string).unregister();
                }
            }
        }

        for(String string : conf.getConfig().getConfigurationSection("ranks").getKeys(false)){
            System.out.println("WOW");
            if(!string.equalsIgnoreCase("Default")){
                String id = conf.getConfig().getString("ranks."+string+".id");
                RankProperty rankProperty = new RankProperty(string,conf.getConfig().getString("ranks."+string+".prefix"),conf.getConfig().getString("ranks."+string+".perms"),id);
                rankIndex.put(conf.getConfig().getString("ranks."+string+".perms"),rankProperty);
                ranks.add(rankProperty);



            }else {
                String id = conf.getConfig().getString("ranks."+string+".id");
                RankProperty rankProperty = new RankProperty(string,conf.getConfig().getString("ranks."+string+".prefix"),conf.getConfig().getString("ranks."+string+".perms"),id);
                defaultRank = rankProperty;
            }
        }



        if (data.getConfig().contains("data")) {
            for(String forcedPlayer : data.getConfig().getConfigurationSection("data").getKeys(false)){

                String forcedRank = data.getConfig().getString("data."+forcedPlayer);
                System.out.println(forcedPlayer +"/ "+forcedRank);
                this.forcedRank.put(forcedPlayer,RankProperty.getRankProperty(forcedRank));
            }
        }

        ArrayList<String> tempsList = new ArrayList<>();
        for (RankProperty rank : ranks){
            tempsList.add(rank.id+rank.name);
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            RankProperty rankProperty = rankUpdater.getRank(player);
            Nametag.getPlugin().getRankPlayer().put(player, rankProperty);
            rankProperty.addPlayer(player);
            System.out.println(rankProperty.name);
        }


        temp.getConfig().set("temps",tempsList);
        temp.save();

        getServer().getPluginManager().registerEvents(rankUpdater,this);
    }

    public static Nametag getPlugin() {
        return plugin;
    }
    public void registerCommand(String commandName, Command commandClass){
        try{
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(commandName, commandClass);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public RankProperty getDefaultRank(){
        return defaultRank;
    }

    public void addForcedPlayerRankToData(String player, String name){
    data.getConfig().set("data."+player,name);
    data.save();
    this.forcedRank.put(player,RankProperty.getRankProperty(name));
    }
    public void rmvForcedPlayerRankToData(String player){
        data.getConfig().set("data."+player,null);
        data.save();
        this.forcedRank.remove(player);
    }
}
