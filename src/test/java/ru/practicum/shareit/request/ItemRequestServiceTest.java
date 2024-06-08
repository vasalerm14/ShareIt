package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestService requestService;

    private final User requestor = new User(2, "user2", "user2@mail.ru");
    private final User user = new User(1, "User", "user@mail.ru");
    private final ItemRequest request = new ItemRequest(1, "description", requestor, LocalDateTime.now());
    private final ItemRequest requestSecond = new ItemRequest(2, "2", user, LocalDateTime.now());
    private final Item item = new Item(1, "item", "cool", true, user, request);
    private final Item itemSecond = new Item(2, "i2", "2", true, requestor, requestSecond);

    @Test
    void saveNewRequest() {
        when(userRepository.findById(2)).thenReturn(Optional.of(requestor));
        when(requestRepository.save(any())).thenReturn(request);

        final ItemRequestDtoOut actualRequest = requestService.saveNewRequest(
                new ItemRequestDtoIn("description"), 2);

        Assertions.assertEquals(ItemRequestMapper.toItemRequestDtoOut(request), actualRequest);
    }
    @Test
    void getRequestsByRequestor_whenUserNotFound_thenThrownException() {
        when((userRepository).findById(3)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                requestService.getRequestsByRequestor(3));
    }

    @Test
    void getAllRequests_whenCorrectPageArguments_thenReturnRequests() {
        when(userRepository.findById(2)).thenReturn(Optional.of(requestor));
        when(requestRepository.findAllByRequestorIdIsNot(anyInt(), any())).thenReturn(List.of(requestSecond));
        when(itemRepository.findAllByRequestIdIn(anyList())).thenReturn(List.of(itemSecond));

        final ItemRequestDtoOut requestDtoOut = ItemRequestMapper.toItemRequestDtoOut(requestSecond);
        requestDtoOut.setItems(List.of(ItemMapper.toItemDtoOut(itemSecond)));

        List<ItemRequestDtoOut> actualRequests = requestService.getAllRequests(0, 10, 2);

        Assertions.assertEquals(List.of(requestDtoOut), actualRequests);
    }



    @Test
    void getRequestById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyInt())).thenReturn(Optional.of(request));
        when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(item));
        final ItemRequestDtoOut requestDto = ItemRequestMapper.toItemRequestDtoOut(request);
        requestDto.setItems(List.of(ItemMapper.toItemDtoOut(item)));

        ItemRequestDtoOut actualRequest = requestService.getRequestById(1, 1);

        Assertions.assertEquals(requestDto, actualRequest);
    }
}