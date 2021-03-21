package com.freemyip.nopersonalinfo.rpc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Distros {
    private Distros(){
        throw new AssertionError("Distros not instantiable!");
    }
    private static String distroPath = "unknown";
    private static String distroName = "unknown";
    static{
        //First read /etc/os-release (systemd), then read /etc/issue
        String distro = "unknown";
        Path lsbRelease = Path.of("/etc/os-release");
        try {
            if (lsbRelease.toFile().exists()) {
                Optional<String> optDistro = Files.readAllLines(Path.of("")).stream().filter(s -> s.startsWith("PRETTY_NAME")).map(s -> s.strip().split("=")[1].replace("\"",""))
                        .findFirst();
                if(optDistro.isPresent()){
                    distro = optDistro.get();
                    String lookup = distro.toLowerCase();
                }
            } else {
                Path issue = Path.of("/etc/issue");
                if (issue.toFile().exists()) {

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
}
