# Quick Start Guide

This guide will help you get the REITs Investment Platform up and running in minutes.

## Prerequisites Check

Before starting, ensure you have:
- ✅ Docker Desktop installed and running
- ✅ Git installed
- ✅ At least 4GB of free RAM
- ✅ Ports 8080, 3000, 5432, 6379, 9092, 27017 available

## 5-Minute Setup

### Step 1: Clone and Configure (1 minute)

```bash
# Clone the repository
git clone <your-repo-url>
cd reits

# Copy environment file
cp .env.example .env
```

### Step 2: Start Services (3 minutes)

```bash
# Start all services with Docker Compose
docker-compose up -d

# Wait for services to be healthy (check with):
docker-compose ps
```

### Step 3: Access Applications (1 minute)

Once all services show "healthy" status:

1. **Backend API**: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

2. **Admin Panel**: http://localhost:3000
   - Default Login:
     - Email: `admin@reitsplatform.com`
     - Password: `Admin@123`

3. **Database**:
   - PostgreSQL: `localhost:5432`
   - MongoDB: `localhost:27017`
   - Redis: `localhost:6379`

## Sample Data

The platform comes pre-loaded with:
- 8 sample REITs (Singapore and US markets)
- 1 admin user
- Sample dividend data

## Testing the Platform

### Test 1: Login to Admin Panel
1. Go to http://localhost:3000
2. Login with admin credentials
3. Explore the dashboard

### Test 2: Browse REITs
1. Navigate to "REITs" in the admin panel
2. View the list of 8 pre-loaded REITs
3. Click "Edit" on any REIT to see details

### Test 3: API Testing
Use the Swagger UI (http://localhost:8080/swagger-ui.html):

1. Click "Authorize" button
2. Login via `/auth/login` endpoint:
   ```json
   {
     "email": "admin@reitsplatform.com",
     "password": "Admin@123"
   }
   ```
3. Copy the `accessToken` from response
4. Click "Authorize" and paste: `Bearer {accessToken}`
5. Try any API endpoint!

## Mobile App Setup (Optional)

```bash
cd mobile-app

# Install dependencies
npm install

# Start Expo
npm start

# Scan QR code with Expo Go app
# Or press 'i' for iOS simulator, 'a' for Android emulator
```

## Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove all data (WARNING: deletes database)
docker-compose down -v
```

## Troubleshooting

### Issue: Port already in use
```bash
# Check what's using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Issue: Services not healthy
```bash
# Check logs
docker-compose logs backend
docker-compose logs postgres

# Restart specific service
docker-compose restart backend
```

### Issue: Cannot login to admin panel
```bash
# Verify admin user exists
docker-compose exec postgres psql -U reitsuser -d reitsdb -c "SELECT email FROM users WHERE email='admin@reitsplatform.com';"

# Re-run init script if needed
docker-compose exec postgres psql -U reitsuser -d reitsdb -f /docker-entrypoint-initdb.d/init.sql
```

## Next Steps

1. **Explore the Admin Panel**
   - Create new REIT listings
   - Manage users
   - View platform statistics

2. **Test the API**
   - Create a new user account
   - Buy some REITs
   - Check your portfolio

3. **Try the Mobile App**
   - Register a new account
   - Browse REITs
   - Make a test purchase

4. **Customize**
   - Update `.env` with your API keys
   - Modify theme colors
   - Add more sample data

## Getting Help

- **Documentation**: See [README.md](README.md)
- **Issues**: Open a GitHub issue
- **API Docs**: http://localhost:8080/swagger-ui.html

Happy investing! 🚀
