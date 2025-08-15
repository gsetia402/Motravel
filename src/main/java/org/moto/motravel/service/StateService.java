package org.moto.motravel.service;

import org.moto.motravel.model.State;
import org.moto.motravel.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    /**
     * Get all states ordered by name
     */
    public List<State> getAllStates() {
        return stateRepository.findAllOrderByName();
    }

    /**
     * Get state by ID
     */
    public Optional<State> getStateById(Long id) {
        return stateRepository.findById(id);
    }

    /**
     * Get state by name
     */
    public Optional<State> getStateByName(String name) {
        return stateRepository.findByName(name);
    }

    /**
     * Create a new state
     */
    public State createState(State state) {
        if (stateRepository.existsByName(state.getName())) {
            throw new IllegalArgumentException("State with name '" + state.getName() + "' already exists");
        }
        return stateRepository.save(state);
    }

    /**
     * Update an existing state
     */
    public State updateState(Long id, State stateDetails) {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + id));

        // Check if the new name conflicts with existing states (excluding current state)
        if (!state.getName().equals(stateDetails.getName()) && 
            stateRepository.existsByName(stateDetails.getName())) {
            throw new IllegalArgumentException("State with name '" + stateDetails.getName() + "' already exists");
        }

        state.setName(stateDetails.getName());
        return stateRepository.save(state);
    }

    /**
     * Delete a state
     */
    public void deleteState(Long id) {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + id));

        // Check if state has associated hidden gems
        if (!state.getHiddenGems().isEmpty()) {
            throw new IllegalStateException("Cannot delete state with associated hidden gems. " +
                    "Please remove or reassign the hidden gems first.");
        }

        stateRepository.delete(state);
    }

    /**
     * Search states by name
     */
    public List<State> searchStatesByName(String searchTerm) {
        return stateRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    /**
     * Check if state exists by name
     */
    public boolean existsByName(String name) {
        return stateRepository.existsByName(name);
    }
}
