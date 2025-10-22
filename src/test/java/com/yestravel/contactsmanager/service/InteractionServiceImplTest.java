package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.*;
import com.yestravel.contactsmanager.repo.InteractionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractionServiceImplTest {

    @Mock
    private InteractionRepo interactionRepo;

    @InjectMocks
    private InteractionServiceImpl interactionService;

    private Interaction testInteraction;

    @BeforeEach
    void setUp() {

        testInteraction = new Interaction();
        testInteraction.setId(1L);
        testInteraction.setType(InteractionType.CALL);
        testInteraction.setDate(LocalDateTime.now());
        testInteraction.setDescription("Test interaction");
        testInteraction.setContact(new Contact());
        testInteraction.setUser(new User());
    }

    @Test
    void CreateNewInteraction_shouldSaveAndReturnInteraction_whenValid() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);

        interactionService.saveInteraction(testInteraction);

        verify(interactionRepo, times(1)).save(testInteraction);
        assertEquals(InteractionType.CALL, testInteraction.getType());
    }

    @Test
    void deleteInteraction() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);

        interactionService.saveInteraction(testInteraction);

        interactionService.deleteInteraction(testInteraction);
        assert interactionService.findInteractionById(testInteraction.getId()) == null;
        verify(interactionRepo, times(1)).delete(testInteraction);
    }

    @Test
    void findInteractionById() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);
        when(interactionRepo.findById(1L)).thenReturn(Optional.of(testInteraction));

        interactionService.saveInteraction(testInteraction);

        Interaction found = interactionService.findInteractionById(1L);

        assertEquals(testInteraction, found);
        verify(interactionRepo, times(1)).findById(1L);
        verifyNoMoreInteractions(interactionRepo);

    }

    @Test
    void findAllInteractions() {
    }

    @Test
    void findAllInteractionsWithContactId() {
    }

    @Test
    void editInteraction() {
    }
}