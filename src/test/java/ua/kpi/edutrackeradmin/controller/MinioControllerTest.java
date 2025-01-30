package ua.kpi.edutrackeradmin.controller;

import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackeradmin.service.MinioService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MinioControllerTest {
    @Mock
    private MinioService minioService;
    @InjectMocks
    private MinioController minioController;

    @Test
    void getImageUrl() throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String imageName = "testImage.jpg";
        String expectedUrl = "http://example.com/testImage.jpg";

        when(minioService.getUrl(imageName)).thenReturn(expectedUrl);

        ResponseEntity<String> response = minioController.getImageUrl(imageName);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
        verify(minioService).getUrl(imageName);
    }

    @Test
    void download() throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "testFile.pdf";
        byte[] expectedData = "file content".getBytes();

        when(minioService.getPhoto(fileName)).thenReturn(expectedData);

        ResponseEntity<byte[]> response = minioController.download(fileName);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedData, response.getBody());
        verify(minioService).getPhoto(fileName);
    }
}