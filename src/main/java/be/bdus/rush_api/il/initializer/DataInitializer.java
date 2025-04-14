package be.bdus.rush_api.il.initializer;

import be.bdus.rush_api.dal.repositories.*;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import be.bdus.rush_api.dl.enums.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LCompanyRepository locationCompanyRepository;
    private final PCompanyRepository productionCompanyRepository;
    private final EquipementRepository equipementRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final StageRepository stageRepository;

    public DataInitializer(LCompanyRepository locationCompanyRepository,
                           PCompanyRepository productionCompanyRepository,
                           EquipementRepository equipementRepository,
                           UserRepository userRepository,
                           ProjectRepository projectRepository,
                           StageRepository stageRepository) {
        this.locationCompanyRepository = locationCompanyRepository;
        this.productionCompanyRepository = productionCompanyRepository;
        this.equipementRepository = equipementRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public void run(String... args) {
        loadLocationCompanies();
        loadProductionCompanies();
        loadUsers();
        loadEquipements();
        loadStagesAndProjects();
    }

    private void loadLocationCompanies() {
        if (locationCompanyRepository.count() == 0) {
            LocationCompany loc1 = new LocationCompany();
            loc1.setName("LocaPro");
            loc1.setAddress("123 Rue de la Location");
            loc1.setZipCode("1000");
            loc1.setCity("Bruxelles");
            loc1.setCountry("Belgique");
            loc1.setPhoneNumber(321234567);
            loc1.setEmail("contact@locapro.com");

            locationCompanyRepository.save(loc1);
        }
    }

    private void loadProductionCompanies() {
        if (productionCompanyRepository.count() == 0) {
            ProductionCompany prod = new ProductionCompany();
            prod.setName("ProdX");
            prod.setAddress("456 Rue de la Prod");
            prod.setZipCode("75000");
            prod.setCity("Paris");
            prod.setCountry("France");
            prod.setPhoneNumber(339876543);
            prod.setEmail("info@prodx.com");

            productionCompanyRepository.save(prod);
        }
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            User user1 = new User("Jean", "Dupont", "jean.dupont@mail.com", "pass123");
            user1.setPhoneNumber("123456789");
            user1.setJobTitle("Chef de projet");
            user1.setAvailable(true);
            user1.setStatus(EmployeStatus.ACTIVE);
            user1.setRole(UserRole.ADMIN);

            User user2 = new User("Marie", "Curie", "marie.curie@mail.com", "pass123");
            user2.setPhoneNumber("987654321");
            user2.setJobTitle("Technicienne");
            user2.setAvailable(true);
            user2.setStatus(EmployeStatus.ACTIVE);
            user2.setRole(UserRole.USER);

            userRepository.saveAll(List.of(user1, user2));
        }
    }

    private void loadEquipements() {
        if (equipementRepository.count() == 0 && locationCompanyRepository.count() > 0) {
            LocationCompany loc = locationCompanyRepository.findAll().get(0);

            Equipement eq1 = new Equipement();
            eq1.setName("Caméra Sony");
            eq1.setOwner(loc);
            eq1.setDescription("Caméra 4K professionnelle");
            eq1.setModel("Sony FX6");
            eq1.setSerialNumber("SN123456");
            eq1.setType("Caméra");
            eq1.setCondition(EquipementCondition.NEW);
            eq1.setStock(5);
            eq1.setStockagePlace("Entrepôt A");
            eq1.setDateAcquisition(new Date());

            equipementRepository.save(eq1);
        }
    }

    private void loadStagesAndProjects() {
        if (projectRepository.count() == 0 && userRepository.count() >= 2 && productionCompanyRepository.count() > 0) {
            User responsable = userRepository.findAll().get(0);
            List<User> employes = userRepository.findAll();

            ProductionCompany prod = productionCompanyRepository.findAll().get(0);
            List<Equipement> equipements = equipementRepository.findAll();

            Project project = new Project();
            project.setName("Project Cinéma");
            project.setDescription("Tournage d'un court-métrage.");
            project.setStartingDate(new Date());
            project.setStatus(StageStatus.OPEN);
            project.setResponsable(responsable);
            project.setEmployes(employes);
            project.setProductionCompany(prod);
            project.setEquipements(equipements);
            project.setNbOfStages(1);
            project.setPourcentageDone(0.0);
            project.setDuration(30);
            project.setBudget(50000);
            project.setPlace("Bruxelles");

            projectRepository.save(project);

            Stage stage = new Stage();
            stage.setName("Préparation Matériel");
            stage.setDescription("Vérification et préparation du matériel.");
            stage.setStartingDate(new Date());
            stage.setStatus(StageStatus.IN_PROGRESS);
            stage.setResponsable(responsable);

            stageRepository.save(stage);
        }
    }
}
