package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.RentingService;
import be.bdus.rush_api.dal.repositories.RCompanyRepository;
import be.bdus.rush_api.dl.entities.RentingCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentingServiceImpl implements RentingService {

    private final RCompanyRepository locationRepository;

    @Override
    public Page<RentingCompany> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    @Override
    public RentingCompany findById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No location found found"));
    }

    @Override
    public RentingCompany save(RentingCompany locationCompany) {
        return locationRepository.save(locationCompany);
    }

    @Override
    public RentingCompany update(RentingCompany locationCompany, Long id) {
        Optional<RentingCompany> selectedLocation = locationRepository.findById(id);
        if (selectedLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
        }
        RentingCompany updatedLocation = selectedLocation.get();
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
    public Optional<RentingCompany> findByName(String name) {
        return locationRepository.findByName(name);
    }

    @Override
    public Page<RentingCompany> findByProjectsId(Pageable pageable, Long id) {
        return locationRepository.findByProjectsId(id, pageable);
    }
}
