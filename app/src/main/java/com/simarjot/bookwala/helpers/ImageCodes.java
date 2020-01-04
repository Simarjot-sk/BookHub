package com.simarjot.bookwala.helpers;

public enum ImageCodes {
    Front(50, 51, 52),
    Back(60, 61, 62),
    Optional(70, 71, 72);

    public int GALLERY_CODE;
    public int CAMERA_CODE;
    public int CROP_CODE;

    ImageCodes(int gallery, int camera, int crop){
        GALLERY_CODE = gallery;
        CAMERA_CODE = camera;
        CROP_CODE = crop;
    }
}
