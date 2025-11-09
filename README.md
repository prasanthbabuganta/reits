# REITs Investment Platform

A comprehensive Real Estate Investment Trust (REITs) platform that enables users to invest in REITs, track their portfolios, and manage investments across Singapore and international markets.

![Platform Overview](docs/images/platform-overview.png)

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Development](#development)
- [Deployment](#deployment)
- [Testing](#testing)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Features

### Core Business Features

- **REITs Marketplace**
  - Browse and search Singapore REITs, US REITs, and Asian REITs
  - Filter by sector (retail, office, industrial, hospitality, healthcare, data centers)
  - View detailed REIT information (market cap, dividend yield, P/B ratio, occupancy rates)
  - Real-time price updates via WebSocket
  - Historical performance data

- **Investment Management**
  - Buy/sell REIT units with fractional investing
  - Dollar-cost averaging (DCA) plans
  - Portfolio rebalancing tools
  - Tax-efficient investment strategies

- **Portfolio Analytics**
  - Real-time portfolio performance tracking
  - Dividend tracking and projections
  - Geographic and sector diversification analysis
  - Risk assessment metrics
  - Benchmark comparisons (e.g., FTSE ST REIT Index)

- **Admin Panel**
  - User management with KYC verification
  - REIT listing management
  - Transaction monitoring
  - Platform analytics and reporting

- **Mobile App**
  - Native iOS and Android support
  - Biometric authentication
  - Push notifications for dividends
  - Offline mode for portfolio viewing
  - Dark/light theme support

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**:
  - PostgreSQL 15 (primary data)
  - MongoDB 7 (market data & analytics)
  - Redis 7 (caching & sessions)
- **Message Queue**: Apache Kafka
- **Security**: Spring Security with JWT
- **API Documentation**: OpenAPI/Swagger
- **Build Tool**: Maven

### Frontend (Admin Panel)
- **Framework**: React 18
- **State Management**: Redux Toolkit
- **UI Library**: Material-UI (MUI)
- **Charts**: Recharts, Chart.js
- **HTTP Client**: Axios
- **Routing**: React Router v6

### Mobile App
- **Framework**: React Native with Expo
- **Navigation**: React Navigation
- **State Management**: Redux Toolkit
- **Charts**: React Native Chart Kit
- **Storage**: AsyncStorage, Expo Secure Store

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **CI/CD**: GitHub Actions (recommended)
- **Monitoring**: Prometheus + Grafana (optional)

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│   Mobile App    │────▶│   API Gateway   │────▶│  Spring Boot    │
│  (React Native) │     │   (NGINX)       │     │    Backend      │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └────────┬────────┘
                                                          │
┌─────────────────┐                                      │
│                 │                                      │
│  Admin Panel    │──────────────────────────────────────┤
│    (React)      │                                      │
│                 │                                      │
└─────────────────┘                                      │
                                                          │
                        ┌─────────────────────────────────┼────────────────┐
                        │                                 │                │
                  ┌─────▼──────┐                   ┌─────▼──────┐  ┌─────▼──────┐
                  │            │                   │            │  │            │
                  │ PostgreSQL │                   │  MongoDB   │  │   Redis    │
                  │            │                   │            │  │            │
                  └────────────┘                   └────────────┘  └────────────┘
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- Maven 3.9+
- Git

### Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/reits-platform.git
   cd reits-platform
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start all services**
   ```bash
   docker-compose up -d
   ```

4. **Access the applications**
   - Backend API: http://localhost:8080
   - Admin Panel: http://localhost:3000
   - API Documentation: http://localhost:8080/swagger-ui.html

5. **Default Admin Credentials**
   - Email: admin@reitsplatform.com
   - Password: Admin@123

### Manual Setup

#### Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

#### Admin Panel Setup

```bash
cd admin-panel

# Install dependencies
npm install

# Start development server
npm start
```

#### Mobile App Setup

```bash
cd mobile-app

# Install dependencies
npm install

# Start Expo
npm start

# Run on iOS
npm run ios

# Run on Android
npm run android
```

## Project Structure

```
reits/
├── backend/                    # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/reitsplatform/
│   │   │   │   ├── api/            # REST Controllers & DTOs
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── domain/         # Entities & Repositories
│   │   │   │   ├── security/       # JWT & Security Config
│   │   │   │   ├── integration/    # External API Integrations
│   │   │   │   └── config/         # Configuration Classes
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── admin-panel/                # React Admin Dashboard
│   ├── src/
│   │   ├── components/         # React Components
│   │   │   ├── Dashboard/
│   │   │   ├── REITManagement/
│   │   │   └── UserManagement/
│   │   ├── services/           # API Services
│   │   ├── store/              # Redux Store & Slices
│   │   └── utils/              # Utility Functions
│   ├── package.json
│   └── Dockerfile
│
├── mobile-app/                 # React Native Mobile App
│   ├── src/
│   │   ├── screens/            # App Screens
│   │   │   ├── Auth/
│   │   │   ├── Market/
│   │   │   ├── Portfolio/
│   │   │   └── Profile/
│   │   ├── navigation/         # Navigation Config
│   │   ├── services/           # API Services
│   │   └── store/              # Redux Store
│   ├── App.js
│   └── package.json
│
├── database/                   # Database Scripts
│   └── init.sql
│
├── docker-compose.yml
├── .env.example
└── README.md
```

## API Documentation

### Authentication Endpoints

#### Register
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+65 9123 4567",
  "preferredCurrency": "SGD"
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

#### Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

### REIT Endpoints

#### Get All REITs
```http
GET /api/v1/reits
Authorization: Bearer {token}
```

#### Get REIT by ID
```http
GET /api/v1/reits/{id}
Authorization: Bearer {token}
```

#### Search REITs
```http
GET /api/v1/reits/search?q=capitamall
Authorization: Bearer {token}
```

### Transaction Endpoints

#### Buy REIT
```http
POST /api/v1/transactions/buy
Authorization: Bearer {token}
Content-Type: application/json

{
  "reitId": 1,
  "type": "BUY",
  "units": 100,
  "paymentMethod": "WALLET"
}
```

#### Sell REIT
```http
POST /api/v1/transactions/sell
Authorization: Bearer {token}
Content-Type: application/json

{
  "reitId": 1,
  "type": "SELL",
  "units": 50
}
```

### Portfolio Endpoints

#### Get Portfolio
```http
GET /api/v1/portfolio
Authorization: Bearer {token}
```

For complete API documentation, visit: http://localhost:8080/swagger-ui.html

## Development

### Running Tests

#### Backend Tests
```bash
cd backend
mvn test
```

#### Frontend Tests
```bash
cd admin-panel
npm test
```

#### Mobile Tests
```bash
cd mobile-app
npm test
```

### Code Quality

```bash
# Backend - Run checkstyle
mvn checkstyle:check

# Frontend - Run ESLint
npm run lint
```

### Database Migrations

Database schema is managed by Hibernate (JPA). For production, consider using Flyway or Liquibase:

```bash
# Example with Flyway
mvn flyway:migrate
```

## Deployment

### Production Environment Variables

Update `.env` file with production values:

```env
# Production Database
DB_HOST=your-prod-db-host
DB_NAME=reitsdb_prod
DB_USER=prod_user
DB_PASSWORD=strong_password_here

# JWT Configuration
JWT_SECRET=your-production-secret-minimum-256-bits

# External Services
ALPHA_VANTAGE_API_KEY=your_actual_api_key
STRIPE_API_KEY=your_stripe_production_key
```

### Docker Production Deployment

```bash
# Build production images
docker-compose -f docker-compose.prod.yml build

# Start services
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose logs -f
```

### Kubernetes Deployment (Optional)

```bash
# Apply Kubernetes manifests
kubectl apply -f k8s/

# Check deployment status
kubectl get pods
kubectl get services
```

## Security

### Security Features

- JWT-based authentication with refresh tokens
- BCrypt password hashing
- Role-based access control (RBAC)
- CORS configuration
- Rate limiting
- SQL injection prevention via JPA/Hibernate
- XSS protection
- HTTPS enforcement (production)

### Security Best Practices

1. **Never commit sensitive data** (use .env files)
2. **Rotate JWT secrets** regularly
3. **Enable MFA** for admin accounts
4. **Regular security audits**
5. **Keep dependencies updated**
6. **Use HTTPS** in production
7. **Implement CSP headers**

## Performance Optimization

### Backend Optimization
- Redis caching for frequently accessed data
- Database query optimization with indexes
- Connection pooling (HikariCP)
- Async processing for heavy operations

### Frontend Optimization
- Code splitting
- Lazy loading of components
- Image optimization
- CDN for static assets

### Mobile Optimization
- Offline-first architecture
- Image caching
- Minimize bundle size
- Optimize re-renders

## Monitoring & Logging

### Application Monitoring
- Spring Boot Actuator for health checks
- Prometheus metrics export
- Grafana dashboards
- ELK stack for log aggregation

### Access Endpoints
- Health Check: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Prometheus: http://localhost:8080/actuator/prometheus

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```bash
   # Check if PostgreSQL is running
   docker ps | grep postgres

   # Check connection
   psql -h localhost -U reitsuser -d reitsdb
   ```

2. **Port Already in Use**
   ```bash
   # Find process using port 8080
   lsof -i :8080

   # Kill the process
   kill -9 <PID>
   ```

3. **Docker Containers Not Starting**
   ```bash
   # Remove all containers and volumes
   docker-compose down -v

   # Rebuild and start
   docker-compose up --build
   ```

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Roadmap

### Phase 1: MVP ✅
- User authentication & profile
- Basic REIT listings
- Simple buy/sell functionality
- Portfolio view
- Basic admin panel

### Phase 2: Enhanced Features (Q2 2024)
- [ ] Advanced analytics
- [ ] Dividend tracking
- [ ] Automated investing (DCA)
- [ ] Mobile app iOS/Android release
- [ ] Payment integration (Stripe)

### Phase 3: Scale & Optimize (Q3 2024)
- [ ] International markets expansion
- [ ] AI-powered recommendations
- [ ] Social features
- [ ] Advanced trading tools
- [ ] Performance optimization

### Phase 4: Advanced Features (Q4 2024)
- [ ] Institutional features
- [ ] Public API for third parties
- [ ] Robo-advisory
- [ ] Tax optimization tools
- [ ] White-label solution

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- Documentation: [docs.reitsplatform.com](https://docs.reitsplatform.com)
- Email: support@reitsplatform.com
- Discord: [Join our community](https://discord.gg/reitsplatform)
- Issue Tracker: [GitHub Issues](https://github.com/yourusername/reits-platform/issues)

## Acknowledgments

- Spring Boot Team
- React & React Native Communities
- All contributors who helped build this platform

---

**Built with ❤️ by the REITs Platform Team**
