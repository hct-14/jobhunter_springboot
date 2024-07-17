package job_hunter.hct_14.controller;


import job_hunter.hct_14.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Value("${hct14.upload-file.base-uri}")
    private String baseURI;
       private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public String upload(@RequestParam("file")MultipartFile file,  @RequestParam("folder") String folder) throws URISyntaxException, IOException {
        //valid

        //creat a  directory

        this.fileService.createDirectory(baseURI + folder);

        //store file
        this.fileService.store(file, folder);
        return file.getOriginalFilename() + folder;

    }
}
