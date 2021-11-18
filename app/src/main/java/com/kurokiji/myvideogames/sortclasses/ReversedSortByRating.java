package com.kurokiji.myvideogames.sortclasses;

import com.kurokiji.myvideogames.models.VideoGame;

import java.util.Comparator;

public class ReversedSortByRating implements Comparator<VideoGame> {
    @Override
    public int compare(VideoGame o1, VideoGame o2) {
        if (o1.review > o2.review) return 1;
        else if (o1.review == o2.review) return 0; //(o1 == o2)
        else return -1;
    }

}
