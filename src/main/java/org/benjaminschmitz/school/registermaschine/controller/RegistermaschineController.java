package org.benjaminschmitz.school.registermaschine.controller;

import org.benjaminschmitz.school.registermaschine.model.Registermaschine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistermaschineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistermaschineController.class);

    @PostMapping("/execute")
    public String execute(@RequestBody Commands commands) {
        String[] commandsArray = commands.getCommands().split("\n");
        Registermaschine registermaschine = new Registermaschine(0, 0);
        StringBuilder resultContent = new StringBuilder();

        registermaschine.buildIndex(commandsArray);

        unifyCommands(commandsArray);

        resultContent.append(registermaschine).append(System.lineSeparator());
        try {
            while (registermaschine.isRunning()) {
                if (registermaschine.getBz() >= commandsArray.length || registermaschine.getBz() < 0) {
                    throw new RuntimeException("Der Befehlszähler liegt außerhalb des erlaubten Bereichs.");
                }
                String command = commandsArray[registermaschine.getBz()].trim();
                resultContent.append("Befehl: ").append(command).append(System.lineSeparator());
                registermaschine.executeCommand(command);
                resultContent.append(registermaschine).append(System.lineSeparator());
            }
        } catch (RuntimeException e) {
            resultContent.append("Fehler: ").append(e.getMessage()).append(System.lineSeparator());
            LOGGER.error("An error occurred while executing the commands.", e);
        }

        return resultContent.toString();
    }

    private String[] unifyCommands(String[] commands) {
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i].trim();
            if (command.matches("\\w*:.*")) {
                commands[i] = command.substring(command.indexOf(":") + 1);
                command = commands[i].trim();
            }
            if (command.contains("#")) {
                commands[i] = command.substring(0, command.indexOf("#"));
                command = commands[i].trim();
            }
        }
        return commands;
    }
}

class Commands {
    private String commands;

    public String getCommands() {
        return commands;
    }

    public Commands setCommands(String commands) {
        this.commands = commands;
        return this;
    }
}
