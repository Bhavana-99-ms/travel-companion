# TravelGuide — Travel Companion Booking Platform

A Spring Boot web app connecting travelers with local tour guides and travel service providers. Users browse providers, book tours, and leave reviews; providers manage their profile, availability, and incoming bookings.

## Tech stack

- **Backend:** Java 17, Spring Boot 3.2, Spring MVC, Spring Data JPA
- **Auth:** Spring Security (form login, role-based access, BCrypt password hashing)
- **View layer:** Thymeleaf (server-rendered HTML) + Thymeleaf Spring Security extras
- **Database:** MySQL
- **Build tool:** Maven
- **Other:** Lombok, Bean Validation (Jakarta)

## Features

**For travelers**
- Browse and search providers by city and service type (city tours, heritage walks, food tours, adventure, photography, nature treks, cultural experiences, custom itineraries)
- View a provider's profile, rating, and reviews
- Book a provider for a date range with special requests
- View and cancel bookings from a personal dashboard
- Leave a rating and review after a completed booking

**For providers**
- Register with a provider role
- Build a profile: bio, languages, location, price per day, years of experience, certifications
- Toggle availability on/off
- View incoming bookings and confirm, complete, or cancel them
- View reviews left by past customers

**Roles & access control**
- `ROLE_USER` — book tours, leave reviews
- `ROLE_PROVIDER` — manage profile and bookings
- `ROLE_ADMIN` — reserved for admin-only routes (`/admin/**`)
- Public pages: home, provider listing/search, login, register
- Everything else requires authentication

## Project structure

```
src/main/java/com/travel/
├── TravelGuideApplication.java
├── config/          # Spring Security setup, custom user details service
├── controller/       # Home, auth, user dashboard, provider dashboard
├── dto/              # Request/form data (register, booking, review, profile)
├── enums/            # BookingStatus, Role, ServiceType
├── model/            # JPA entities: User, ServiceProvider, Booking, Review
├── repository/       # Spring Data JPA repositories
└── service/          # Business logic

src/main/resources/
├── templates/         # Thymeleaf views (auth, home, booking, provider, review)
├── static/            # CSS and JS
└── application.properties
```

## Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8+ running locally (or accessible remotely)

## Setup

**1. Clone and enter the project**
```bash
git clone <your-repo-url>
cd travel-companion
```

**2. Create the database**

The app auto-creates the schema on startup (`createDatabaseIfNotExist=true`, `hibernate.ddl-auto=update`), so you only need MySQL running — no manual schema setup required.

**3. Configure your database credentials**

⚠️ **Do not commit real credentials.** Set them as environment variables instead of hardcoding them in `application.properties`:

```bash
export DB_URL="jdbc:mysql://localhost:3306/travel_guide_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true"
export DB_USERNAME="root"
export DB_PASSWORD="your_password_here"
```

On Windows PowerShell:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/travel_guide_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password_here"
```

Then update `application.properties` to reference them instead of a literal password:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

**4. Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

The app starts on **http://localhost:8080** by default (configurable via `server.port`).

## Usage

1. Go to `http://localhost:8080` and register an account as either a **traveler** or a **provider**.
2. As a provider, complete your profile under `/provider/dashboard/profile` so travelers can find and book you.
3. As a traveler, browse `/providers`, filter by city or service type, and book a provider.
4. Providers confirm/complete/cancel bookings from their dashboard; travelers can leave a review once a booking is completed.

## Security notes

- Passwords are hashed with BCrypt before storage.
- Role-based route protection restricts `/admin/**` and `/provider/dashboard/**`.
- CSRF protection is enabled by default, with an exemption only for `/api/**` routes.
- **Before pushing this to a public repo:** remove the hardcoded DB password from `application.properties`, rotate that password, and add `application.properties` (or a `.env`) to `.gitignore` if it contains secrets.

## Possible extensions

- Payment integration for bookings
- Email/SMS notifications on booking status changes
- Provider image gallery / portfolio uploads (upload directory is already configured)
- Admin dashboard for managing users, providers, and disputes
- REST API layer for a mobile client
