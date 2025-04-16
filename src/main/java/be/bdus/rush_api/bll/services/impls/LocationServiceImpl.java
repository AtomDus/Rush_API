package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.LocationService;
import be.bdus.rush_api.dal.repositories.LCompanyRepository;
import be.bdus.rush_api.dl.entities.LocationCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LCompanyRepository locationRepository;

    @Override
    public Page<LocationCompany> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable);
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
        return locationRepository.save(updatedLocation);
    }

    @Override
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public Optional<LocationCompany> findByName(String name) {
        return locationRepository.findByName(name);
    }

    @Override
    public Page<LocationCompany> findByProjectsId(Pageable pageable, Long id) {
        return locationRepository.findByProjectsId(id, pageable);
    }
}
