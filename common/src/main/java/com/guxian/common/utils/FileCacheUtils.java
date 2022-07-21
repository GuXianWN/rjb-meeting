package com.guxian.common.utils;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public class FileCacheUtils {
    @SneakyThrows
    public File saveFaceFile(MultipartFile file, String fileName) {
        return saveFaceFile(file.getInputStream(), fileName);
    }

    @SneakyThrows
    public File saveFaceFile(File file) {
        return saveFaceFile(new FileInputStream(file), file.getName());
    }

    /**
     * 专门为保存图片编写，请勿用于其他用途
     *
     * @param file
     * @param fileName
     * @return file
     */
    public static File saveFaceFile(InputStream file, String fileName) {
        // 复制文件
        File targetFile = new File(SomeUtils.getPath() + "ph/" + fileName, "");
        try {
            FileUtils.writeByteArrayToFile(targetFile, file.readAllBytes());
            log.info("file save to  {}", SomeUtils.getPath() + "ph/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    public File getFaceFile(String fileName) {
        return new File(SomeUtils.getPath() + "ph/" + fileName, "");
    }

    @SneakyThrows
    public boolean deleteCache(File fileName) {
        var file = buildFilenameWithPath(fileName.getName());
        return file.canExecute() && Files.deleteIfExists(Path.of(file.getPath()));
    }

    public File saveFileFromRemote(URL url, String filename) {
        try {
            return saveFileFromRemote(url.openStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(BizCodeEnum.SERVER_ERROR);
        }
    }

    public File saveFileFromRemote(InputStream in, String filename) {
        return saveFaceFile(in, filename);
    }


    /**
     * @param filename 返回当前设置的文件路径加文件名组成的文件
     * @return
     */
    private  File buildFilenameWithPath(String filename) {
        log.info("buildFilenameWithPath======>{}", SomeUtils.getPath() + "ph/" + filename);
        return new File(SomeUtils.getPath() + "ph/" + filename);
    }
}
