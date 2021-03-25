package com.freemyip.nopersonalinfo.rpc;

import com.freemyip.nopersonalinfo.dbus.Debug;
import com.freemyip.nopersonalinfo.mpd.Command;
import com.freemyip.nopersonalinfo.mpd.Connection;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

import java.util.List;
import java.util.Map;


public class Main {
    private static Debug state = new Debug();
    public static void main(String[] args) throws InterruptedException, DBusException {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize("750687646325407844",handlers,true);
        Connection conn = new Connection();
        Runtime.getRuntime().addShutdownHook(new Thread(DiscordRPC::discordShutdown));
        DBusConnection dBusConn = DBusConnection.getConnection(DBusConnection.DBusBusType.SESSION);
        dBusConn.requestBusName("com.nopersonlinfo.freemyip.rpc");
        dBusConn.exportObject("/Debug",state);
        while(true) {
            if (!conn.failed()) {
                Map<String, String> status = conn.runCommand(new Command("status"));
                String state = "";
                boolean getSong = true;
                switch (status.getOrDefault("state", "unknown")) {
                    case "play":
                        state = "Playing";
                        break;
                    case "pause":
                        state = "Paused";
                        break;
                    case "stop":
                        //fallthrough
                    default:
                        state = "Not playing.";
                        getSong = false;
                }
                String song = "No Song.";
                DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(state);
                if (getSong) {
                    Map<String, String> response = conn.runCommand(new Command("currentsong"));
                    song = response.getOrDefault("file", "unknown");
                    List<String> songPath = List.of(song.split("/"));
                    song = songPath.get(songPath.size() - 1);
                    int elapseTime = Double.valueOf(status.getOrDefault("elapsed", "0.0")).intValue();
                    int totalTime = Double.valueOf(response.getOrDefault("Time", "0.0")).intValue();
                    long currentTime = System.currentTimeMillis();
                    builder = builder.setEndTimestamp(currentTime + (totalTime - elapseTime) * 1000L);
                }
                DiscordRPC.discordUpdatePresence(builder.setDetails(song).setBigImage("music-logo", "Music Player Daemon").setSmallImage(Distros.getDistroPath(), Distros.getDistroName()).build());
                DiscordRPC.discordRunCallbacks();
                Thread.sleep(1000);
            }else{
                conn.retry();
                DiscordRPC.discordClearPresence();
                Thread.sleep(10000);
            }
        }
    }
    public static boolean getDebugState(){
        return state.isDebug();
    }
}
