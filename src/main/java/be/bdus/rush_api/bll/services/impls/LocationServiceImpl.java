package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.LocationService;
import be.bdus.rush_api.dal.repositories.LCompanyRepository;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.il.request.SearchParam;
import be.bdus.rush_api.il.specifications.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LCompanyRepository locationRepository;

    @Override
    public Page<LocationCompany> findAll(List<SearchParam<LocationCompany>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return locationRepository.findAll(pageable);
        }
        return locationRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public LocationCompany findById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No location found found"));
    }

    @Override
    public LocationCompany save(LocationCompany locationCompany) {
        return locationRepository.save(locationCompany);
    }

    @Override
    public LocationCompany update(LocationCompany locationCompany, Long id) {
        Optional<LocationCompany> selectedLocation = locationRepository.findById(id);
        if (selectedLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
        }
        LocationCompany updatedLocation = selectedLocation.get();
        updatedLocation.setName(locationCompany.getName());
        updatedLocation.setAddress(locationCompany.getAddress());
        updatedLocation.setZipCode(locationCompany.getZipCode());
        updatedLocation.setCity(locationCompany.getCity());
        updatedLocation.setCountry(locationCompany.getCountry());
        updatedLocation.setPhoneNumber(locationCompany.getPhoneNumber());
        updatedLocation.setEmail(locationCompany.getEmail());
        return locationRepository.save(locationCompany);
    }

    @Override
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public LocationCompany findByName(String name) {
        return locationRepository.findByName(name).orElse(null);
    }
}
