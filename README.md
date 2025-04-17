# 🎬 Rush - Application de Gestion pour Assistants de Production Cinématographique

**Rush** est une application innovante développée en Java Spring Boot, conçue pour faciliter le travail quotidien des assistants de production dans l'industrie cinématographique. Elle centralise les données essentielles et automatise les processus administratifs et logistiques liés à la gestion des tournages.

---

## 🚀 Fonctionnalités principales

### 👥 Gestion du Personnel
- Accès complet aux membres de l'équipe interne et aux collaborateurs externes.
- Filtres par compétence, disponibilité et projet.
- Historique des participations à d'autres productions.

### 🗓️ Planification et Disponibilités
- Vue calendrier interactive (intégrable).
- Gestion des disponibilités et des conflits d'horaire (intégrable).
- Notifications automatiques pour les événements importants.

### 🎥 Gestion de l’Équipement
- Liste des équipements avec filtres par statut, usage, etc.
- Suivi des maintenances, assurances, et dates de location.
- Planification automatique des révisions à venir.

### 📂 Gestion des Projets & Etapes
- Création, mise à jour, suppression de projets et de leurs étapes (stages).
- Attribution d'équipements et d’employés aux projets.
- Suivi de l'état d’avancement (ex : "clôturé").

### 🧾 Archivage & Tâches
- Système de tâches catégorisées par type et statut.
- Archivage des tâches et procédures pour réutilisation future.

### 💬 Messagerie Collaborative
- Forum de discussion par projet, sujet ou département.
- Messagerie professionnelle avec options de confidentialité.

---

## 🛠️ Stack Technique

- **Backend** : Java 17, Spring Boot 3, Spring Security, JWT
- **Base de données** : PostgreSQL
- **Documentation API** : Swagger / OpenAPI
- **Tests** : JUnit, Mockito
- **Gestion des rôles** : Admin, Staff, Utilisateur standard

---

## 📦 API Endpoints Principaux

Les contrôleurs REST sont organisés autour des entités suivantes :

| Module | Endpoints | Sécurité |
|--------|-----------|----------|
| Authentification | `/auth/login`, `/auth/register` | Public |
| Utilisateurs | `/users` | Admin / Staff |
| Équipements | `/equipements` | Admin / Staff |
| Projets | `/projects` | Authentifié |
| Stages | `/stages` | Admin / Staff |
| Sociétés de location | `/renting-company` | Admin / Staff |
| Sociétés de production | `/production-companies` | Admin / Staff |

> 🔐 Les routes sont protégées via Spring Security avec vérification JWT et annotations `@PreAuthorize`.

---

## 🔧 Démarrage du projet

### Prérequis

- Java 17+
- Maven 3+
- PostgreSQL

### Configuration

Configurer votre base de données et les variables d’environnement dans `application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rushdb
spring.datasource.username=postgres
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
