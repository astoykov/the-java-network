package com.alesto.javanetwork.service;

import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.entity.Timeline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Alex on 01/07/2015.
 */
public interface DataExchangeService {

    public List<Follows> getFollowedUsers(String username);

    public List<Timeline> getTimelineMessages(String username);

    public Timeline postTimelineMessage(Timeline timeline);

    public List<Timeline> getWallMessages(String username);

    public Follows followUser(Follows followsUser);
}
