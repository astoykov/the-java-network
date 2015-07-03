package com.alesto.javanetwork.service;

import com.alesto.javanetwork.configuration.TestDataServiceConfiguration;
import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.repository.FollowsRepository;
import com.alesto.javanetwork.repository.TimelineRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(name = "testDataService", classes = {TestDataServiceConfiguration.class}),
})
public class DataExchangeServiceTest {


    @Inject
    private TimelineRepository timelineRepository;

    @Inject
    private FollowsRepository followsRepository;

    @Inject
    private DataExchangeService dataExchangeService;

    @Before
    public void setUp() {

    }

    /**
     * Tests posting and of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testNewTimelineMessage() throws Exception {
        List<Timeline> list = null;

        final Timeline testTimeline = new Timeline();
        testTimeline.setMessage("msg 1");
        testTimeline.setUsername("Alice");
        testTimeline.setCreated(new Date());

        dataExchangeService.postTimelineMessage(testTimeline);
        verify(timelineRepository, times(1)).saveAndFlush(any(Timeline.class));
    }

    /**
     * Test following a new user
     *
     * @throws Exception
     */
    @Test
    public void testReadTimelineMessage() throws Exception {

        final Timeline testTimeline = new Timeline();
        testTimeline.setMessage("msg 1");
        testTimeline.setUsername("Alice");
        testTimeline.setCreated(new Date());
        final List<Timeline> list = Arrays.asList(testTimeline);

        when(timelineRepository.findAllByUsername("Alice")).thenReturn(list);

        List<Timeline> result = dataExchangeService.getTimelineMessages("Alice");
        assertEquals(1, result.size());
    }

    /**
     * Tests posting and of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testFollowAUser() throws Exception {

        final Follows followUser = new Follows();
        followUser.setUsername("Alice");
        followUser.setFollowsUser("Bob");

        dataExchangeService.followUser(followUser);
        verify(followsRepository, times(1)).saveAndFlush(any(Follows.class));
    }

    /**
     * Tests reading finding which users are followed
     *
     * @throws Exception
     */
    @Test
    public void testFollowingUsers() throws Exception {
        final Follows followUser = new Follows();
        followUser.setUsername("Alice");
        followUser.setFollowsUser("Bob");
        followUser.setId("" + System.currentTimeMillis());
        followUser.setCreated(new Date());
        final List<Follows> followUserList = Arrays.asList(followUser);

        when(followsRepository.findAllByUsername("Alice")).thenReturn(followUserList);

        List<Follows> result = dataExchangeService.getFollowedUsers("Alice");
        assertEquals(1, result.size());
    }

    /**
     * Tests reading a wall
     *
     * @throws Exception
     */
    @Test
    public void testWall() throws Exception {
        final Follows followUser = new Follows();
        followUser.setUsername("Alice");
        followUser.setFollowsUser("Bob");
        followUser.setId("" + System.currentTimeMillis());
        followUser.setCreated(new Date(System.currentTimeMillis()+1000));

        final Follows followUser2 = new Follows();
        followUser2.setUsername("Alice");
        followUser2.setFollowsUser("Charlie");
        followUser2.setId("" + System.currentTimeMillis());
        followUser2.setCreated(new Date(System.currentTimeMillis()+2000));
        final List<Follows> followUserList = Arrays.asList(followUser,followUser2);

        when(followsRepository.findAllByUsername("Alice")).thenReturn(followUserList);

        final Timeline testTimeline = new Timeline();
        testTimeline.setMessage("msg 1");
        testTimeline.setUsername("Alice");
        testTimeline.setCreated(new Date(System.currentTimeMillis()+3000));

        final Timeline testTimeline2 = new Timeline();
        testTimeline2.setMessage("msg 2");
        testTimeline2.setUsername("Alice");
        testTimeline2.setCreated(new Date(System.currentTimeMillis()+4000));
        final List<Timeline> aliceTimeLine = Arrays.asList(testTimeline,testTimeline2);


        final Timeline testTimeline3 = new Timeline();
        testTimeline3.setMessage("msg 3");
        testTimeline3.setUsername("Bob");
        testTimeline3.setCreated(new Date(System.currentTimeMillis()+5000));

        final Timeline testTimeline4 = new Timeline();
        testTimeline4.setMessage("msg 4");
        testTimeline4.setUsername("Bob");
        testTimeline4.setCreated(new Date(System.currentTimeMillis()+6000));
        final List<Timeline> bobTimeLine = Arrays.asList(testTimeline3,testTimeline4);


        final Timeline testTimeline5 = new Timeline();
        testTimeline5.setMessage("msg 5");
        testTimeline5.setUsername("Charlie");
        testTimeline5.setCreated(new Date(System.currentTimeMillis()+7000));
        final List<Timeline> charlieTimeLine = Arrays.asList(testTimeline5);


        when(timelineRepository.findAllByUsername("Alice")).thenReturn(aliceTimeLine);
        when(timelineRepository.findAllByUsername("Bob")).thenReturn(bobTimeLine);
        when(timelineRepository.findAllByUsername("Charlie")).thenReturn(charlieTimeLine);

        List<Timeline> result = dataExchangeService.getWallMessages("Alice");
        assertEquals(5, result.size());

        //Charlie posted the latest message, so should come at the top
        assertEquals("Charlie", result.get(0).getUsername());

        //Alice posted the first message, so should come at the bottom
        assertEquals("Alice", result.get(4).getUsername());
    }

}
