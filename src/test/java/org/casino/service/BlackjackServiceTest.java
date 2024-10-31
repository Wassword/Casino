//package org.casino.service;
//
//import org.casino.models.*;
//import org.casino.models.interfaces.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class BlackjackServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private User user;
//
//    @Mock
//    private Dealer dealer;
//
//    @Mock
//    private Deck deck;
//
//    @InjectMocks
//    private BlackjackService blackjackService;
//
//    @BeforeEach
//    void setUp() {
//        Card card1 = new Card(Suits.HEART, Values.TEN);
//        Card card2 = new Card(Suits.DIAMOND, Values.ACE);
//        when(user.getHand()).thenReturn(Arrays.asList(card1, card2));
//    }
//
//    @Test
//    void testGetPlayerHand_WhenUserHandIsNotEmpty_ShouldReturnCardImagePaths() {
//        List<String> expectedImagePaths = Arrays.asList("imagePath1", "imagePath2");
//        List<String> actualImagePaths = blackjackService.getPlayerHand();
//        assertEquals(expectedImagePaths, actualImagePaths);
//    }
//
//    @Test
//    void testGetPlayerHand_WhenUserHandIsEmpty_ShouldThrowNoSuchElementException() {
//        when(user.getHand()).thenReturn(List.of());
//        assertThrows(NoSuchElementException.class, () -> blackjackService.getPlayerHand());
//    }
//}