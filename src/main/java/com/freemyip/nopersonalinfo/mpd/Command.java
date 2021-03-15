package com.freemyip.nopersonalinfo.mpd;

import java.util.List;
import java.util.stream.Collectors;

public class Command {
    private String name;
    private List<String> args;
    public Command(String name, String... args){
        this.name = name;
        this.args = List.of(args);
    }
    public String commandForm(){
        return name + " " + args.stream().map(s -> s.contains(" ") ? "\"" + s + "\"" : s).collect(Collectors.joining(" "));
    }
}
