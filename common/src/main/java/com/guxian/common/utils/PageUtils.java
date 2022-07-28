package com.guxian.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;


@Data
@Accessors
@Component
public class PageUtils {
    Long page;
    Long size;

    public <T> Page<T> toPage(Class<T> t) {
        return new Page<T>(page,size);
    }

    public PageUtils() {
        this.page=1L;
        this.size=10L;
    }
}
