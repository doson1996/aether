package com.ds.aether.server.service.impl;

import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/4/17
 * @description
 */
@Slf4j
@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Override
    public Result<String> add(AddJobParam param) {
        return null;
    }

}
