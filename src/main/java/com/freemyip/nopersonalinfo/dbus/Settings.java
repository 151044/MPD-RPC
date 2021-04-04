package com.freemyip.nopersonalinfo.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;

import java.util.prefs.Preferences;

public class Settings implements DBusInterface {
    private boolean displayNoSong = true;
    private static Preferences prefs = Preferences.userNodeForPackage(Settings.class);
    public Settings(){
        displayNoSong = prefs.getBoolean("displayNoSong",true);
    }
    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getObjectPath() {
        return "/Settings";
    }

    public boolean displayNoSong() {
        return displayNoSong;
    }

    public void displayOnNoSong(boolean noSongHide) {
        this.displayNoSong = noSongHide;
        prefs.putBoolean("displayNoSong",displayNoSong);
    }
}
