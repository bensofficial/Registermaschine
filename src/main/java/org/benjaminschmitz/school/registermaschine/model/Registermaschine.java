package org.benjaminschmitz.school.registermaschine.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Registermaschine {
    
    public static final String LABEL_REGEX = "[A-Za-z]+[A-Za-z0-9]*";

    private static final int REGISTER_SIZE = 16;
    private final int[] register;
    private int bz;
    private int a;
    private boolean running;
    private final Map<String, Integer> bzIndex;

    public Registermaschine(int bz, int a) {
        this(bz, a, new int[16]);
    }

    private Registermaschine(int bz, int a, int[] register) {
        if (register.length != REGISTER_SIZE) {
            throw new IllegalArgumentException("Register size must be " + REGISTER_SIZE);
        }
        this.bz = bz;
        this.a = a;
        this.register = register;
        this.running = true;
        this.bzIndex = new HashMap<>();
    }

    public void executeCommand(String command) {
        if (!running) throw new RuntimeException("Die Registermaschine läuft nicht mehr.");
        if (command.isEmpty()) {
            bz++;
            return;
        }
        String[] commandSubstrings = command.split(" ", 3);
        switch (commandSubstrings[0].toLowerCase()) {
            case "load": {
                load(getIntegerParameter(commandSubstrings));
                break;
            }
            case "loadi":
            case "dload": {
                dload(getIntegerParameter(commandSubstrings));
                break;
            }
            case "store": {
                store(getIntegerParameter(commandSubstrings));
                break;
            }
            case "add": {
                add(getIntegerParameter(commandSubstrings));
                break;
            }
            case "addi": {
                addi(getIntegerParameter(commandSubstrings));
                break;
            }
            case "end":
            case "halt":{
                end();
                break;
            }
            case "sub": {
                sub(getIntegerParameter(commandSubstrings));
                break;
            }
            case "subi": {
                subi(getIntegerParameter(commandSubstrings));
                break;
            }
            case "mult":
            case "mul": {
                mul(getIntegerParameter(commandSubstrings));
                break;
            }
            case "muli": {
                muli(getIntegerParameter(commandSubstrings));
                break;
            }
            case "div": {
                div(getIntegerParameter(commandSubstrings));
                break;
            }
            case "divi": {
                divi(getIntegerParameter(commandSubstrings));
                break;
            }
            case "jump":
            case "jmp": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jump(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jump(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jgt": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jgt(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jgt(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jge": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jge(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jge(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jlt": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jlt(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jlt(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jle": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jle(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jle(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jeq": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jeq(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jeq(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            case "jne": {
                if (getParameter(commandSubstrings).matches(LABEL_REGEX)) {
                    if (!bzIndex.containsKey(getParameter(commandSubstrings))) {
                        throw new RuntimeException("Das angegebene Label existiert nicht.");
                    }
                    jne(bzIndex.get(getParameter(commandSubstrings)));
                } else {
                    jne(getIntegerParameter(commandSubstrings));
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Illegaler Befehl.");
            }
        }
    }

    public void buildIndex(String[] commands) {
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            if(command.matches( LABEL_REGEX+ ":.*")) {
                String key = command.split(":")[0];
                if(bzIndex.containsKey(key)) {
                    throw new RuntimeException("Doppeltes Label: " + key);
                }
                bzIndex.put(key, i);
            }
        }
    }

    private void load(int registerId) {
        checkRegisterId(registerId);
        a = register[registerId];
        bz++;
    }

    private void dload(int number) {
        a = number;
        bz++;
    }

    private void store(int registerId) {
        checkRegisterId(registerId);
        register[registerId] = a;
        bz++;
    }

    private void add(int registerId) {
        checkRegisterId(registerId);
        a += register[registerId];
        bz++;
    }

    private void addi(int value) {
        a += value;
        bz++;
    }

    private void end() {
        bz++;
        running = false;
    }

    private void sub(int registerId) {
        checkRegisterId(registerId);
        a -= register[registerId];
        bz++;
    }

    private void subi(int value) {
        a -= value;
        bz++;
    }

    private void mul(int registerId) {
        checkRegisterId(registerId);
        a *= register[registerId];
        bz++;
    }

    private void muli(int value) {
        a *= value;
        bz++;
    }

    private void div(int registerId) {
        checkRegisterId(registerId);
        a /= register[registerId];
        bz++;
    }

    private void divi(int value) {
        a /= value;
        bz++;
    }

    private void jump(int bz) {
        this.bz = bz;
    }

    private void jgt(int bz) {
        if (a > 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private void jge(int bz) {
        if (a >= 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private void jlt(int bz) {
        if (a < 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private void jle(int bz) {
        if (a <= 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private void jeq(int bz) {
        if (a == 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private void jne(int bz) {
        if (a != 0) {
            this.bz = bz;
        } else {
            this.bz++;
        }
    }

    private String getParameter(String[] commands) {
        if (commands.length <= 1) {
            throw new RuntimeException("Fehlender Befehlparameter.");
        }
        return commands[1];
    }

    private int getIntegerParameter(String[] commands) {
        try {
            return Integer.parseInt(getParameter(commands));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Der Parameter ist kein Zahlenwert.", e);
        }
    }

    private void checkRegisterId(int registerId) {
        if (registerId < 0 || registerId >= register.length) {
            throw new RuntimeException("Der angegebene Index des Registers liegt außerhalb des erlaubten Bereichs.");
        }
    }

    @Override
    public String toString() {
        return "Registermaschine{" + "bz=" + bz + ", a=" + a + ", register=" + Arrays.toString(register) + '}';
    }

    public int getBz() {
        return bz;
    }

    public boolean isRunning() {
        return running;
    }
}
