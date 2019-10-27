package com.zip.example.demo.controller;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author jacksparrow414
 * @Date 2019-10-27
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/uploadFileToZip/")
public class ZipCompressController {

    /**
     * 上传多张图片并压缩成Zip文件
     * @param multipartFiles
     * @return
     */
    @RequestMapping(value = "uploadPictures")
    public String uploadPictures(MultipartFile[] multipartFiles) throws Exception {
        File file = new File("picture.zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
        for (MultipartFile commonsMultipartFile : multipartFiles) {
            ZipEntry zipEntry = new ZipEntry(commonsMultipartFile.getOriginalFilename());
            zipOutputStream.putNextEntry(zipEntry);
            IOUtils.copy(commonsMultipartFile.getInputStream(),zipOutputStream);
        }
        zipOutputStream.close();
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        URI uri = new URIBuilder().setScheme("http").setHost("127.0.0.1").setPort(8090).setPath("/zip/unCompress/receiveZip").build();
        HttpPost httpPost = new HttpPost(uri);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file",file);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
        if (closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            return "发送请求成功";
        }
        return "上传图片失败";
    }
}
