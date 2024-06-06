package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
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
    List<Booking> findAllByBookerId(int bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end")
    List<Booking> findAllByBookerIdAndStateCurrent(int bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp > b.end")
    List<Booking> findAllByBookerIdAndStatePast(int brokerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp < b.start")
    List<Booking> findAllByBookerIdAndStateFuture(int bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, BookingStatus bookingStatus, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1")
    List<Booking> findAllByOwnerId(int ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end")
    List<Booking> findAllByOwnerIdAndStateCurrent(int ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp > b.end")
    List<Booking> findAllByOwnerIdAndStatePast(int ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp < b.start")
    List<Booking> findAllByOwnerIdAndStateFuture(int ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = ?2")
    List<Booking> findAllByOwnerIdAndStatus(int ownerId, BookingStatus bookingStatus, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartLessThanEqualAndStatus(int itemId, LocalDateTime localDateTime,
                                                                      BookingStatus bookingStatus, Sort end);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatus(int itemId, LocalDateTime localDateTime,
                                                              BookingStatus bookingStatus, Sort end);

    List<Booking> findByItemInAndStartLessThanEqualAndStatus(List<Item> items, LocalDateTime thisMoment,
                                                             BookingStatus approved, Sort end);

    List<Booking> findByItemInAndStartAfterAndStatus(List<Item> items, LocalDateTime thisMoment,
                                                     BookingStatus approved, Sort end);

    Boolean existsByBookerIdAndItemIdAndEndBefore(int bookerId, int itemId, LocalDateTime localDateTime);

}
