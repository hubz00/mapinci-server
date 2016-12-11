package mapinci.Coordinate;

import mapinci.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CoordinateControllerTest {


    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

//    @Test
//    public void getShape() throws Exception {
//        mockMvc.perform(get("/coordinate"))
//                .andExpect(status().isOk());
//    }

    @Test
    public void postShape() throws Exception {
        mockMvc.perform(post("/coordinate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"segments\": [], \"length\": 0 }"))
                .andExpect(status().isOk());
    }



}