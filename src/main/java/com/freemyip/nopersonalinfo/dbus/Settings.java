package com.freemyip.nopersonalinfo.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;

public class Settings implements DBusInterface {
    private boolean displayNoSong = true;
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
    }
}
