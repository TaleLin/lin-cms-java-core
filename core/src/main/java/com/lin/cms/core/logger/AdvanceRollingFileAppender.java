package com.lin.cms.core.logger;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.util.ContextUtil;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class AdvanceRollingFileAppender<E> extends FileAppender<E> {

    File currentlyActiveFile;

    public static final long DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Append to or truncate the file? The default value for this variable is
     * <code>true</code>, meaning that by default a <code>FileAppender</code> will
     * append to an existing file and not truncate it.
     */
    protected boolean append = true;

    /**
     * The name of the active log file.
     */
    protected String fileName = null;

    private boolean prudent = false;

    private FileSize bufferSize = new FileSize(DEFAULT_BUFFER_SIZE);

    private String dir;

    public static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    FileSize maxFileSize = new FileSize(DEFAULT_MAX_FILE_SIZE);

    /**
     * 获取日志文件目录
     *
     * @return dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * 设置日志文件目录
     *
     * @param dir
     */
    public void setDir(String dir) {
        if (this.isAbsolute(dir)) {
            this.dir = dir;
        } else {
            String cmd = System.getProperty("user.dir");
            Path path = FileSystems.getDefault().getPath(cmd, dir);
            this.dir = path.toAbsolutePath().toString();
        }
    }

    public FileSize getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(FileSize maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * The <b>File</b> property takes a string value which should be the name of
     * the file to append to.
     */
    public void setFile(String file) {
        if (file == null) {
            Date now = new Date();
            String subDir = this.getPresentTime(now, "yyyy-MM");
            String filename = this.getPresentTime(now, "yyyy-MM-dd");
            String trueFilename = String.format("%s/%s/%s.log", this.dir, subDir, filename);
            this.fileName = trueFilename;
        } else {
            this.fileName = file;
        }
    }

    /**
     * Returns the value of the <b>Append</b> property.
     */
    public boolean isAppend() {
        return append;
    }

    /**
     * Returns the value of the <b>File</b> property.
     *
     * <p>
     * This method may be overridden by derived classes.
     */
    public String getFile() {
        return fileName;
    }

    /**
     * yyyy-MM-dd
     * yyyy-MM-dd hh:mm:ss
     * yyyy-MM
     *
     * @param format 格式
     * @return 字符串
     */
    public String getPresentTime(String format) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(now);
    }

    public String getPresentTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * If the value of <b>File</b> is not <code>null</code>, then
     * {@link #openFile} is called with the values of <b>File</b> and
     * <b>Append</b> properties.
     */
    public void start() {
        if (this.dir == null) {
            addError("log dir must be not be empty. Aborting.");
            return;
        }
        this.setFile(null);
        // we don't want to void existing log files
        if (!append) {
            addWarn("Append mode is mandatory for RollingFileAppender. Defaulting to append=true.");
            append = true;
        }
        if (isPrudent()) {
            if (rawFileProperty() != null) {
                addWarn("Setting \"File\" property to null on account of prudent mode");
                setFile(null);
            }
        }
        currentlyActiveFile = new File(getFile());
        addInfo("Active log file name: " + getFile());
        super.start();
    }

    @Override
    public void stop() {
        super.stop();

        Map<String, String> map = ContextUtil.getFilenameCollisionMap(context);
        if (map == null || getName() == null)
            return;

        map.remove(getName());
    }

    protected boolean checkForFileCollisionInPreviousFileAppenders() {
        boolean collisionsDetected = false;
        if (fileName == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) context.getObject(CoreConstants.FA_FILENAME_COLLISION_MAP);
        if (map == null) {
            return collisionsDetected;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (fileName.equals(entry.getValue())) {
                addErrorForCollision("File", entry.getValue(), entry.getKey());
                collisionsDetected = true;
            }
        }
        if (name != null) {
            map.put(getName(), fileName);
        }
        return collisionsDetected;
    }

    protected void addErrorForCollision(String optionName, String optionValue, String appenderName) {
        addError("'" + optionName + "' option has the same value \"" + optionValue + "\" as that given for appender [" + appenderName + "] defined earlier.");
    }

    /**
     * <p>
     * Sets and <i>opens</i> the file where the log output will go. The specified
     * file must be writable.
     *
     * <p>
     * If there was already an opened file, then the previous file is closed
     * first.
     *
     * <p>
     * <b>Do not use this method directly. To configure a FileAppender or one of
     * its subclasses, set its properties one by one and then call start().</b>
     *
     * @param file_name The path to the log file.
     */
    public void openFile(String file_name) throws IOException {
        lock.lock();
        try {
            File file = new File(file_name);
            boolean result = FileUtil.createMissingParentDirectories(file);
            if (!result) {
                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }

            ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, append, bufferSize.getSize());
            resilientFos.setContext(context);
            setOutputStream(resilientFos);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return true if in prudent mode
     * @see #setPrudent(boolean)
     */
    public boolean isPrudent() {
        return prudent;
    }

    /**
     * When prudent is set to true, file appenders from multiple JVMs can safely
     * write to the same file.
     *
     * @param prudent
     */
    public void setPrudent(boolean prudent) {
        this.prudent = prudent;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void setBufferSize(FileSize bufferSize) {
        addInfo("Setting bufferSize to [" + bufferSize.toString() + "]");
        this.bufferSize = bufferSize;
    }

    private void safeWrite(E event) throws IOException {
        ResilientFileOutputStream resilientFOS = (ResilientFileOutputStream) getOutputStream();
        FileChannel fileChannel = resilientFOS.getChannel();
        if (fileChannel == null) {
            return;
        }

        // Clear any current interrupt (see LOGBACK-875)
        boolean interrupted = Thread.interrupted();

        FileLock fileLock = null;
        try {
            fileLock = fileChannel.lock();
            long position = fileChannel.position();
            long size = fileChannel.size();
            if (size != position) {
                fileChannel.position(size);
            }
            super.writeOut(event);
        } catch (IOException e) {
            // Mainly to catch FileLockInterruptionExceptions (see LOGBACK-875)
            resilientFOS.postIOFailure(e);
        } finally {
            if (fileLock != null && fileLock.isValid()) {
                fileLock.release();
            }

            // Re-interrupt if we started in an interrupted state (see LOGBACK-875)
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Implemented by delegating most of the rollover work to a rolling policy.
     */
    public void rollover() {
        lock.lock();
        try {
            // Note: This method needs to be synchronized because it needs exclusive
            // access while it closes and then re-opens the target file.
            //
            // make sure to close the hereto active log file! Renaming under windows
            // does not work for open files.
            this.closeOutputStream();
            attemptRollover();
            attemptOpenFile();
        } finally {
            lock.unlock();
        }
    }

    private void attemptOpenFile() {
        try {
            this.setFile(null);
            // update the currentlyActiveFile LOGBACK-64
            currentlyActiveFile = new File(getFile());

            // This will also close the file. This is OK since multiple close operations are safe.
            this.openFile(getFile());
        } catch (IOException e) {
            addError("setFile(" + fileName + ", false) call failed.", e);
        }
    }

    private void attemptRollover() {
        File renamedFile = getRenameFile();
        currentlyActiveFile.renameTo(renamedFile);
    }

    /**
     * This method differentiates RollingFileAppender from its super class.
     */
    @Override
    protected void subAppend(E event) {
        // 需要同步
        synchronized (this) {
            // 1. size 超过
            if (currentlyActiveFile.length() >= maxFileSize.getSize()) {
                this.rollover();
            }
            // 2. 时间，过了一天
            if (!this.checkIsPresent()) {
                this.rollover();
            }
        }
        super.subAppend(event);
    }

    /**
     * 检查当天的日志文件是否存在
     *
     * @return
     */
    private boolean checkIsPresent() {
        this.setFile(null);
        File f = new File(getFile());
        return f.exists();
    }

    private File getRenameFile() {
        Date now = new Date();
        String t1 = this.getPresentTime(now, "yyyy-MM");
        String t2 = this.getPresentTime(now, "yyyy-MM-dd");
        String t3 = this.getPresentTime(now, "hh:mm:ss");
        String trueFilename = String.format("%s/%s/%s-%s.log", this.dir, t1, t2, t3);
        File file = new File(trueFilename);
        return file;
    }

    /**
     * 重写 append
     *
     * @param eventObject
     */
    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }
        subAppend(eventObject);
    }

    @Override
    protected void writeOut(E event) throws IOException {
        if (prudent) {
            safeWrite(event);
        } else {
            super.writeOut(event);
        }
    }

    @SuppressWarnings("Since15")
    private boolean isAbsolute(String str) {
        Path path = FileSystems.getDefault().getPath(str);
        return path.isAbsolute();
    }
}
