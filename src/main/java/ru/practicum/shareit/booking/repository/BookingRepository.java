package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerId(int bookerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end")
    List<Booking> findAllByBookerIdAndStateCurrent(int bookerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp > b.end")
    List<Booking> findAllByBookerIdAndStatePast(int brokerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp < b.start")
    List<Booking> findAllByBookerIdAndStateFuture(int bookerId, Sort start);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, BookingStatus bookingStatus, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1")
    List<Booking> findAllByOwnerId(int ownerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end")
    List<Booking> findAllByOwnerIdAndStateCurrent(int ownerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp > b.end")
    List<Booking> findAllByOwnerIdAndStatePast(int ownerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp < b.start")
    List<Booking> findAllByOwnerIdAndStateFuture(int ownerId, Sort start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = ?2")
    List<Booking> findAllByOwnerIdAndStatus(int ownerId, BookingStatus bookingStatus, Sort start);

    Optional<Booking> findFirstByItemIdAndStartLessThanEqualAndStatus(int itemId, LocalDateTime localDateTime,
                                                                      BookingStatus bookingStatus, Sort end);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatus(int itemId, LocalDateTime localDateTime,
                                                              BookingStatus bookingStatus, Sort end);

    List<Booking> findByItemInAndStartLessThanEqualAndStatus(List<Item> items, LocalDateTime thisMoment,
                                                             BookingStatus approved, Sort end);

    List<Booking> findByItemInAndStartAfterAndStatus(List<Item> items, LocalDateTime thisMoment,
                                                     BookingStatus approved, Sort end);

    Boolean existsByBookerIdAndItemIdAndEndBefore(int bookerId, int itemId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND (:start BETWEEN b.start AND b.end " +
            "OR :end BETWEEN b.start AND b.end)")
    List<Booking> findOverlappingBookings(int itemId,
                                          LocalDateTime start,
                                          LocalDateTime end);
}
