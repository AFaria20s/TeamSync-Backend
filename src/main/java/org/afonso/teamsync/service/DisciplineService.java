package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.DisciplineRequest;
import org.afonso.teamsync.entity.Discipline;
import org.afonso.teamsync.repository.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineRepository repo;

    public List<Discipline> getAll() {
        return repo.findAll();
    }

    public Discipline getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
    }

    public Discipline create(DisciplineRequest request) {
        Discipline discipline = new Discipline();
        discipline.setName(request.getName());
        discipline.setDescription(request.getDescription());

        return repo.save(discipline);
    }

    public Discipline update(UUID id, DisciplineRequest request) {
        Discipline discipline = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));

        if (request.getName() != null) discipline.setName(request.getName());
        if (request.getDescription() != null) discipline.setDescription(request.getDescription());

        return repo.save(discipline);
    }

    public void delete(UUID id) {
        Discipline discipline = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        repo.delete(discipline);
    }
}
