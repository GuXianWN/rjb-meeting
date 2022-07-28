package com.guxian.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> {
    Long page;
    Long size;
    Long total;
    List<T> data;

    public static <T> PageData<T> fromPage(Page<T> page) {
        return new PageData<T>()
                .setPage(page.getPages())
                .setSize(page.getSize())
                .setTotal(page.getTotal())
                .setData(page.getRecords());
    }

    public <E> PageData<E> editData(List<E> list) {
        return new PageData<E>()
                .setPage(this.getPage())
                .setSize(this.getSize())
                .setTotal(this.getTotal())
                .setData(list);
    }
}
