package com.springboot3base.common.service;

import com.springboot3base.common.exception.ProcessFailedException;
import com.springboot3base.common.model.FileEntity;
import com.springboot3base.common.model.repositories.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Component
public class FileUploadService {

    private final String STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/springboot-analysis.appspot.com/o/";
    private final String DEFAULT_PATH = "tmp/";
    private final FileRepository fileRepository;

    public FileUploadService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private String generatePath(String path, String fileName) {
        if (ObjectUtils.isEmpty(path))
            return DEFAULT_PATH + fileName;
        else
            return path + "/" + fileName;
    }

    private String generateUniqueFileName(String ext) {
        String filename = "";
        long millis = System.currentTimeMillis();
        String datetime = new Date().toString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        String rndchars = RandomStringUtils.randomAlphanumeric(16);
        filename = rndchars + "_" + datetime + "_" + millis + "." + ext;
        return filename;
    }

    /**
     *
     * @param path ex: tmp/product/id
     * @param file
     * @return
     * @throws IOException
     */
    public FileEntity uploadFile(String path, MultipartFile file) throws IOException {
        String filePath = generatePath(path, generateUniqueFileName(FilenameUtils.getExtension(file.getOriginalFilename())));
        /*
            Handle upload file at here
            filePath will look like this: tmp/product/1/444234235235.jpg
         */
        throw new ProcessFailedException("missed payload");
        // return fileRepository.saveAndFlush(new FileEntity(STORAGE_URL + filePath.replaceAll("/", "%2F") + "?alt=media", filePath));
    }
}
