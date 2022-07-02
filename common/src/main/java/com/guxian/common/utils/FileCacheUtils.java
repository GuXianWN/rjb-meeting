package com.guxian.common.utils;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileCacheImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Log4j2
public class FileCacheUtils {
    private static final String ROOT_TMP_DIR = System.getProperty("user.dir")
            .replace('\\', '/') + "/tmp";

    private String localPath;

    /**
     * @param childrenDir must use /
     */

    public FileCacheUtils(String childrenDir) {
        this.localPath = ROOT_TMP_DIR + childrenDir;
    }

    @SneakyThrows
    public File saveFile(MultipartFile file, String fileName) {
        return saveFile(file.getInputStream(), fileName);
    }

    public File saveFile(InputStream file, String fileName) {
        // 复制文件
        File targetFile = new File(localPath + "/" + fileName, "");
        try {
            FileUtils.writeByteArrayToFile(targetFile, file.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    @SneakyThrows
    public boolean deleteCache(File fileName) {
        var file = buildFilenameWithPath(fileName.getName());
        return file.canExecute() && Files.deleteIfExists(Path.of(file.getPath()));
    }

    public File saveFileFromRemote(URL url, String filename) {
        var file = buildFilenameWithPath(filename);
        try (InputStream in = url.openStream();
        ) {
            saveFile(in, filename);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return file;
    }

    /**
     * @param filename 返回当前设置的文件路径加文件名组成的文件
     * @return
     */
    private File buildFilenameWithPath(String filename) {
        return new File(localPath + "/" + filename);
    }
}
