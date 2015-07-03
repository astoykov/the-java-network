package com.alesto.javanetwork.console;


import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;

/**
 * Tests important methods on the Console class
 */
public class ConsoleTest {

    // Test the how long ago descriptor method
    @Test
    public void testHowLongAgo1() throws Exception {
        Console.CommandExecutor executor = new Console.CommandExecutor();

        final HashMap<String,Long> testMsg = new HashMap<String,Long>();

        testMsg.put("created", System.currentTimeMillis() - 2000);
        assertEquals("(2 seconds ago)",executor.howLongAgo(testMsg));
    }

    @Test
    public void testHowLongAgo2() throws Exception {
        Console.CommandExecutor executor = new Console.CommandExecutor();

        final HashMap<String,Long> testMsg = new HashMap<String,Long>();

        testMsg.put("created", System.currentTimeMillis() - 59*60*1000);
        assertEquals("(59 minutes ago)", executor.howLongAgo(testMsg));
    }

    @Test
    public void testHowLongAgo3() throws Exception {
        Console.CommandExecutor executor = new Console.CommandExecutor();

        final HashMap<String,Long> testMsg = new HashMap<String,Long>();

        testMsg.put("created", System.currentTimeMillis() - 2*3600*1000);
        assertEquals("(2 hours ago)",executor.howLongAgo(testMsg));
    }


    // Test the line command parsing and initialization
    @Test
    public void testLineCommand_exit() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("exit");
        assertEquals(Console.LineCommand.CommandName.EXIT, lineCommand.name);
    }

    @Test
    public void testLineCommand_post() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("A -> aaa");
        assertEquals(Console.LineCommand.CommandName.POST, lineCommand.name);
    }

    @Test
    public void testLineCommand_post2() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("A -> aaa zzz www");
        assertEquals(Console.LineCommand.CommandName.POST, lineCommand.name);
        assertEquals("aaa zzz www", lineCommand.param);
    }

    @Test
    public void testLineCommand_read() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("A");
        assertEquals(Console.LineCommand.CommandName.READ, lineCommand.name);
    }

    @Test
    public void testLineCommand_wall() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("A wall");
        assertEquals(Console.LineCommand.CommandName.WALL, lineCommand.name);
    }

    @Test
    public void testLineCommand_follows() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("A follows B");
        assertEquals(Console.LineCommand.CommandName.FOLLOW, lineCommand.name);
    }

    @Test
    public void testLineCommand_help() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("help");
        assertEquals(Console.LineCommand.CommandName.HELP, lineCommand.name);
    }

    @Test
    public void testLineCommand_unknown() throws Exception {
        Console.LineCommand lineCommand = new Console.LineCommand("aaha ahaa ahaha");
        assertEquals(Console.LineCommand.CommandName.UNKNOWN, lineCommand.name);
    }
}
