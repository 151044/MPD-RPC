package com.freemyip.nopersonalinfo.rpc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Distros {
    private Distros(){
        throw new AssertionError("Distros not instantiable!");
    }
    private static String distroPath = "unknown";
    private static String distroName = "unknown";
    private static Map<String,String> findLogo = new HashMap<>();
    static{
        //Hardcoded
        findLogo.put("arch","arch-logo");
        findLogo.put("fedora","fedora-logo");
        findLogo.put("endeavouros","enos-logo");
        findLogo.put("opensuse","suse-logo");
        findLogo.put("mint","mint-logo");
        findLogo.put("ubuntu","ubuntu-logo");
        findLogo.put("manjaro","manjaro-logo");
        findLogo.put("debian","debian-logo");
        findLogo.put("pop!_os","pop-logo");
        findLogo.put("gentoo","gentoo-logo");
        findLogo.put("void","void-logo");
        //First read /etc/os-release (systemd), then read /etc/issue
        String distro = "unknown";
        Path osRelease = Path.of("/etc/os-release");
        try {
            if (osRelease.toFile().exists()) {
                Map<String,String> lsbRead = Files.readAllLines(osRelease).stream().map(str -> str.split("=")).collect(Collectors.toMap(s -> s[0],s -> Arrays.stream(s).skip(1).collect(Collectors.joining("="))));
                if(lsbRead.containsKey("PRETTY_NAME")){
                    distro = lsbRead.get("PRETTY_NAME").replace("\"","");
                    String lookup = distro.toLowerCase();
                    findLogo.keySet().stream().filter(lookup::contains).findFirst().ifPresent(s -> distroPath = findLogo.get(s));
                    if(lsbRead.containsKey("BUILD_ID") && !lsbRead.get("BUILD_ID").equalsIgnoreCase("rolling")){
                        distro += " " + lsbRead.get("BUILD_ID");
                    }
                    distroName = distro;
                }
            } else {
                Path issue = Path.of("/etc/issue");
                if (issue.toFile().exists()) {
                    List<String> raw = Files.readAllLines(issue).stream().map(s -> s.split(" ")).flatMap(Arrays::stream).collect(Collectors.toList());
                    raw.stream().filter(str -> findLogo.keySet().stream().anyMatch(s -> s.contains(str))).limit(1).forEach(str -> {
                        distroName = str;
                        findLogo.keySet().stream().filter(s -> s.contains(str)).findFirst().ifPresent(s -> distroPath = s);
                    });
                }
            }
        }catch(IOException ioe){
            if(Main.getDebugState()){
                ioe.printStackTrace();
            }
        }
    }

    public static String getDistroName() {
        return distroName;
    }

    public static String getDistroPath() {
        return distroPath;
    }
    public static void setDistro(String name){
        distroName = name;
        String lookup = name.toLowerCase();
        findLogo.keySet().stream().filter(lookup::contains).findFirst().ifPresent(s -> distroPath = findLogo.get(s));
    }
}
