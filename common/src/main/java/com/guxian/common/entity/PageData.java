package com.guxian.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PageData {
    private Long page;
    private Long size;
    private Long total;
    private List<?> data;
}
