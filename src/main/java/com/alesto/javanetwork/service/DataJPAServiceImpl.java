package com.alesto.javanetwork.service;

import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.repository.FollowsRepository;
import com.alesto.javanetwork.repository.TimelineRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Alex on 01/07/2015.
 */
@Named("dataExchangeService")
public class DataJPAServiceImpl implements DataExchangeService{

    @Inject
    private FollowsRepository followsRepository;

    @Inject
    private TimelineRepository timelineRepository;

    @Override
    public Timeline postTimelineMessage(Timeline timeline) {
        return timelineRepository.saveAndFlush(timeline);
    }

    @Override
    public List<Timeline> getTimelineMessages(String username) {
        List<Timeline> resultTimelineList = timelineRepository.findAllByUsername(username).stream()
                .sorted((t1, t2) -> t2.getCreated().compareTo(t1.getCreated())).collect(Collectors.toList());

        System.out.println(resultTimelineList);
        return resultTimelineList;
    }

    @Override
    public List<Timeline> getWallMessages(String username) {

        //Adds the user himselft
        Follows myself = new Follows();
        myself.setUsername(username);
        myself.setFollowsUser(username);

        List<Follows> followedUsers  = Stream.concat(Arrays.asList(myself).stream(),
                followsRepository.findAllByUsername(username).stream())
                .collect(Collectors.toList());

        //Gets each followed user's timeline, then merges into one and sorts bu date
        List<Timeline> wallTimeline = followedUsers.stream()
                .map((Follows followedUser) -> getTimelineMessages(followedUser.getFollowsUser()))
                .collect(Collectors.toList()).stream()
                .flatMap(t -> t.stream()).sorted((t1, t2) -> t2.getCreated().compareTo(t1.getCreated())).collect(Collectors.toList());

        return wallTimeline;
    }



    @Override
    public Follows followUser(Follows followsUser) {
        return followsRepository.saveAndFlush(followsUser);
    }

    @Override
    public List<Follows> getFollowedUsers(String username) {
        return followsRepository.findAllByUsername(username);
    }
}
