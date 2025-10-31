package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.*;
import com.yestravel.contactsmanager.repo.InteractionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
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
    private Contact contact1 = new Contact();

    @BeforeEach
    void setUp() {


        testInteraction = new Interaction();
        testInteraction.setId(1L);
        testInteraction.setType(InteractionType.CALL);
        testInteraction.setDate(LocalDateTime.now());
        testInteraction.setDescription("Test interaction");
        testInteraction.setContact(contact1);
        testInteraction.setUser(new User());
    }

    @Test
    void CreateNewInteraction_shouldSaveAndReturnInteraction_whenValid() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);

        interactionService.createInteraction(testInteraction);

        verify(interactionRepo, times(1)).save(testInteraction);
        assertEquals(InteractionType.CALL, testInteraction.getType());
    }

    @Test
    void deleteInteraction() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);

        interactionService.createInteraction(testInteraction);

        interactionService.deleteInteraction(testInteraction);
        assert interactionService.findInteractionById(testInteraction.getId()) == null;
        verify(interactionRepo, times(1)).delete(testInteraction);
    }

    @Test
    void findInteractionById() {
        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);
        when(interactionRepo.findById(1L)).thenReturn(Optional.of(testInteraction));

        interactionService.createInteraction(testInteraction);

        Interaction found = interactionService.findInteractionById(1L);

        assertEquals(testInteraction, found);
        verify(interactionRepo, times(1)).findById(1L);
        verifyNoMoreInteractions(interactionRepo);

    }

    @Test
    void findAllInteractions() {

        Interaction testInteraction2 = new Interaction();
        testInteraction2.setId(2L);
        testInteraction2.setType(InteractionType.EMAIL);
        testInteraction2.setContact(contact1);

        when(interactionRepo.save(testInteraction)).thenReturn(testInteraction);
        when(interactionRepo.save(testInteraction2)).thenReturn(testInteraction2);
        when(interactionRepo.findAll()).thenReturn(List.of(testInteraction, testInteraction2));

        interactionService.createInteraction(testInteraction);
        interactionService.createInteraction(testInteraction2);

        List<Interaction> interactions = interactionService.findAllInteractions();

        assertEquals(2, interactions.size());
        assertEquals(InteractionType.EMAIL, interactions.get(1).getType());
        assertEquals(InteractionType.CALL, interactions.get(0).getType());
        assertEquals(2L, interactions.get(1).getId());
        assertEquals(1L, interactions.get(0).getId());
        verify(interactionRepo, times(1)).findAll();
        verifyNoMoreInteractions(interactionRepo);
    }

    @Test
    void findAllInteractionsWithContactId_shouldReturnOnlyMatchingInteractions() {

        Contact contactA = new Contact();
        contactA.setId(1L);

        Contact contactB = new Contact();
        contactB.setId(2L);

        Interaction interaction1 = new Interaction();
        interaction1.setId(1L);
        interaction1.setType(InteractionType.CALL);
        interaction1.setContact(contact1);

        Interaction interaction2 = new Interaction();
        interaction2.setId(2L);
        interaction2.setType(InteractionType.EMAIL);
        interaction2.setContact(contact1);

        Interaction interaction3 = new Interaction();
        interaction3.setId(3L);
        interaction3.setType(InteractionType.MEETING);
        interaction3.setContact(contactB);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Interaction> mockPage = new PageImpl<>(List.of(interaction1, interaction2), pageRequest, 2);

        when(interactionRepo.findAllInteractionsByContact_Id(1L, pageRequest))
                .thenReturn(mockPage);

        Page<Interaction> result = interactionService.findByContactId(1L, 0, 10);

        List<Interaction> interactions = result.toList();

        assertEquals(2, result.get().count());
        assertTrue(result.stream().allMatch(i -> i.getContact().getId().equals(1L)));
        assertEquals(InteractionType.CALL, interactions.get(0).getType());
        assertEquals(InteractionType.EMAIL, interactions.get(1).getType());
        assertEquals(1, interactions.get(0).getId());
        assertEquals(2, interactions.get(1).getId());
        verify(interactionRepo, times(1)).findAllInteractionsByContact_Id(1L, pageRequest);
        verifyNoMoreInteractions(interactionRepo);
    }

    @Test
    void editInteraction() {
    }
}