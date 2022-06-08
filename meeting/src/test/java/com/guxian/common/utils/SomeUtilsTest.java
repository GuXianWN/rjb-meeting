package com.guxian.common.utils;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class SomeUtilsTest {

    @Test
    void testAddStringToArray() {
        assertThat(SomeUtils.addStringToArrayBetween("-",
                List.of(
                        "1",
                        "2",
                        "3"
                ).toArray(new String[0]))).isEqualTo("1-2-3");
    }

    @Test
    void testRandomString() {
        String actual = SomeUtils.randomString(10);
        log.info(actual);
        assertThat(actual).hasSize(10);
    }


}
