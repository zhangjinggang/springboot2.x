package com.choice.cloud.sysmonitor.biz.remind.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.choice.cloud.sysmonitor.biz.remind.MnRemindConfService;
import com.choice.cloud.sysmonitor.core.bean.ResponseData;
import com.choice.cloud.sysmonitor.core.distributlock.DistributedLock;
import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;
import com.choice.cloud.sysmonitor.core.properties.BasicProperties;
import com.choice.cloud.sysmonitor.core.properties.MailRemindTaskProperties;
import com.choice.cloud.sysmonitor.domain.dto.EmailParam;
import com.choice.cloud.sysmonitor.domain.dto.PageMnRemindConfDto;
import com.choice.cloud.sysmonitor.domain.entity.remind.MnRemindConf;
import com.choice.cloud.sysmonitor.intergration.dto.client.MessageCenterFeign;
import com.choice.cloud.sysmonitor.mapper.remind.MnRemindConfMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;

/**
 * 邮件提醒配置表(MnRemindConf)表服务实现类
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:17:10
 */
@Slf4j
@Service
public class MnRemindConfServiceImpl implements MnRemindConfService {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MnRemindConfMapper mnRemindConfMapper;

    @Autowired
    private MessageCenterFeign messageCenterFeign;

    @Autowired
    private BasicProperties basicProperties;

    @Autowired
    private MailRemindTaskProperties mailRemindTaskProperties;

    /**
     * 启动定时任务，查询条件为空，全量发送执行
     */
    @Async
    @Override
    @DistributedLock(waitTime = 0, notLockRes = ResponseCodeEnum.NOT_GET_LOCK_TASK)
    @Transactional(rollbackFor = Exception.class)
    public void startTask(String lockKey) {
        log.info("开始执行邮件提醒定时任务");

        // 从第一页开始，分页查询配置，然后发送邮件
        for (int page = 1; true; page++) {
            PageMnRemindConfDto pageMnRemindConfDto = new PageMnRemindConfDto();
            pageMnRemindConfDto.setCurrent(page);
            List<MnRemindConf> mnRemindConfList = mnRemindConfMapper.queryAllByLimit(pageMnRemindConfDto);
            // 如果查詢不到配置了，結束
            if (CollectionUtils.isEmpty(mnRemindConfList)) {
                break;
            }
            sendMail(mnRemindConfList, pageMnRemindConfDto);
        }

        log.info("结束执行邮件提醒定时任务");
    }


    /**
     * 接收一个参数Dto执行定时任务，可以控制查询条件，覆盖发送邮箱地址
     */
    @Async
    @Override
    @DistributedLock(waitTime = 0, notLockRes = ResponseCodeEnum.NOT_GET_LOCK_TASK)
    @Transactional(rollbackFor = Exception.class)
    public void startTask(String lockKey, PageMnRemindConfDto pageMnRemindConfDto) {
        log.info("开始执行邮件提醒定时任务");

        // 从第一页开始，分页查询配置，然后发送邮件
        for (int page = 1; true; page++) {
            pageMnRemindConfDto.setCurrent(page);
            List<MnRemindConf> mnRemindConfList = mnRemindConfMapper.queryAllByLimit(pageMnRemindConfDto);
            // 如果查詢不到配置了，結束
            if (CollectionUtils.isEmpty(mnRemindConfList)) {
                break;
            }
            sendMail(mnRemindConfList, pageMnRemindConfDto);
        }

        log.info("结束执行邮件提醒定时任务");
    }


    /**
     * 执行发送邮件操作
     *
     * @param mnRemindConfList
     * @param pageMnRemindConfDto
     */
    private void sendMail(List<MnRemindConf> mnRemindConfList, PageMnRemindConfDto pageMnRemindConfDto) {
        String email = (pageMnRemindConfDto == null ? null : pageMnRemindConfDto.getReplaceMail());
        for (MnRemindConf mnRemindConf : mnRemindConfList) {
            // 如果参数中有指定邮箱地址，则不再使用配置项中的邮件地址
            List<String> emailList = Lists.newArrayList();
            if (StringUtils.isNotBlank(email)) {
                emailList.add(email);
            } else {
                emailList = mnRemindConf.getRecEmailList();
            }
            if (CollectionUtils.isNotEmpty(emailList)) {
                sendMail(emailList, mailRemindTaskProperties.getSubject(), mnRemindConf);
            } else {
                log.warn("当前提醒项没有收件人，不发送邮件，{}", JSON.toJSONString(pageMnRemindConfDto));
            }
        }
    }

    /**
     * 发送邮件
     *
     * @param emailList    收件邮箱地址
     * @param mnRemindConf 配置
     */
    private void sendMail(List<String> emailList, String subject, MnRemindConf mnRemindConf) {
        log.info("开始发送邮件：{}", JSON.toJSONString(mnRemindConf));
        ResponseData responseData = null;
        try {
            // Context是导这个包import org.thymeleaf.context.Context;
            Context context = new Context();
            EmailParam emailParam = new EmailParam();
            Date date = new Date();
            emailParam.setDate(DateUtil.formatDate(date));
            emailParam.setTime(DateUtil.formatTime(date));
            emailParam.setSysName(mnRemindConf.getConfName());
            // 定义模板数据
            context.setVariable("emailParam", emailParam);
            // 指定模板路径
            String emailContent = templateEngine.process("mail/mailRemind.html", context);
            // 调用消息中心发送邮件
            responseData = messageCenterFeign.sendEmail("Bearer " + basicProperties.getJwt(),
                    UUID.randomUUID().toString(),
                    emailList,
                    subject, true,
                    emailContent,
                    createExcel(mnRemindConf));
            // 处理过程异常
        } catch (Exception e) {
            log.error("发送邮件处理异常，message={}，error={}", e.getMessage(), e);
        } finally {
            log.info("发送URL文件={}，结果={}", mnRemindConf.getConfValue(), responseData);
        }
    }


    /**
     * 根据URL下载pdf文件，然后转化为File
     *
     * @param mnRemindConf 文件下载地址
     * @return
     */
    private MultipartFile[] createExcel(MnRemindConf mnRemindConf) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.MULTIPART_FORM_DATA));

        // 有些是需要密码的
        if (StringUtils.isNotBlank(mnRemindConf.getUserName()) && StringUtils.isNotBlank(mnRemindConf.getPassWord())) {
            String auth = mnRemindConf.getUserName() + ":" + mnRemindConf.getPassWord();
            byte[] authentication = auth.getBytes();
            byte[] base64Authentication = Base64Utils.encode(authentication);
            String baseCredential = new String(base64Authentication);
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + baseCredential);
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                mnRemindConf.getConfValue(),
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);
        byte[] result = response.getBody();

        StringBuilder fileName = new StringBuilder();
        fileName.append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN)).append(".pdf");

        MockMultipartFile excel = new MockMultipartFile("attachments", fileName.toString(), null, result);
        MultipartFile[] multipartFiles = new MultipartFile[]{excel};

        log.info("结束构造附件数据，当前时间：{}", DateUtil.formatDateTime(new Date()));
        return multipartFiles;
    }
}