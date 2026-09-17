package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.AddressRequest;
import org.afonso.teamsync.entity.Address;
import org.afonso.teamsync.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repo;

    public List<Address> getAll() {
        return repo.findAll();
    }

    public Address getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }

    public Address create(AddressRequest request) {
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        return repo.save(address);
    }

    public Address update(UUID id, AddressRequest request) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (request.getStreet() != null) address.setStreet(request.getStreet());
        if (request.getCity() != null) address.setCity(request.getCity());
        if (request.getDistrict() != null) address.setDistrict(request.getDistrict());
        if (request.getPostalCode() != null) address.setPostalCode(request.getPostalCode());
        if (request.getCountry() != null) address.setCountry(request.getCountry());

        return repo.save(address);
    }

    public void delete(UUID id) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        repo.delete(address);
    }
}
