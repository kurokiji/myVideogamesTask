package com.kurokiji.myvideogames.sortclasses;

import com.kurokiji.myvideogames.models.VideoGame;

import java.util.Comparator;

public class SortByName implements Comparator<VideoGame> {
    @Override
    public int compare(VideoGame o1, VideoGame o2) {
        if (o1.name.charAt(0) > o2.name.charAt(0)) return 1;
        else if (o1.name.charAt(0) == o2.name.charAt(0)) return 0;
        else return -1;
    }
}
