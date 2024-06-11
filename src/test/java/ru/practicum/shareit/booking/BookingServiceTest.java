package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingService bookingService;

    private final User user = new User(1, "User", "user@mail.ru");
    private final User booker = new User(2, "user2", "user2@mail.ru");
    private final Item item = new Item(1, "item", "cool", true, user, null);
    private final Booking booking = new Booking(1,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12),
            item, booker, BookingStatus.WAITING);
    private final BookingDtoIn bookingDtoIn = new BookingDtoIn(
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12), 1);
    private final BookingDtoIn bookingDtoInWrong = new BookingDtoIn(
            LocalDateTime.of(2023, 7, 2, 12, 12, 12),
            LocalDateTime.of(2023, 7, 1, 12, 12, 12), 1);
    private final BookingDtoIn bookingDtoWrongItem = new BookingDtoIn(
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12), 2);


    @Test
    void saveNewBooking_whenUserNotFound_thenThrownException() {
        when((userRepository).findById(3)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 3));
    }

    @Test
    void saveNewBooking_whenItemNotFound_thenThrownException() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when((itemRepository).findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.saveNewBooking(bookingDtoWrongItem, 2));
    }

    @Test
    void saveNewBooking_whenItemNotAvailable_thenThrownException() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        item.setAvailable(false);

        Assertions.assertThrows(ItemIsNotAvailableException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 2));
    }

    @Test
    void saveNewBooking_whenBookerIsOwner_thenThrownException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotAvailableToBookOwnItemsException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 1));
    }

    @Test
    void saveNewBooking_whenIncorrectDatesOfBooking_thenThrownException() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(WrongDatesException.class, () ->
                bookingService.saveNewBooking(bookingDtoInWrong, 2));
    }

    @Test
    void saveNewBooking_whenOwnerIsBooker_thenThrownException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotAvailableToBookOwnItemsException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 1));
    }

    @Test
    void approve() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        BookingDtoOut actualBooking = bookingService.approve(1, true, 1);

        Assertions.assertEquals(BookingStatus.APPROVED, actualBooking.getStatus());
    }

    @Test
    void approve_whenBookingNotFound_thenThrownException() {
        when((bookingRepository).findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.approve(2, true, 1));
    }

    @Test
    void approve_whenItemAlreadyBooked_thenThrownException() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatus.APPROVED);

        Assertions.assertThrows(ItemIsNotAvailableException.class, () ->
                bookingService.approve(1, true, 1));
    }

    @Test
    void getBookingById_whenUserIsOwner_thenReturnBooking() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        BookingDtoOut actualBooking = bookingService.getBookingById(1, 1);

        Assertions.assertEquals(BookingMapper.toBookingDtoOut(booking), actualBooking);
    }

    @Test
    void getBookingById_whenUserIsNotAuthorOrOwner_thenThrownException() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Assertions.assertThrows(IllegalVewAndUpdateException.class, () ->
                bookingService.getBookingById(1, 3));
    }

    @Test
    void getAllByBooker_whenStateAll_thenReturnAllBookings() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerId(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "ALL", 2);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateCurrent_thenReturnListOfBookings() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStateCurrent(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "CURRENT", 2);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStatePast_thenReturnListOfBookings() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatePast(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "PAST", 2);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateFuture_thenReturnListOfBookings() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStateFuture(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "FUTURE", 2);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateWaiting_thenReturnListOfBookings() {
        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatus(anyInt(), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "WAITING", 2);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateUnsupported_thenExceptionThrown() {
        Assertions.assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getAllByBooker(0, 10, "a", 2));
    }

    @Test
    void getAllByOwner_whenStateAll_thenReturnAllBookings() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerId(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "ALL", 1);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);

    }

    @Test
    void getAllByOwner_whenStateCurrent_thenReturnListOfBookings() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStateCurrent(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "CURRENT", 1);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStatePast_thenReturnListOfBookings() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStatePast(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "PAST", 1);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStateFuture_thenReturnListOfBookings() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStateFuture(anyInt(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "FUTURE", 1);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStateWaiting_thenReturnListOfBookings() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStatus(anyInt(), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "WAITING", 1);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void approve_whenBookingAlreadyRejected_thenThrownException() {
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        Assertions.assertThrows(ItemIsNotAvailableException.class, () ->
                bookingService.approve(1, true, 1));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenThrownException() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.getBookingById(1, 1));
    }

    @Test
    void getAllByBooker_whenStateUnsupported_thenThrownException() {
        Assertions.assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getAllByBooker(0, 10, "UNKNOWN", 2));
    }

    @Test
    void getAllByOwner_whenStateUnsupported_thenThrownException() {
        Assertions.assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getAllByOwner(0, 10, "UNKNOWN", 1));
    }

    @Test
    void saveNewBooking_whenStartDateBeforeNow_thenThrownException() {
        BookingDtoIn pastBookingDtoIn = new BookingDtoIn(
                LocalDateTime.of(2022, 7, 1, 12, 12, 12),
                LocalDateTime.of(2023, 7, 30, 12, 12, 12), 1);

        when(userRepository.findById(2)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(WrongDatesException.class, () ->
                bookingService.saveNewBooking(pastBookingDtoIn, 2));
    }







}