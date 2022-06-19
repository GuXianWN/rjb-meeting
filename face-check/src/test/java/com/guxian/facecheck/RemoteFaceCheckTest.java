package com.guxian.facecheck;

import com.alibaba.cloud.commons.io.Charsets;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RemoteFaceCheckTest {
    /**
     * 发送请求 url: https://api.cloud.tencent.com/requesttc3 方式:POST
     * 请求头: Content-Type: application/json
     */
    @Test
    public void test() throws IOException, InterruptedException, URISyntaxException {
        String url = "https://api.cloud.tencent.com/requesttc3";
        String cookie = " qcloud_uid=1e08219cc9bc07e82f09fbfce630477c%40devS; intl_sid=17c1f906a1a4ee189; AMCV_F2C33DDA5A3918ED0A495C08%40AdobeOrg=-1891778711%7CMCIDTS%7C18912%7CMCMID%7C80112075338854320193899994734551352067%7CMCAID%7CNONE%7CMCOPTOUT-1633942327s%7CNONE%7CvVersion%7C2.4.0; pgv_pvid=2105010060; UM_distinctid=17e4a2ae3224ad-02fa4c83c3b459-59191353-14ac90-17e4a2ae323403; pgv_pvi=3594273792; _ga=GA1.2.738579607.1636866415; _ga_NM5TEZ9LY8=GS1.1.1643681844.3.0.1643681844.0; intl_language=zh; language=zh; lastLoginType=wx; web_uid=b4bfafb7-a395-443e-91d7-5781552842b9; qcmainCSRFToken=ryZ-_DArt9; qcloud_visitId=4b40d43d7314445a42355f2993db3e3d; qcloud_from=qcloud.google.seo-1655456281161; uin=o100020974377; tinyid=144115262795785717; skey=cTCLlTIH-9NkArb9bZC-l2TMkac-bzHAVIBIUkH3aNU_; loginType=wx; regionId=4; nick=%E9%9B%A8%E8%BE%B0%20%E5%96%B5; appid=1307207519; intl=1; moduleId=1307207519; opc_xsrf=089d9fbd4fc1ab95343d950b3acbff84%7C1655647496; saas_synced_session=100020974377%7CcTCLlTIH-9NkArb9bZC-l2TMkac-bzHAVIBIUkH3aNU_; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22100020974377%22%2C%22first_id%22%3A%221e08219cc9bc07e82f09fbfce630477c%40devS%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217bbe4c9bbf1b0-074d89286fc836-5734174f-1098042-17bbe4c9bc0b1d%22%7D; systemTimeGap=54098; ownerUin=O100020974377G";
        String contentType = "application/json";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        //从resources目录下读取200-ok-remote-check.json文件
        String json = Resources.toString(Resources.getResource("face-check/request/200-ok-remote-check.json"), Charsets.UTF_8);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest
                        .BodyPublishers.ofString("JsonData:" + json))
                .header("Content-Type", contentType)
                .header("cookie", cookie)
                .header("user-agent", userAgent)
                .build();

        //send request
        var response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

}
