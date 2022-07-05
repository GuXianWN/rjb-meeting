package com.guxian.common.utils;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileCacheImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Log4j2
public class FileCacheUtils {
    private static final String ROOT_TMP_DIR = FileCacheUtils.class.getClassLoader().getResource("")
            .getPath() + "\\static";

    private final String localPath;

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

    @SneakyThrows
    public File saveFile(File file) {
        return saveFile(new FileInputStream(file), file.getName());
    }

    public File saveFile(InputStream file, String fileName) {
        // 复制文件
        File targetFile = new File(SomeUtils.getPath() + "\\ph\\" + fileName, "");
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
        log.info("fileName=====>{}",filename);
        var file = buildFilenameWithPath(filename);
        try (InputStream in = url.openStream();) {
            saveFile(in, filename);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return file;
    }


    public File getFile(String filename) {
        var file = new File(localPath + filename);
        if (!file.canRead()) {
            log.error("当前FileCache获取到文件为空！ filename: {}", file.getAbsolutePath());
            return null;
        }
        return file;
    }

    /**
     * @param filename 返回当前设置的文件路径加文件名组成的文件
     * @return
     */
    private File buildFilenameWithPath(String filename) {
        log.info("fileName=====>{}",filename);
        log.info("buildFilenameWithPath======>{}", SomeUtils.getPath() +"\\ph\\"+ filename);
        return new File(SomeUtils.getPath() +"\\ph\\"+ filename);
    }
}
