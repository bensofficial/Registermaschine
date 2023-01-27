package org.benjaminschmitz.school.registermaschine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistermaschineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistermaschineController.class);

    @PostMapping("/execute")
    public String execute(@RequestParam("commands") String commandParameter, Model model) {
        String[] commands = commandParameter.split("\n");
        Registermaschine registermaschine = new Registermaschine(0, 0);
        StringBuilder resultContent = new StringBuilder();

        resultContent.append(registermaschine).append(System.lineSeparator());
        try {
            while (registermaschine.isRunning()) {
                if (registermaschine.getBz() >= commands.length || registermaschine.getBz() < 0) {
                    throw new RuntimeException("Der Befehlszähler liegt außerhalb des erlaubten Bereichs.");
                }
                String command = commands[registermaschine.getBz()].trim();
                resultContent.append("Befehl: ").append(command).append(System.lineSeparator());
                registermaschine.executeCommand(command);
                resultContent.append(registermaschine).append(System.lineSeparator());
            }
        } catch (RuntimeException e) {
            resultContent.append("Fehler: ").append(e.getMessage()).append(System.lineSeparator());
            LOGGER.error("An error occurred while executing the commands.");
        }

        model.addAttribute("result", resultContent.toString());
        return "execute";
    }
}
