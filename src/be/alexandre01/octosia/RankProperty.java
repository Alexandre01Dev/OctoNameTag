package be.alexandre01.octosia;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class RankProperty {
    private static HashMap<String, RankProperty> index = new HashMap<>();

    public static RankProperty getRankProperty(String name) {
        return index.get(name);
    }

    Scoreboard score;
    Team t;
    String prefix;
    String  id;
    String name;
    String perms;
    public RankProperty(String name,String prefix,String perms, String id){
        this.name = name;
        this.prefix = prefix.replaceAll("&","§");
        this.id = id;
        this.perms = perms;
        index.put(name,this);
        setupRank();
    }

    private void setupRank(){
        score = Bukkit.getScoreboardManager().getMainScoreboard();
        t = score.getTeam(id+name);

        if(t == null){
            t = score.registerNewTeam(id+name);
        }

        t.setPrefix(prefix);
        for(Player players : Bukkit.getOnlinePlayers()){

            players.setScoreboard(score);
        }
    }
    public void addPlayer(Player player){
        t.addPlayer(player);
        Nametag.getPlugin().getRankPlayer().put(player,this);
    }
    public void removePlayer(Player player){
        t.removePlayer(player);
        Nametag.getPlugin().getRankPlayer().remove(player);
    }
}
