package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.IllegalVewAndUpdateException;
import ru.practicum.shareit.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.exception.NotAvailableToBookOwnItemsException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.WrongDatesException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, int userId) {
        User booker = getUser(userId);
        Item item = getItem(bookingDtoIn.getItemId());
        if (!item.getAvailable()) {
            throw new ItemIsNotAvailableException("Вещь недоступна для брони");
        }
        if (booker.getId() == item.getOwner().getId()) {
            throw new NotAvailableToBookOwnItemsException("Нельзя забронировать свою вещь");
        }
        if (!bookingDtoIn.getEnd().isAfter(bookingDtoIn.getStart()) ||
                bookingDtoIn.getStart().isBefore(LocalDateTime.now())) {
            throw new WrongDatesException("Дата начала бронирования должна быть раньше даты возврата");
        }

        List<Booking> existingBookings = bookingRepository.findOverlappingBookings(
                item.getId(), bookingDtoIn.getStart(), bookingDtoIn.getEnd());
        if (!existingBookings.isEmpty()) {
            throw new ItemIsNotAvailableException("Бронь уже существует");
        }


        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        bookingRepository.save(BookingMapper.toBooking(bookingDtoIn, booking));
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    public BookingDtoOut approve(int bookingId, Boolean isApproved, int userId) {
        User owner = getUser(userId);
        Booking booking = getById(bookingId);
        Item item = booking.getItem();
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ItemIsNotAvailableException("Вещь уже забронирована");
        }
        if (owner.getId() != item.getOwner().getId()) {
            throw new IllegalVewAndUpdateException("Подтвердить бронирование может только собственник вещи");
        }
        BookingStatus newBookingStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newBookingStatus);
        return BookingMapper.toBookingDtoOut(booking);
    }


    @Transactional(readOnly = true)
    @Override
    public BookingDtoOut getBookingById(int bookingId, int userId) {
        Booking booking = getById(bookingId);
        User booker = booking.getBooker();
        Item item = booking.getItem();
        User owner = getUser(item.getOwner().getId());
        if (booker.getId() != userId && owner.getId() != userId) {
            throw new IllegalVewAndUpdateException("Только автор или владелец может просматривать данное бронирование");
        }
        return BookingMapper.toBookingDtoOut(booking);
    }


    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoOut> getAllByBooker(String state, int bookerId) {
        User booker = getUser(bookerId);
        List<Booking> bookings;
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);

        }
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(booker.getId(), Sort.by(DESC, "start"));
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStateCurrent(booker.getId(),
                        Sort.by(DESC, "start"));
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndStatePast(booker.getId(),
                        Sort.by(DESC, "start"));
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStateFuture(booker.getId(),
                        Sort.by(DESC, "start"));
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                        BookingStatus.WAITING, Sort.by(DESC, "start"));
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                        BookingStatus.REJECTED, Sort.by(DESC, "end"));
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoOut> getAllByOwner(int ownerId, String state) {
        User owner = getUser(ownerId);
        List<Booking> bookings;
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");

        }
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerId(owner.getId(),
                        Sort.by(DESC, "start"));
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStateCurrent(owner.getId(),
                        Sort.by(DESC, "start"));
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndStatePast(owner.getId(),
                        Sort.by(DESC, "start"));
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStateFuture(owner.getId(),
                        Sort.by(DESC, "start"));
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                        BookingStatus.WAITING, Sort.by(DESC, "start"));
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                        BookingStatus.REJECTED, Sort.by(DESC, "start"));
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Booking getById(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Booking.class)));
    }

    private User getUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
    }

    private Item getItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
    }
}