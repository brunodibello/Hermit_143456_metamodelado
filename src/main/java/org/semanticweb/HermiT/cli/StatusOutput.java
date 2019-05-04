/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.cli;

import java.io.PrintStream;

class StatusOutput {
    protected int level;
    public static final int ALWAYS = 0;
    public static final int STATUS = 1;
    public static final int DETAIL = 2;
    public static final int DEBUG = 3;

    public StatusOutput(int inLevel) {
        this.level = inLevel;
    }

    public void log(int inLevel, String message) {
        if (inLevel <= this.level) {
            System.err.println(message);
        }
    }
}

