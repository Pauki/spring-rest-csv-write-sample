package cz.pauki.util;

import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Optional;

/**
 * Util class for file operation
 */
public class FileUtil {

    /** Search concrete line in file
     *
     * @param fileChannel file channel
     * @param line line for search
     * @return optional with request
     */
    public static Optional<String> searchLineInFile(@NotNull FileChannel fileChannel, @NotNull String line){
        BufferedReader br = new BufferedReader(Channels.newReader(fileChannel, "Cp1250"));
        return br.lines().filter(s -> s.equals(line)).findAny();
    }

    /** Write text at the end of file
     *
     * @param fileChannel file channel
     * @param text text for write
     * @throws IOException exception
     */
    public static void writeToFile(@NotNull FileChannel fileChannel, @NotNull String text) throws IOException {
        Assert.notNull(fileChannel, "fileChannel cannot be null");

        ByteBuffer buff = ByteBuffer.wrap(text.getBytes("Cp1250"));
        fileChannel.position(fileChannel.size());
        fileChannel.write(buff);
    }

    /** Retrieve lock for requested file
     *
     * Don´t forget to release obtained lock!!!
     *
     * @param file file we want to lock
     * @return (@see java.nio.channels.FileLock) or null if lock not obtained
     */
    public static FileLock getFileLock(@NotNull File file){
        Assert.notNull(file, "file cannot be null");

        FileLock lock = null;
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            lock = randomAccessFile.getChannel().tryLock();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OverlappingFileLockException e){
            return null;
        }

        return lock;
    }
}
