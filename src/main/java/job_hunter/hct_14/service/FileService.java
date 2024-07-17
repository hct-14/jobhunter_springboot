package job_hunter.hct_14.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    @Value("${hct14.upload-file.base-uri}")
    private String baseURI;
    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File  tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory()){
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>CREATE NEW DIRECTORY SUCCESS, PATH = " + folder );
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            System.out.println(">> SKIP MAKINDIRECTORY< ALREADY EXISTS");
        }
    }
    public void store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String fileName = System.currentTimeMillis() + "." + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);

        }
    }
}
