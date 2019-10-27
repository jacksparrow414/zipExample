package com.zip.example.demo.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @Author jacksparrow414
 * @Date 2019-10-27
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/unCompress")
public class UnCompressController {

    @RequestMapping(value = "receiveZip")
    public String receiveZip(MultipartFile file) throws IOException {
        List<MultipartFile> list = new ArrayList<>();
        File receiveFile = new File("receive.zip");
        FileUtils.copyInputStreamToFile(file.getInputStream(),receiveFile);
        ZipFile zipFile = new ZipFile(receiveFile);
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        InputStream inputStream = null;
        ZipEntry entry = null;
        while ((entry =zipInputStream.getNextEntry()) != null){
           inputStream = zipFile.getInputStream(entry);
           MultipartFile multipartFile = new MockMultipartFile("file",entry.getName(),"image/jpg", FileCopyUtils.copyToByteArray(inputStream));
           list.add(multipartFile);
        }
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(zipInputStream);
        if (list.size()>0){
            return "解压成功";
        }
        return "解压缩失败";
    }
}
