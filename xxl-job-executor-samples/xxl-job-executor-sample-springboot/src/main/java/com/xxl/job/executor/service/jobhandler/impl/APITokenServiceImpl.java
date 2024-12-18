package com.xxl.job.executor.service.jobhandler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxl.job.executor.mapper.APITokenMapper;
import com.xxl.job.executor.model.APIToken;
import com.xxl.job.executor.service.jobhandler.APITokenService;
import org.springframework.stereotype.Service;

@Service
public class APITokenServiceImpl extends ServiceImpl<APITokenMapper, APIToken> implements APITokenService {

}
