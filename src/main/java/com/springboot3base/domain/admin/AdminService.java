package com.springboot3base.domain.admin;

import com.springboot3base.common.exception.ProcessFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    @Transactional(readOnly = true)
    public List<String> getRoles() {
        throw new ProcessFailedException("missed payload");
    }

}
