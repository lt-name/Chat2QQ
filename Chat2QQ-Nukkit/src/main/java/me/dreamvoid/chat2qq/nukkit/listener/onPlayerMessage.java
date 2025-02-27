package me.dreamvoid.chat2qq.nukkit.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.scheduler.AsyncTask;
import me.dreamvoid.chat2qq.nukkit.NukkitPlugin;
import me.dreamvoid.miraimc.api.MiraiBot;

public class onPlayerMessage implements Listener {
    private final NukkitPlugin plugin;
    public onPlayerMessage(NukkitPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerChatEvent e){
        if(!(plugin.getConfig().getBoolean("general.require-command-to-chat",false))){
            boolean allowPrefix = false;
            String formatText = plugin.getConfig().getString("bot.group-chat-format")
                    .replace("%player%",e.getPlayer().getName())
                    .replace("%message%",e.getMessage());

            // 判断消息是否带前缀
            if(plugin.getConfig().getBoolean("general.requite-special-word-prefix.enabled",false)){
                for(String prefix : plugin.getConfig().getStringList("general.requite-special-word-prefix.prefix")){
                    if(e.getMessage().startsWith(prefix)){
                        allowPrefix = true;
                        formatText = formatText.replace(prefix,"");
                        break;
                    }
                }
            } else allowPrefix = true;

            if(allowPrefix){
                String finalFormatText = formatText;
                plugin.getServer().getScheduler().scheduleAsyncTask(plugin, new AsyncTask() {
                    @Override
                    public void onRun() {
                        MiraiBot.getBot(plugin.getConfig().getLong("bot.botaccount")).getGroup(plugin.getConfig().getLong("bot.groupid")).sendMessageMirai(finalFormatText);
                    }
                });
            }
        }
    }
}
