package com.alesto.javanetwork.web.controller.rest.v1;

import com.alesto.javanetwork.configuration.TestWebConfiguration;
import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.service.DataExchangeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name = "web", classes = {TestWebConfiguration.class})
})
public class RestTimelineControllerTest {

    @Inject
    private WebApplicationContext context;

    private MockMvc mvc;

    @Inject
    private DataExchangeService mockDataService;


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context). //
                build();

    }

    /**
     * Tests the posting and reading of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testNewTimelineMessage() throws Exception {
        final Timeline timelineToPost = new Timeline();
        timelineToPost.setMessage("msg 1");
        timelineToPost.setUsername("Alice");

        when(mockDataService.postTimelineMessage(timelineToPost)).thenReturn(timelineToPost);


        mvc.perform(post(ApiEndpoints.TIMELINE_INDEX + ".json", "Alice").param("message", "msg 1")).andExpect(status().isCreated());
        verify(mockDataService, times(1)).postTimelineMessage(any(Timeline.class));

    }

    /**
     * Tests the posting and reading of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testReadTimelineMessage() throws Exception {
        final Timeline testTimeline = new Timeline();
        testTimeline.setMessage("msg 1");
        testTimeline.setUsername("Alice");
        testTimeline.setId("" + System.currentTimeMillis());
        testTimeline.setCreated(new Date());
        final List<Timeline> timelineList = Arrays.asList(testTimeline);

        when(mockDataService.getTimelineMessages("Alice")).thenReturn(timelineList);

        mvc.perform(get(ApiEndpoints.API_INDEX + "/" + testTimeline.getUsername() + "/timeline.json").param("userId", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Alice")))
                .andExpect(jsonPath("$.content[0].message", is("msg 1")));

    }

    /**
     * Tests reading of wall message
     *
     * @throws Exception
     */
    @Test
    public void testReadWallMessages() throws Exception {
        final Timeline testTimeline = new Timeline();
        testTimeline.setMessage("msg 1");
        testTimeline.setUsername("Alice");
        testTimeline.setId("" + System.currentTimeMillis());
        testTimeline.setCreated(new Date());

        final Timeline testTimeline2 = new Timeline();
        testTimeline2.setMessage("msg 2");
        testTimeline2.setUsername("Bob");
        testTimeline2.setId("" + System.currentTimeMillis());
        testTimeline2.setCreated(new Date());


        final List<Timeline> timelineList = Arrays.asList(testTimeline,testTimeline2);

        when(mockDataService.getWallMessages("Alice")).thenReturn(timelineList);

        mvc.perform(get(ApiEndpoints.API_INDEX + "/" + testTimeline.getUsername() + "/wall.json").param("userId", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Alice")))
                .andExpect(jsonPath("$.content[0].message", is("msg 1")))
                .andExpect(jsonPath("$.content[1].username", is("Bob")))
                .andExpect(jsonPath("$.content[1].message", is("msg 2")));

    }
}
