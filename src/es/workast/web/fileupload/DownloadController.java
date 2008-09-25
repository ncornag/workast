package es.workast.web.fileupload;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.workast.model.activity.Activity;
import es.workast.model.activity.type.status.StatusActivityData;
import es.workast.service.activity.ActivityManager;
import es.workast.service.fileupload.FileManager;

@Controller
@RequestMapping("/data/download")
public class DownloadController {

    @Autowired
    ActivityManager activityManager;

    @Autowired
    FileManager fileManager;

    @RequestMapping("/attachment/{actId}/{attachId}")
    public void download(@PathVariable("actId") Long actId, @PathVariable("attachId") String attachId, HttpServletResponse response) throws Exception {

        Activity activity = activityManager.getActivity(actId);
        if (activity != null) { // FIXME: Throw an error if activity==null
            Map<String, StatusActivityData.Attachment> attachments = ((StatusActivityData) activity.getActivityData()).getAttachmentsAsMap();
            StatusActivityData.Attachment attachment = attachments.get(attachId);
            if (attachment != null && fileManager.getAttachment(attachId).exists()) { // FIXME: Throw an error 
                BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(fileManager.getAttachment(attachId)));

                response.setBufferSize((int) attachment.getSize());
                response.setContentType(attachment.getContentType());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getName() + "\"");
                response.setContentLength((int) attachment.getSize());

                FileCopyUtils.copy(fileStream, response.getOutputStream());
                fileStream.close();
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        }

    }
}