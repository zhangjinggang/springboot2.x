package com.choice.cloud.sysmonitor.intergration.dto.client;

import com.choice.cloud.sysmonitor.core.bean.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 调用发送邮件接口
 * @author seal
 */
@FeignClient(name = "message-center")
public interface MessageCenterFeign {

    /**
     * 发送邮件
     *
     * @param emailId         emailId 用于追踪日志
     * @param receivers       收件人邮箱
     * @param subject         邮件主题
     * @param isHtml          是否是html
     * @param mailContent     邮件正文内容
     * @param attachmentFiles 附件
     * @return
     */
    @PostMapping(value = "/api/email/sendEmail", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseData sendEmail(@RequestHeader(org.springframework.http.HttpHeaders.AUTHORIZATION) String jwt,
                           @RequestParam("emailId") String emailId,
                           @RequestParam("receivers") List<String> receivers,
                           @RequestParam("subject") String subject,
                           @RequestParam("isHtml") Boolean isHtml,
                           @RequestParam("mailContent") String mailContent,
                           @RequestPart MultipartFile[] attachmentFiles
    );
}
