package com.freemyip.nopersonalinfo.mpd;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Connection {
    private boolean failed = false;
    private Socket sock;
    private BufferedReader reader;
    private String hostname;
    private int port;
    private BufferedWriter writer;
    public Connection() {
        this("localhost",6600);
    }
    public Connection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        init();
    }
    public Map<String,String> runCommand(Command c){
        if(!failed){
            try {
                writer.write(c.commandForm());
                writer.newLine();
                writer.flush();
                String response;
                Map<String,String> ret = new HashMap<>();
                while(!(response = reader.readLine()).equals("OK")){
                    if(response.startsWith("ACK")){
                        System.out.println(response);
                        return Map.of();
                    }else{
                        String[] split = response.split(":");
                        ret.put(split[0], Arrays.stream(split).skip(1).map(s -> s.strip()).collect(Collectors.joining(":")));
                    }
                }
                return ret;
            } catch (IOException e) {
                e.printStackTrace();
                failed = true;
                return Map.of();
            }
        }else{
            return Map.of();
        }
    }
    public boolean retry(){
        return init();
    }
    public boolean failed(){
        return failed;
    }
    private boolean init(){
        try {
            sock = new Socket(hostname.trim(), port);
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            if (!reader.readLine().startsWith("OK MPD")) {
                failed = true;
            } else {
                writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
            failed = true;
        }
        return failed;
    }
}
