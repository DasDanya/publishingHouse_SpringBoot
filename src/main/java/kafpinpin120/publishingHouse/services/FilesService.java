package kafpinpin120.publishingHouse.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesService {
    public boolean isImage(MultipartFile file){
        String contentType = file.getContentType();

        return contentType != null && contentType.startsWith("image/");
    }

    public void saveImage(MultipartFile image, String destinationPath) throws IOException {

        byte[] imageAsBytes = image.getBytes();
        Path imagePath = Paths.get(destinationPath);

        Files.write(imagePath,imageAsBytes);
    }

    public byte[] getFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if(Files.exists(path)){
            return Files.readAllBytes(path);
        }else{
            return new byte[0];
        }
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}
