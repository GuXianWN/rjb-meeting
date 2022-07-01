package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

public interface CheckFaceExistService {
    boolean hasFace(File file);

    boolean hasFace(String url);

}
