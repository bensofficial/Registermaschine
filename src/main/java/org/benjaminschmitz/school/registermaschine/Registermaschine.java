package org.benjaminschmitz.school.registermaschine;

import java.util.Arrays;

public class Registermaschine {

    private static final int REGISTER_SIZE = 16;
    private final int[] register;
    private int bz;
    private int a;
    private boolean running;

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
    }

    public void executeCommand(String command) {
        if (!running) throw new RuntimeException("Die Registermaschine läuft nicht mehr.");
        if (command.isEmpty()) {
            throw new RuntimeException("Kein Befehl angegeben.");
        }
        String[] commandSubstrings = command.split(" ", 3);
        switch (commandSubstrings[0].toLowerCase()) {
            case "load" -> load(getParameter(commandSubstrings));
            case "dload" -> dload(getParameter(commandSubstrings));
            case "store" -> store(getParameter(commandSubstrings));
            case "add" -> add(getParameter(commandSubstrings));
            case "end" -> end();
            case "sub" -> sub(getParameter(commandSubstrings));
            case "mult" -> mult(getParameter(commandSubstrings));
            case "div" -> div(getParameter(commandSubstrings));
            case "jump" -> jump(getParameter(commandSubstrings));
            case "jgt" -> jgt(getParameter(commandSubstrings));
            case "jge" -> jge(getParameter(commandSubstrings));
            case "jlt" -> jlt(getParameter(commandSubstrings));
            case "jle" -> jle(getParameter(commandSubstrings));
            case "jeq" -> jeq(getParameter(commandSubstrings));
            case "jne" -> jne(getParameter(commandSubstrings));
            default -> throw new IllegalArgumentException("Illegaler Befehl.");
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

    private void end() {
        bz++;
        running = false;
    }

    private void sub(int registerId) {
        checkRegisterId(registerId);
        a -= register[registerId];
        bz++;
    }

    private void mult(int registerId) {
        checkRegisterId(registerId);
        a *= register[registerId];
        bz++;
    }

    private void div(int registerId) {
        checkRegisterId(registerId);
        a /= register[registerId];
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

    private int getParameter(String[] commands) {
        if (commands.length <= 1) {
            throw new RuntimeException("Fehlender Befehlparameter.");
        }
        String command = commands[1];
        try {
            return Integer.parseInt(command);
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
