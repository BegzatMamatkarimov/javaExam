    package com.arbek.service;

    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;

    @Service
    public class FileServiceImpl implements FileService{
        @Override
        public String uploadFile(String path, MultipartFile file) throws IOException {
            String fileName = file.getOriginalFilename();

            String filePath = path + File.separator + fileName;

            File f = new File(path);
            if(!f.exists()){
                f.mkdir();
            }

            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }

        @Override
        public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
            String filePath = path + File.separator + fileName;
            return new FileInputStream(filePath);
        }

        public boolean deleteFile(String path, String fileName) {
            File file = new File(path + fileName);
            return file.delete();
        }
    }

