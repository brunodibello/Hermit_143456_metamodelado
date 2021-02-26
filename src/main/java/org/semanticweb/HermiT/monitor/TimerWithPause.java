package org.semanticweb.HermiT.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class TimerWithPause
extends Timer {
    private static final long serialVersionUID = -9176603965017225734L;
    protected final BufferedReader m_in = new BufferedReader(new InputStreamReader(System.in));

    public TimerWithPause(OutputStream out) {
        super(out);
    }

    @Override
    protected void doStatistics() {
        super.doStatistics();
        System.out.print("Press something to continue.. ");
        System.out.flush();
        try {
            this.m_in.readLine();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}

