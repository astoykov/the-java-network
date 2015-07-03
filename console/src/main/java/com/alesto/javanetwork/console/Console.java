package com.alesto.javanetwork.console;

import org.springframework.web.client.RestTemplate;

import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Created by Alex on 29/06/2015.
 */
public class Console {

    private static RestDataService restDataService = new RestDataService(new RestTemplate());

    public static void main(String[] args)
    {
        System.out.println("Welcome to The Java Network!");
        printHelp();

        CommandExecutor executor = new CommandExecutor();
        Scanner in = new Scanner(System.in);

        while(in.hasNextLine())
        {
            try {
                LineCommand command = new LineCommand(in.nextLine());

                switch (command.name) {
                    case READ:
                        executor.executeRead(command.user);
                        break;
                    case POST:
                        executor.executePost(command.user, command.param);
                        break;
                    case WALL:
                        executor.executeWall(command.user);
                        break;
                    case FOLLOW:
                        executor.executeFollow(command.user, command.param);
                        break;
                    case HELP:
                        printHelp();
                        break;
                    case EXIT:
                        System.out.println("Bye!");
                        in.close();
                        System.exit(0);
                        break;
                    case UNKNOWN:
                        System.out.println("Unknown command");
                        break;
                }
            }catch(RuntimeException rte)
            {
                if(rte.getCause() instanceof SocketException)
                {
                    System.out.println("Looks like we got an error trying to connect the server");
                }else{
                    throw rte;
                }
            }
        }


    }


    private static void printHelp()
    {
        System.out.println("Usage:");
        System.out.println("To read a user timeline: <user> Enter");
        System.out.println("To read a user wall: <user> wall Enter");
        System.out.println("To post a message: <user> -> message Enter");
        System.out.println("To follow a user: <user> follows <user> Enter");
        System.out.println("To exit: exit Enter");
        System.out.println("For help: help Enter");

    }

    public static class CommandExecutor {

        public void executePost(String userName, String message)
        {
            restDataService.postMessage(userName,message);
            System.out.println("Posted a new message for " + userName);
        }

        public void executeRead(String userName)
        {
            System.out.println(userName + "'s timeline:");
            LocalDateTime ldtNow = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            List<HashMap<?,?>> timelineMessages = restDataService.getTimeline(userName);

            for (HashMap<?, ?> mapMessage : timelineMessages) {
                LocalDateTime ldtGap = (new Date(System.currentTimeMillis() - (Long) mapMessage.get("created"))).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                System.out.println(mapMessage.get("message") + " " + howLongAgo(mapMessage));
            }
        }

        public void executeWall(String userName)
        {
            System.out.println(userName + "'s wall:");

            List<HashMap<?,?>> wallMessages = restDataService.getWall(userName);
            for (HashMap<?, ?> mapMessage : wallMessages) {

                System.out.println(mapMessage.get("username") + " - " +  mapMessage.get("message") + howLongAgo(mapMessage));
            }
        }

        public void executeFollow(String userName, String followed)
        {
            restDataService.follow(userName, followed);
            System.out.println(userName + " is now following " + followed);
        }

        public String howLongAgo(HashMap <?,?> mapMessage)
        {
            LocalDateTime ldtGap = (new Date(System.currentTimeMillis() - (Long) mapMessage.get("created"))).toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
            StringJoiner sj = new StringJoiner("", "(", " ago)");
            if(ldtGap.getHour()>0) return sj.add("" + ldtGap.getHour()).add(" hours").toString();
            if(ldtGap.getMinute()>0) return sj.add("" + ldtGap.getMinute()).add(" minutes").toString();
            return sj.add("" + ldtGap.getSecond()).add(" seconds").toString();
        }
    }


    public static class LineCommand {
        public enum CommandName {
            READ,POST,FOLLOW,WALL,UNKNOWN,HELP,EXIT;
        }

        public final CommandName name;
        public final String user;
        public final String param;

        public LineCommand(String line)
        {
            //validate and parse
            String[] words = line.split("\\s+");
            if(words.length < 1) throw new IllegalArgumentException();

            if(words.length == 1) {
                this.param = null;
                switch(words[0])
                {
                    case "exit": this.name = CommandName.EXIT; break;
                    case "help": this.name = CommandName.HELP; break;
                    default: this.name = CommandName.READ;
                }

            }else{
                switch(words[1])
                {
                    case "->": this.param = line.substring(line.indexOf("->") + 2).trim(); this.name = CommandName.POST;break;
                    case "wall": this.param = null; this.name = CommandName.WALL;break;
                    case "follows": this.param = words[2]; this.name = CommandName.FOLLOW;break;
                    default: this.name = CommandName.UNKNOWN; this.param = null;
                }
            }
            this.user = words[0];
        }
    }
}
