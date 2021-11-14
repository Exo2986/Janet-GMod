package gg.galaxygaming.janetgmod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplitOutputStream extends OutputStream {
    private final OutputStream primary;
    private OutputStream secondary;
    private boolean firstRun;
    private int logDate;

    public SplitOutputStream(OutputStream primary) {
        if (primary == null)
            throw new NullPointerException();
        this.firstRun = true;
        this.primary = primary;
        checkDay();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void checkDay() {
        int curDate = Calendar.getInstance().get(Calendar.DATE);
        if (this.firstRun || this.logDate != curDate) {
            File log = new File("logs/" + new SimpleDateFormat("MM-dd-yy").format(Calendar.getInstance().getTime()) + ".txt");
            if (!log.exists())
                try {
                    log.createNewFile();
                } catch (IOException ignored) {
                }
            this.logDate = curDate;
            try {
                if (!this.firstRun) {
                    this.secondary.flush();
                    this.secondary.close();
                } else
                    this.firstRun = false;
                this.secondary = new FileOutputStream(log, true);
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void write(int i) throws IOException {
        checkDay();
        this.primary.write(i);
        this.secondary.write(i);
    }

    @Override
    public void write(byte[] i) throws IOException {
        checkDay();
        this.primary.write(i);
        this.secondary.write(i);
    }

    @Override
    public void write(byte[] i, int o, int length) throws IOException {
        checkDay();
        this.primary.write(i, o, length);
        this.secondary.write(i, o, length);
    }

    @Override
    public void flush() throws IOException {
        this.primary.flush();
        this.secondary.flush();
    }

    @Override
    public void close() throws IOException {
        this.primary.close();
        this.secondary.close();
    }
}