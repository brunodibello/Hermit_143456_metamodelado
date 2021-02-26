package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;

public class HelpCommand
extends AbstractCommand {
    public HelpCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "prints this list of command", "commandName", "prints help for a command"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: help");
        writer.println("    Prints this message.");
        writer.println("usage: help commandName");
        writer.println("    Prints help for the command commandName.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            String commandName = args[1];
            DebuggerCommand command = this.m_debugger.getCommand(commandName);
            if (command == null) {
                this.m_debugger.getOutput().println("Unknown command '" + commandName + "'.");
            } else {
                command.printHelp(this.m_debugger.getOutput());
            }
        } else {
            int index;
            String[] description;
            this.m_debugger.getOutput().println("Available commands are:");
            int maxFirstColumnWidth = 0;
            for (DebuggerCommand command : this.m_debugger.getDebuggerCommands().values()) {
                description = command.getDescription();
                for (index = 0; index < description.length; index += 2) {
                    int firstColumnWidth = command.getCommandName().length();
                    if (description[index].length() != 0) {
                        firstColumnWidth += 1 + description[index].length();
                    }
                    maxFirstColumnWidth = Math.max(maxFirstColumnWidth, firstColumnWidth);
                }
            }
            for (DebuggerCommand command : this.m_debugger.getDebuggerCommands().values()) {
                description = command.getDescription();
                for (index = 0; index < description.length; index += 2) {
                    String commandLine = command.getCommandName();
                    if (description[index].length() != 0) {
                        commandLine = commandLine + ' ' + description[index];
                    }
                    this.m_debugger.getOutput().print("  ");
                    this.m_debugger.getOutput().print(commandLine);
                    for (int i = commandLine.length(); i < maxFirstColumnWidth; ++i) {
                        this.m_debugger.getOutput().print(' ');
                    }
                    this.m_debugger.getOutput().print("  :  ");
                    this.m_debugger.getOutput().println(description[index + 1]);
                }
            }
            this.m_debugger.getOutput().println();
            this.m_debugger.getOutput().println("Nodes in the current model are identified by node IDs.");
            this.m_debugger.getOutput().println("Predicates are written as follows, where uri can be abbreviated or full:");
            this.m_debugger.getOutput().println("    ==      equality");
            this.m_debugger.getOutput().println("    !=      inequality");
            this.m_debugger.getOutput().println("    +uri    atomic concept with the URI uri");
            this.m_debugger.getOutput().println("    -uri    atomic role with the URI uri");
            this.m_debugger.getOutput().println("    $uri    description graph with the URI uri");
        }
    }
}

