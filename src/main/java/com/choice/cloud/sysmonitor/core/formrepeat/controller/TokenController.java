package com.choice.cloud.sysmonitor.core.formrepeat.controller;


import com.choice.cloud.sysmonitor.common.util.DmAssert;
import com.choice.cloud.sysmonitor.common.util.UUIDGenerator;
import com.choice.cloud.sysmonitor.core.bean.ResponseData;
import com.choice.cloud.sysmonitor.core.constant.CommonConstant;
import com.choice.cloud.sysmonitor.core.constant.RedisKeyUtil;
import com.choice.cloud.sysmonitor.core.distributlock.DistributedLockCallback;
import com.choice.cloud.sysmonitor.core.distributlock.DistributedLockTemplate;
import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;
import com.choice.cloud.sysmonitor.core.formrepeat.constant.FormRepeatSubmitConstant;
import com.choice.cloud.sysmonitor.core.formrepeat.dto.Token;
import com.choice.cloud.sysmonitor.intergration.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaojufei
 */
@Slf4j
@RestController
@RequestMapping("/sys-monitor/token")
public class TokenController {
    @Autowired
    private DistributedLockTemplate distributedLockTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取token接口
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseData getToken(@RequestAttribute(CommonConstant.USER) UserDTO userDTO) {

        DmAssert.notBlank(userDTO.getTenantId(), ResponseCodeEnum.USER_TENANT_ID_EMPTY);
        DmAssert.notBlank(userDTO.getId(), ResponseCodeEnum.USER_ID_EMPTY);

        log.info("{}获取页面防重复提交token处理", userDTO);

        // 锁住用户，防止同一时间用一个账号大量请求token
        return distributedLockTemplate.tryLock(new DistributedLockCallback<ResponseData>() {
            @Override
            public ResponseData process() {
                String canGetToken = RedisKeyUtil.getUserFormOpsIsLockedLockKey(userDTO.getTenantId(),
                        userDTO.getId());

                // 如果已经无法获取token，提示出剩余时间
                if (redisTemplate.hasKey(canGetToken)) {
                    long remainTime = redisTemplate.getExpire(canGetToken, TimeUnit.MINUTES);
                    ResponseData messageBody = ResponseData.getResponseData(ResponseCodeEnum.USER_TOKEN_LIMIT, "您页面无效操作次数过多，请" + remainTime +
                                    "分钟后再操作",
                            remainTime);
                    return messageBody;
                }

                // 用户的TOKEN集合KEY
                String key = RedisKeyUtil.getUserFormOpsTokenMapLockKey(userDTO.getTenantId(), userDTO.getId());

                // 生成一个Token
                String tokenString = UUIDGenerator.getUUID();
                Token token = new Token(tokenString, new Date());

                long mapSize = redisTemplate.opsForHash().size(key);
                log.info("{}目前有{}个值", key, mapSize);

                // 如果是最后一个
                if ((mapSize + 1) == FormRepeatSubmitConstant.MAX_TOKEN_NUM) {
                    redisTemplate.opsForValue().set(canGetToken, tokenString,
                            FormRepeatSubmitConstant.TOKEN_OUTNUMBER_WAITTIME, TimeUnit.SECONDS);
                    // 删除Map的key，下次重新计数
                    redisTemplate.delete(key);
                }

                // 获取的最后一个保持可用
                redisTemplate.opsForHash().put(key, tokenString, token);
                redisTemplate.expire(key, FormRepeatSubmitConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

                return ResponseData.getSuccessResData(token);
            }

            @Override
            public String getLockName() {
                return RedisKeyUtil.getUserFormOpsLockKey(userDTO.getTenantId(), userDTO.getId());
            }
        }, true);
    }
}
