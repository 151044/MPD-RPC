package com.freemyip.nopersonalinfo.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;

public class Debug implements DBusInterface {
    private boolean debug = false;
    public boolean isDebug(){
        return debug;
    }
    public void setDebug(boolean flag){
        debug = flag;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getObjectPath() {
        return "/Debug";
    }
}
