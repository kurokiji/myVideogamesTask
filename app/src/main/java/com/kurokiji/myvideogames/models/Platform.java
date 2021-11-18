package com.kurokiji.myvideogames.models;

import androidx.annotation.NonNull;

/**
 * Representa un objeto Platform que aparece como un elemento del spinner personalizado de
 * plataformas.
 *
 */
public class Platform {
    private String platformName;
    private int platformImage;

    public Platform(String platformName, int platformImage) {
        this.platformName = platformName;
        this.platformImage = platformImage;
    }

    public String getPlatformName() {
        return this.platformName;
    }

    public int getPlatformImage() {
        return this.platformImage;
    }

    @NonNull
    @Override
    public String toString() {
        return platformName;
    }
}
