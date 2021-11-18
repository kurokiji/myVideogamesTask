package com.kurokiji.myvideogames.models;

import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;
/**
 * Representa la reseña de un videojuego
 *
 */
public class VideoGame {
    public String name;
    public String uriPath;
    public float review;
    public String platform;
    public int progress;
    public String format;

public VideoGame (String name, String uriPath, float review, String platform, int progress, String format) {
    this.name = name;
    this.uriPath = uriPath;
    this.review = review;
    this.platform = platform;
    this.progress = progress;
    this.format = format;
    }
}
