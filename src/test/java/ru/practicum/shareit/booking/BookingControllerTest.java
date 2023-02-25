package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.item.ItemDao;
import ru.practicum.shareit.utils.exception.AvailableFalseException;
import ru.practicum.shareit.utils.exception.NotOwnerException;
import ru.practicum.shareit.utils.exception.TimeFalseException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    private static String endpoint;

    private static Booking booking;

    private static BookingDao bookingDao;

    private static String jsonBooking;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        endpoint = "/bookings";

        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        var itemDao = ItemDao.builder()
                .id(1)
                .name("test")
                .build();

        bookingDao = BookingDao.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .item(itemDao)
                .booker(null)
                .status(Status.WAITING)
                .build();

        jsonBooking = "{" +
                "\"itemId\":1," +
                "\"start\": \"2024-01-20T12:57:49\"," +
                "\"end\": \"2024-01-21T12:57:00\"" +
                "}";
    }

    @Test
    void addBooking() {
        when(bookingService.addBooking(any())).thenReturn(bookingDao);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(bookingDao.getId())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1)).addBooking(any());
    }

    @Test
    void addBookingAvailable() {
        when(bookingService.addBooking(any())).thenThrow(AvailableFalseException.class);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1)).addBooking(any());
    }

    @Test
    void addBookingTimeError() {
        when(bookingService.addBooking(any())).thenThrow(TimeFalseException.class);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1)).addBooking(any());
    }

    @Test
    void updateBooking() {
        when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDao);

        try {
            mockMvc.perform(patch(endpoint + "/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("approved", "true")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(bookingDao.getId())))
                    .andExpect(jsonPath("$.status", is(bookingDao.getStatus().toString())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1)).updateBooking(anyInt(), anyInt(),  anyBoolean());
    }

    @Test
    void updateBookingOtherUser() {
        when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean()))
                .thenThrow(NotOwnerException.class);

        try {
            mockMvc.perform(patch(endpoint + "/1")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBooking() {
        when(bookingService.getBooking(anyInt(), anyInt())).thenReturn(bookingDao);

        try {
            mockMvc.perform(get(endpoint + "/1")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(bookingDao.getId())))
                    .andExpect(jsonPath("$.status", is(bookingDao.getStatus().toString())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1)).getBooking(anyInt(), anyInt());
    }

    @Test
    void getBookings() {
        when(bookingService.getAllBookingUser(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingDao));

        try {
            mockMvc.perform(get(endpoint)
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1))
                .getAllBookingUser(anyInt(), any(), anyInt(), anyInt());
    }

    @Test
    void getBookingsOwner() {
        when(bookingService.getAllBookingOwner(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingDao));

        try {
            mockMvc.perform(get(endpoint + "/owner")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1))
                .getAllBookingOwner(anyInt(), any(), anyInt(), anyInt());
    }

    @Test
    void getBookingsOwnerFromError() {
        try {
            mockMvc.perform(get(endpoint + "/owner")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("from", "-1")
                            .param("size", "1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBookingsOwnerSizeError() {
        try {
            mockMvc.perform(get(endpoint + "/owner")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("from", "0")
                            .param("size", "0")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBookingsOwnerStateError() {
        try {
            mockMvc.perform(get(endpoint + "/owner")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("state", "0")
                            .param("from", "0")
                            .param("size", "1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBookingsStateError() {
        try {
            mockMvc.perform(get(endpoint + "/owner")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("state", "0")
                            .param("from", "0")
                            .param("size", "1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeBooking() {
        when(bookingService.removeBooking(anyInt()))
            .thenReturn(bookingDao);

        try {
            mockMvc.perform(delete(endpoint + "/1")
                            .content(jsonBooking)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookingService, times(1))
                .removeBooking(anyInt());
    }
}