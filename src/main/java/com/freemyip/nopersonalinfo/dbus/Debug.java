package com.freemyip.nopersonalinfo.dbus;

import com.freemyip.nopersonalinfo.rpc.Distros;
import org.freedesktop.dbus.interfaces.DBusInterface;

public class Debug implements DBusInterface {
    private boolean debug = false;
    public boolean isDebug(){
        return debug;
    }
    public void setDebug(boolean flag){
        debug = flag;
    }
    public void setDistro(String distroName){
        Distros.setDistro(distroName);
    }
    public String getDistro(){
        return Distros.getDistroName();
    }
    public String getImagePath(){
        return Distros.getDistroPath();
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
