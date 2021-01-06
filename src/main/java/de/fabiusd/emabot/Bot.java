package de.fabiusd.emabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {

    public static MessageChannel mc;
    public static Timer t;
    public static TimerTask tt;

    public static void main(String[] args) throws FileNotFoundException, LoginException {
        Scanner scan = new Scanner(new File("token.txt"));
        JDABuilder jda = JDABuilder.createDefault(scan.nextLine());
        jda.setActivity(Activity.watching("porn"));
        jda.addEventListeners(new MyListener());
        jda.build();

        tt = new TimerTask() {
            @Override
            public void run() {
                try {
                    FileUtils.copyURLToFile(new URL("https://ema-bonn.de/index.php/service/neuigkeiten"), new File("new"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (!(new File("old")).exists() || FileUtils.contentEquals(new File("new"), new File("old"))) {
                        mc.sendMessage("@everyone es gibt news! https://ema-bonn.de/").queue();
                        FileUtils.copyFile(new File("new"), new File("old"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    public static class MyListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getMessage().getContentRaw().equals("..newshere")) {
                mc = event.getChannel();
                mc.sendMessage("Dieser Channel wird jetzt als Update Channel verwendet!").queue();
                if (t == null) {
                    t = new Timer();
                    t.scheduleAtFixedRate(tt, 0L, 100000L);
                }
            }
        }
    }

}
