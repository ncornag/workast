package es.workast.web.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.workast.core.json.JsonResponse;
import es.workast.service.fileupload.FileManager;
import es.workast.service.fileupload.impl.FileManagerException;

/**
 * Controller class to upload Files.
 * 
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/fileupload")
public class FileUploadController {

    @Autowired
    FileManager fileManager;

    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {

        String managedFileKey = "";
        try {
            managedFileKey = fileManager.managePendingFile(file);
        } catch (FileManagerException e) {
            // FIXME Add error to the model; 
        }

        JsonResponse response = JsonResponse.ok();
        model.addAttribute("response", response);

        response.addData("id", managedFileKey);
        response.addData("name", file.getOriginalFilename());
        response.addData("size", Long.valueOf(file.getSize()).toString());

        response.setContentType("text/html;charset=ISO-8859-1");

        //response.setHeader("Cache-Control", "no-cache");
        return "json/response.json";
    }

}
