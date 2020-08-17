package be.alexandre01.octosia;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class NameTagCommand extends BukkitCommand {

    protected NameTagCommand() {
        super("octonametag");
        this.description = "OctoNameTagPlugin";
        this.usageMessage  = "/onte";
        this.setPermission("onte.admin");
        this.setAliases(Arrays.asList("onte","octonte"));
    }

    @Override
    public boolean execute(CommandSender sender, String msg, String[] args) {

        if(args.length >= 2){
            if(args.length >= 3){
            if(args[0].equalsIgnoreCase("addUser")){
                    for (RankProperty rankProperty : Nametag.getPlugin().getRanks()){
                        if(rankProperty.name.equalsIgnoreCase(args[2])){
                            Nametag.getPlugin().addForcedPlayerRankToData(args[1],args[2]);
                            Nametag.getPlugin().getForcedRanks().put(args[1],rankProperty);
                            sender.sendMessage("§aLe joueur a été ajouté avec succès.");
                            return false;
                    }
                }
                sender.sendMessage("§aLe grade n'a pas été trouvé");
            }
            }else {
                sender.sendMessage("§e- §9/onte addUser <Player> <Group>");
                sender.sendMessage("§e- §9/onte rmvUser <Player> <Group>");
            }
            if(args[0].equalsIgnoreCase("rmvUser")){
                    Player target = Bukkit.getPlayer(args[1]);
                    Nametag.getPlugin().rmvForcedPlayerRankToData(args[1]);
                    sender.sendMessage("§aLe joueur a été retiré avec succès.");
            }

        }else {
            sender.sendMessage("§e- §9/onte addUser <Player> <Group>");
            sender.sendMessage("§e- §9/onte rmvUser <Player> <Group>");
        }
        Player player = (Player) sender;
        return false;
    }
}
