package be.alexandre01.octosia;



import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.naming.Name;

public class RankUpdater implements Listener {
    public RankUpdater(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    RankProperty rankProperty = getRank(player);
                    if(Nametag.getPlugin().getRankPlayer().containsKey(player)){
                        System.out.println(true);
                        if(!Nametag.getPlugin().getRankPlayer().get(player).equals(rankProperty)){
                            Nametag.getPlugin().getRankPlayer().get(player).removePlayer(player);
                            rankProperty.addPlayer(player);
                        }
                    }
                    }

            }
        }.runTaskTimerAsynchronously(Nametag.getPlugin(),20*30,20*60);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        RankProperty rankProperty = getRank(player);
        Nametag.getPlugin().getRankPlayer().put(player, rankProperty);
        rankProperty.addPlayer(player);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        RankProperty rankProperty = getRank(player);
        Nametag.getPlugin().getRankPlayer().remove(player);
        rankProperty.removePlayer(player);
    }


    public RankProperty getRank(Player player){
        if (Nametag.getPlugin().getForcedRanks().containsKey(player.getName())) {
            System.out.println("OK1");
            return Nametag.getPlugin().getForcedRanks().get(player.getName());
        }
        System.out.println("OK2");
        for(RankProperty rank : Nametag.getPlugin().getRanks()){
            if(player.hasPermission(rank.perms)){
                System.out.println("OK3");
                return rank;
            }
        }
        System.out.println("OK4");
        return Nametag.getPlugin().getDefaultRank();
    }
}
