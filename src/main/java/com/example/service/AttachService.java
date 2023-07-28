package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exp.AppBadRequestException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@Service
public class AttachService {
    @Value("${attach.folder.name}")
    private String folderName;

    @Value("{attach.url}")
    private String attachUrl;

    @Autowired
    AttachRepository attachRepository;

    public String saveToSystem(MultipartFile file) {
//        System.out.println(file.getSize());
//        System.out.println(file.getName());
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getContentType());
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public AttachDTO save(MultipartFile file) {
        String pathFolder = getYmDString(); // 2022/04/23
        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
        String extension = getExtension(file.getOriginalFilename()); // jpg
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            // attaches/2022/04/23/dasdasd-dasdasda-    asdasda-asdasd.jpg
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setId(key);
            entity.setPath(pathFolder); // 2022/04/23
            entity.setSize(file.getSize());
            entity.setOriginalName(file.getOriginalFilename());
            entity.setExtension(extension);
            attachRepository.save(entity);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(key);
            attachDTO.setOriginalName(entity.getOriginalName());
            // any think you want mazgi.
            attachDTO.setUrl(getUrl(entity.getId()));

            return attachDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] loadImage(String fileName) { // open image
            try {
                BufferedImage originalImage = ImageIO.read(new File("attaches/" + fileName));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "png", baos);

                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                return imageInByte;
            } catch (Exception e) {
                return new byte[0];
            }
        }

    public byte[] loadByIdGeneral(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            File file = new File(url);

            byte[] bytes = new byte[(int) file.length()];

            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] loadImageById(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            BufferedImage originalImage = ImageIO.read(new File(url));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, entity.getExtension(), baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public ResponseEntity<?> delete(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            File file = new File(url);
            // Check if the file exists before attempting to delete it
            if (file.exists()) {
                if (file.delete()) {
                    // Delete the entity from the repository
                    attachRepository.delete(entity);

                    // Return a success response
                    return ResponseEntity.ok("File with ID " + id + " has been deleted successfully.");
                } else {
                    // If the file couldn't be deleted, return an error response
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the file with ID " + id + ".");
                }
            } else {
                // If the file does not exist, return a not found response
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File with ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return an error response in case of any exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the file with ID " + id + ".");
        }
    }

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();

            Path file = Paths.get(url);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }




    public AttachDTO getAttachWithUrl(String id) {
        if (id == null) {
            return null;
        }
        AttachDTO dto = new AttachDTO();
        dto.setId(id);
        dto.setUrl(getUrl(id));
        return dto;
    }

    public String getUrl(String id) {
        return attachUrl + "/open/" + id + "/img";
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("File not found");
        });
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

}

