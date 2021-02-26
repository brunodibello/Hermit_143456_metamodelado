package org.semanticweb.HermiT.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class ConsoleTextArea
extends JTextArea {
    protected final ConsoleWriter m_writer = new ConsoleWriter();
    protected final ConsoleReader m_reader = new ConsoleReader();
    protected int m_userTypedTextStart;

    public ConsoleTextArea() {
        this.setDocument(new ConsoleDocument());
        this.enableEvents(8L);
    }

    public Writer getWriter() {
        return this.m_writer;
    }

    public Reader getReader() {
        return this.m_reader;
    }

    public void clear() {
        this.m_userTypedTextStart = 0;
        this.setText("");
    }

    protected void moveToEndIfNecessary() {
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();
        if (selectionEnd < this.m_userTypedTextStart || selectionEnd == this.m_userTypedTextStart && selectionStart != selectionEnd) {
            int length = this.getDocument().getLength();
            this.select(length, length);
        }
    }

    @Override
    public void replaceSelection(String string) {
        this.moveToEndIfNecessary();
        super.replaceSelection(string);
    }

    @Override
    protected void processKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 10) {
            super.processKeyEvent(event);
        }
        if (event.getID() == 401 && event.getKeyCode() == 10) {
            String text;
            int textEnd = this.getDocument().getLength();
            this.select(textEnd, textEnd);
            super.replaceSelection("\n");
            textEnd = this.getDocument().getLength();
            try {
                text = this.getDocument().getText(this.m_userTypedTextStart, textEnd - this.m_userTypedTextStart);
            }
            catch (BadLocationException error) {
                text = "";
            }
            this.m_reader.addToBuffer(text);
            this.m_userTypedTextStart = textEnd;
            this.select(this.m_userTypedTextStart, this.m_userTypedTextStart);
        }
    }

    protected class ConsoleReader
    extends Reader {
        protected char[] m_buffer = new char[4096];
        protected int m_nextCharToRead = 0;
        protected int m_firstFreeChar = 0;

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void addToBuffer(String string) {
            Object object = this.lock;
            synchronized (object) {
                if (this.m_nextCharToRead == this.m_firstFreeChar) {
                    this.m_nextCharToRead = 0;
                    this.m_firstFreeChar = 0;
                } else if (this.m_nextCharToRead != 0) {
                    System.arraycopy(this.m_buffer, this.m_nextCharToRead, this.m_buffer, 0, this.m_firstFreeChar - this.m_nextCharToRead);
                    this.m_nextCharToRead = 0;
                    this.m_firstFreeChar = 0;
                }
                if (this.m_firstFreeChar + string.length() > this.m_buffer.length) {
                    char[] newBuffer = new char[this.m_firstFreeChar + string.length()];
                    System.arraycopy(this.m_buffer, 0, newBuffer, 0, this.m_buffer.length);
                    this.m_buffer = newBuffer;
                }
                string.getChars(0, string.length(), this.m_buffer, this.m_firstFreeChar);
                this.m_firstFreeChar += string.length();
                this.notifyAll();
            }
        }

        @Override
        public void close() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            ConsoleTextArea.this.m_writer.flush();
            Object object = this.lock;
            synchronized (object) {
                while (this.m_nextCharToRead == this.m_firstFreeChar) {
                    try {
                        this.lock.wait();
                    }
                    catch (InterruptedException error) {
                        throw new IOException("Read interrupted.", error);
                    }
                }
                int toCopy = Math.min(this.m_firstFreeChar - this.m_nextCharToRead, length);
                System.arraycopy(this.m_buffer, this.m_nextCharToRead, buffer, offset, toCopy);
                this.m_nextCharToRead += toCopy;
                return toCopy;
            }
        }
    }

    protected class ConsoleWriter
    extends Writer
    implements ActionListener {
        protected final char[] m_buffer = new char[4096];
        protected final Timer m_timer = new Timer(500, this);
        protected int m_firstFreeChar;

        public ConsoleWriter() {
            this.m_timer.setRepeats(false);
            this.m_firstFreeChar = 0;
        }

        @Override
        public void close() {
            this.flush();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void flush() {
            Object object = this.lock;
            synchronized (object) {
                if (this.m_firstFreeChar > 0) {
                    final String string = new String(this.m_buffer, 0, this.m_firstFreeChar);
                    this.m_firstFreeChar = 0;
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            ConsoleTextArea.this.replaceSelection(string);
                            ConsoleTextArea.this.m_userTypedTextStart = ConsoleTextArea.this.getDocument().getLength();
                            ConsoleTextArea.this.select(ConsoleTextArea.this.m_userTypedTextStart, ConsoleTextArea.this.m_userTypedTextStart);
                        }
                    });
                    this.m_timer.stop();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void write(char[] buffer, int offset, int count) {
            Object object = this.lock;
            synchronized (object) {
                int lastPosition = offset + count;
                while (offset != lastPosition) {
                    int toCopy = Math.min(this.m_buffer.length - this.m_firstFreeChar, count);
                    if (toCopy == 0) continue;
                    System.arraycopy(buffer, offset, this.m_buffer, this.m_firstFreeChar, toCopy);
                    count -= toCopy;
                    offset += toCopy;
                    boolean bufferWasEmpty = this.m_firstFreeChar == 0;
                    this.m_firstFreeChar += toCopy;
                    if (this.m_firstFreeChar >= this.m_buffer.length) {
                        this.flush();
                        continue;
                    }
                    if (!bufferWasEmpty) continue;
                    this.m_timer.start();
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.flush();
        }

    }

    protected class ConsoleDocument
    extends PlainDocument {
        protected ConsoleDocument() {
        }

        @Override
        public void remove(int offset, int length) throws BadLocationException {
            if (offset >= ConsoleTextArea.this.m_userTypedTextStart) {
                super.remove(offset, length);
            }
        }

        @Override
        public void insertString(int offset, String string, AttributeSet attributeSet) throws BadLocationException {
            if (offset >= ConsoleTextArea.this.m_userTypedTextStart) {
                super.insertString(offset, string, attributeSet);
            }
        }
    }

}

