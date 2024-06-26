package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotBookerException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoShort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemService itemService;

    private final int id = 1;
    private final User user = new User(id, "User", "user@mail.ru");
    private final User notOwner = new User(2, "User2", "user2@mail.ru");
    private final ItemDtoIn itemDtoIn = new ItemDtoIn("item", "cool item", true, null);
    private final ItemDtoOut itemDtoOut = new ItemDtoOut(id, "item", "cool item", true,
            new UserDtoShort(id, "User"));
    private final Item item = new Item(id, "item", "cool item", true, user, null);
    private final CommentDtoOut commentDto = new CommentDtoOut(id, "abc", "User",
            LocalDateTime.of(2023, 7, 1, 12, 12, 12));
    private final Comment comment = new Comment(id, "abc", item, user,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12));
    private final Booking booking = new Booking(id, null, null, item, user, BookingStatus.WAITING);

    @Test
    void saveNewItem_whenUserFound_thenSavedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDtoOut actualItemDto = itemService.saveNewItem(itemDtoIn, id);

        Assertions.assertEquals(ItemMapper.toItemDtoOut(item), actualItemDto);
        Assertions.assertNull(item.getRequest());
    }

    @Test
    void saveNewItem_whenUserNotFound_thenNotSavedItem() {
        when((userRepository).findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.saveNewItem(itemDtoIn, 2));
    }

    @Test
    void saveNewItem_whenNoName_thenNotSavedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doThrow(DataIntegrityViolationException.class).when(itemRepository).save(any(Item.class));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemService.saveNewItem(itemDtoIn, id));
    }

    @Test
    void updateItem_whenUserIsOwner_thenUpdatedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDtoOut actualItemDto = itemService.updateItem(id, itemDtoIn, id);

        Assertions.assertEquals(itemDtoOut, actualItemDto);
    }

    @Test
    void updateItem_whenUserNotOwner_thenNotUpdatedItem() {
        when(userRepository.findById(2)).thenReturn(Optional.of(notOwner));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotOwnerException.class, () -> itemService.updateItem(id, itemDtoIn, 2));
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        when(bookingRepository.findFirstByItemIdAndStartLessThanEqualAndStatus(anyInt(), any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(anyInt(), any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(id)).thenReturn(List.of(comment));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        final ItemDtoOut itemDto = ItemMapper.toItemDtoOut(item);
        itemDto.setLastBooking(BookingMapper.toBookingDtoShort(booking));
        itemDto.setNextBooking(BookingMapper.toBookingDtoShort(booking));
        itemDto.setComments(List.of(CommentMapper.toCommentDtoOut(comment)));

        ItemDtoOut actualItemDto = itemService.getItemById(id, id);

        Assertions.assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemById_whenItemNotFound_thenExceptionThrown() {
        when((itemRepository).findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(2, id));
    }

    @Test
    void getItemsByOwner_CorrectArgumentsForPaging_thenReturnItems() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyInt(), any())).thenReturn(List.of(item));

        List<ItemDtoOut> targetItems = itemService.getItemsByOwner(0, 10, id);

        Assertions.assertNotNull(targetItems);
        Assertions.assertEquals(1, targetItems.size());
        verify(itemRepository, times(1))
                .findAllByOwnerId(anyInt(), any());
    }

    @Test
    void getItemBySearch_whenTextNotBlank_thenReturnItems() {
        when(itemRepository.search(any(), any())).thenReturn(List.of(item));

        List<ItemDtoOut> targetItems = itemService.getItemBySearch(0, 10, "abc");

        Assertions.assertNotNull(targetItems);
        Assertions.assertEquals(1, targetItems.size());
        verify(itemRepository, times(1))
                .search(any(), any());
    }

    @Test
    void getItemBySearch_whenTextIsBlank_thenReturnEmptyList() {
        List<ItemDtoOut> targetItems = itemService.getItemBySearch(0, 10, "");

        Assertions.assertTrue(targetItems.isEmpty());
        Assertions.assertEquals(0, targetItems.size());
        verify(itemRepository, never()).search(any(), any());
    }

    @Test
    void saveNewComment_whenUserWasBooker_thenSavedComment() {
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(anyInt(), anyInt(), any()))
                .thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(commentRepository.save(any())).thenReturn(comment);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        CommentDtoOut actualComment = itemService.saveNewComment(id, new CommentDtoIn("abc"), id);

        Assertions.assertEquals(commentDto, actualComment);
    }

    @Test
    void saveNewComment_whenUserWasNotBooker_thenThrownException() {
        when((bookingRepository).existsByBookerIdAndItemIdAndEndBefore(anyInt(), anyInt(), any())).thenReturn(false);

        Assertions.assertThrows(NotBookerException.class, () ->
                itemService.saveNewComment(2, new CommentDtoIn("abc"), id));
    }

    @Test
    void updateItem_whenItemNotFound_thenNotUpdatedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(2, itemDtoIn, id));
    }

    @Test
    void getItemsByOwner_whenUserNotFound_thenExceptionThrown() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.getItemsByOwner(0, 10, 2));
        verify(itemRepository, never()).findAllByOwnerId(anyInt(), any());
    }



}