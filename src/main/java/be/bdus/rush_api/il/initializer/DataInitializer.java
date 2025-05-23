package be.bdus.rush_api.il.initializer;

import be.bdus.rush_api.dal.repositories.*;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import be.bdus.rush_api.dl.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RCompanyRepository locationCompanyRepository;
    private final PCompanyRepository productionCompanyRepository;
    private final EquipementRepository equipementRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final StageRepository stageRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public DataInitializer(RCompanyRepository locationCompanyRepository,
                           PCompanyRepository productionCompanyRepository,
                           EquipementRepository equipementRepository,
                           UserRepository userRepository,
                           ProjectRepository projectRepository,
                           StageRepository stageRepository, EmployeeRepository employeeRepository) {
        this.locationCompanyRepository = locationCompanyRepository;
        this.productionCompanyRepository = productionCompanyRepository;
        this.equipementRepository = equipementRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.stageRepository = stageRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        loadLocationCompanies();
        loadProductionCompanies();
        loadUsers();
        loadEmployees();
        loadEquipements();
        loadStagesAndProjects();

    }

    private void loadLocationCompanies() {
        if (locationCompanyRepository.count() == 0) {
            RentingCompany loc1 = new RentingCompany("LocaPro", "123 Rue de la Location", "1000", "Bruxelles", "Belgique", "321234567", "contact@locapro.com");
            RentingCompany loc2 = new RentingCompany("GearRent", "22 Avenue des Tournages", "69000", "Lyon", "France", "334556677", "hello@gearrent.fr");

            locationCompanyRepository.saveAll(List.of(loc1, loc2));
        }
    }

    private void loadProductionCompanies() {
        if (productionCompanyRepository.count() == 0) {
            ProductionCompany prod1 = new ProductionCompany("ProdX", "456 Rue de la Prod", "75000", "Paris", "France", "339876543", "info@prodx.com");
            ProductionCompany prod2 = new ProductionCompany("CinéLight", "89 Boulevard des Films", "1000", "Bruxelles", "Belgique", "322334455", "contact@cinelight.be");

            productionCompanyRepository.saveAll(List.of(prod1, prod2));
        }
    }

    private void loadEmployees() {
        if (employeeRepository.count() == 0) {
            Employee employee1 = new Employee("Jean", "Dupont", "jean.dupont@mail.com");
            employee1.setPhoneNumber("123456789");
            employee1.setJobTitle("Chef de projet");
            employee1.setAvailable(true);

            Employee employee2 = new Employee("Marie", "Curie", "marie.curie@mail.com");
            employee2.setPhoneNumber("987654321");
            employee2.setJobTitle("Technicienne");
            employee2.setAvailable(true);

            Employee employee3 = new Employee("Paul", "Verlaine", "paul.v@mail.com");
            employee3.setPhoneNumber("667788990");
            employee3.setJobTitle("Assistant plateau");
            employee3.setAvailable(true);

            Employee employee4 = new Employee("Claire", "Beauvoir", "claire.b@mail.com");
            employee4.setPhoneNumber("445566778");
            employee4.setJobTitle("Directrice artistique");
            employee4.setAvailable(true);

            employeeRepository.saveAll(List.of(employee1, employee2, employee3, employee4));
        }
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            User Barnabe = new User("Barna", "Barnabé", "Dussart", "dussart.barnabe@gmail.com", passwordEncoder.encode("test"));
            Barnabe.setPhoneNumber("+32488842292");
            Barnabe.setJobTitle("Dev");
            Barnabe.setAvailable(true);
            Barnabe.setStatus(EmployeStatus.ACTIVE);
            Barnabe.setRole(UserRole.ADMIN);
            User user1 = new User("JeaDup", "Jean", "Dupont", "jean.dupont@mail.com", passwordEncoder.encode("pass123"));
            user1.setPhoneNumber("123456789");
            user1.setJobTitle("Chef de projet");
            user1.setAvailable(true);
            user1.setStatus(EmployeStatus.ACTIVE);
            user1.setRole(UserRole.ADMIN);

            User user2 = new User("MarCur","Marie", "Curie", "marie.curie@mail.com", passwordEncoder.encode("pass123"));
            user2.setPhoneNumber("987654321");
            user2.setJobTitle("Technicienne");
            user2.setAvailable(true);
            user2.setStatus(EmployeStatus.ACTIVE);
            user2.setRole(UserRole.USER);

            User user3 = new User("PauVer","Paul", "Verlaine", "paul.v@mail.com", passwordEncoder.encode("pass123"));
            user3.setPhoneNumber("667788990");
            user3.setJobTitle("Assistant plateau");
            user3.setAvailable(true);
            user3.setStatus(EmployeStatus.INACTIVE);
            user3.setRole(UserRole.USER);

            User user4 = new User("ClaBea","Claire", "Beauvoir", "claire.b@mail.com", passwordEncoder.encode("pass123"));
            user4.setPhoneNumber("445566778");
            user4.setJobTitle("Directrice artistique");
            user4.setAvailable(true);
            user4.setStatus(EmployeStatus.ACTIVE);
            user4.setRole(UserRole.USER);

            userRepository.saveAll(List.of(Barnabe, user1, user2, user3, user4));
        }
    }

    private void loadEquipements() {
        if (equipementRepository.count() == 0) {
            List<RentingCompany> locs = locationCompanyRepository.findAll();

            Equipement eq1 = new Equipement(
                    "Caméra Sony",
                    locs.get(0),
                    "Caméra 4K professionnelle",
                    "Sony FX6",
                    "SN123456",
                    "Caméra",
                    EquipementCondition.NEW,
                    5,
                    "Entrepôt A",
                    LocalDate.of(2025, 4, 16),
                    LocalDate.of(2025, 4, 16)
            );

            Equipement eq2 = new Equipement(
                    "Micro Rode",
                    locs.get(1),
                    "Micro directionnel",
                    "Rode NTG3",
                    "SN789012",
                    "Audio",
                    EquipementCondition.GOOD,
                    3,
                    "Entrepôt B",
                    LocalDate.of(2025, 4, 16),
                    LocalDate.of(2025, 4, 16)
            );

            Equipement eq3 = new Equipement(
                    "Projecteur LED",
                    locs.get(0),
                    "Projecteur puissant pour tournage intérieur",
                    "Aputure 300d",
                    "SN345678",
                    "Éclairage",
                    EquipementCondition.NEW,
                    2,
                    "Entrepôt A",
                    LocalDate.of(2025, 4, 16),
                    LocalDate.of(2025, 4, 16)
            );

            equipementRepository.saveAll(List.of(eq1, eq2, eq3));
        }
    }

    private void loadStagesAndProjects() {
        if (projectRepository.count() == 0 && userRepository.count() >= 2 && productionCompanyRepository.count() > 0) {
            User responsable = userRepository.findAll().get(0);
            List<Employee> employes = employeeRepository.findAll();
            ProductionCompany prod = productionCompanyRepository.findAll().get(0);
            List<User> users = userRepository.findAll();
            List<Equipement> equipements = equipementRepository.findAll();
            List<ProductionCompany> prods = productionCompanyRepository.findAll();

            Project p1 = new Project();
            p1.setName("Project Cinéma");
            p1.setDescription("Tournage d'un court-métrage.");
            p1.setStartingDate(LocalDate.of(2025, 4, 16));
            p1.setStatus(StageStatus.OPEN);
            p1.setResponsable(responsable);
            p1.setEmployes(employes);
            p1.setProductionCompany(prod);
            p1.setEquipements(equipements);
            p1.setNbOfStages(1);
            p1.setPourcentageDone(0.0);
            p1.setDuration(30);
            p1.setBudget(50000);
            p1.setPlace("Bruxelles");
            projectRepository.save(p1);

            Stage s1 = new Stage();
            s1.setName("Préparation Matériel");
            s1.setDescription("Vérification et préparation du matériel.");
            s1.setStartingDate(LocalDate.of(2025, 4, 16));
            s1.setStatus(StageStatus.OPEN);
            s1.setResponsable(users.get(0));
            Stage s2 = new Stage();
            s2.setName("Tournage");
            s2.setDescription("Tournage du film.");
            s2.setStartingDate(LocalDate.of(2025, 4, 16));
            s2.setStatus(StageStatus.OPEN);
            s2.setResponsable(users.get(1));

            stageRepository.saveAll(List.of(s1, s2));

            Project p2 = new Project();
            p2.setName("Project Documentaire");
            p2.setDescription("Tournage d'un documentaire.");
            p2.setStartingDate(LocalDate.of(2025, 4, 16));
            p2.setStatus(StageStatus.OPEN);
            p2.setResponsable(responsable);
            p2.setEmployes(employes);
            p2.setProductionCompany(prod);
            p2.setEquipements(equipements);
            p2.setNbOfStages(2);
            p2.setPourcentageDone(0.0);
            p2.setDuration(60);
            p2.setBudget(100000);
            p2.setPlace("Bruxelles");
            projectRepository.save(p2);

            Stage s3 = new Stage();
            s3.setName("Préparation Matériel");
            s3.setDescription("Vérification et préparation du matériel.");
            s3.setStartingDate(LocalDate.of(2025, 4, 16));
            s3.setStatus(StageStatus.PENDING);
            s3.setResponsable(users.get(0));
            Stage s4 = new Stage();
            s4.setName("Tournage");
            s4.setDescription("Tournage du film.");
            s4.setStartingDate(LocalDate.of(2025, 4, 16));
            s4.setStatus(StageStatus.OPEN);
            s4.setResponsable(users.get(1));

            stageRepository.saveAll(List.of(s3, s4));
        }
    }
}