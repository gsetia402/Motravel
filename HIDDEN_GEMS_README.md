# 🏔️ Motravel Hidden Gems Feature

A comprehensive backend feature for discovering and managing curated travel destinations across India, integrated with the existing Motravel vehicle rental platform.

## 🌟 Overview

The Hidden Gems feature allows users to explore lesser-known travel destinations categorized by Indian states and adventure types. It provides a complete CRUD API with advanced filtering, search capabilities, and user interaction features like bookmarking.

## 📊 Database Schema

### Core Tables

#### `states`
- `id` (PK) - Auto-generated state ID
- `name` - State name (unique)

#### `adventure_types`
- `id` (PK) - Auto-generated adventure type ID
- `name` - Adventure type name (unique)

#### `hidden_gems`
- `id` (PK) - Auto-generated gem ID
- `name` - Hidden gem name
- `description` - Detailed description
- `state_id` (FK) - Reference to states table
- `latitude` - GPS latitude coordinate
- `longitude` - GPS longitude coordinate
- `nearest_city` - Nearest major city
- `best_time_to_visit` - Recommended visiting season
- `difficulty_level` - Activity difficulty (optional)
- `cost_range` - Estimated cost range (optional)
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

#### `hidden_gem_adventure_types` (Junction Table)
- `hidden_gem_id` (FK) - Reference to hidden_gems
- `adventure_type_id` (FK) - Reference to adventure_types
- Composite Primary Key

#### `hidden_gem_images` (Collection Table)
- `hidden_gem_id` (FK) - Reference to hidden_gems
- `image_url` - Image URL

#### `hidden_gem_bookmarks` (Optional User Favorites)
- `user_id` - User ID from authentication system
- `hidden_gem_id` (FK) - Reference to hidden_gems
- `bookmarked_at` - Bookmark timestamp
- Composite Primary Key

## 🚀 API Endpoints

### 🌍 Public Endpoints (No Authentication Required)

#### Hidden Gems
- `GET /api/hidden-gems` - List all hidden gems with filtering and pagination
  - Query Parameters:
    - `stateId` - Filter by state
    - `adventureTypeIds` - Filter by adventure types (comma-separated)
    - `search` - Search in name/description
    - `page` - Page number (default: 0)
    - `size` - Page size (default: 10)
    - `sortBy` - Sort field (default: createdAt)
    - `sortDirection` - Sort direction (default: desc)

- `GET /api/hidden-gems/{id}` - Get specific hidden gem details
- `GET /api/hidden-gems/nearby` - Find gems near location
  - Query Parameters:
    - `latitude` - GPS latitude
    - `longitude` - GPS longitude
    - `radius` - Search radius in km (default: 50)
- `GET /api/hidden-gems/stats` - Get platform statistics

#### States
- `GET /api/states` - List all states
- `GET /api/states/{id}` - Get state details with gem count
- `GET /api/states/search?q={term}` - Search states by name
- `GET /api/states/{id}/hidden-gems` - Get gems for specific state

#### Adventure Types
- `GET /api/adventure-types` - List all adventure types
- `GET /api/adventure-types/{id}` - Get adventure type details with gem count
- `GET /api/adventure-types/search?q={term}` - Search adventure types
- `GET /api/adventure-types/{id}/hidden-gems` - Get gems for specific adventure type

### 🔒 Protected User Endpoints (Authentication Required)

#### Bookmarks
- `POST /api/hidden-gems/{id}/bookmark` - Bookmark a hidden gem
- `DELETE /api/hidden-gems/{id}/bookmark` - Remove bookmark
- `POST /api/hidden-gems/{id}/toggle-bookmark` - Toggle bookmark status
- `GET /api/users/bookmarks` - Get user's bookmarked gems
- `GET /api/users/bookmarks/count` - Get user's bookmark count
- `GET /api/users/bookmarks/check/{id}` - Check if gem is bookmarked

### 👑 Admin Endpoints (Admin Role Required)

#### Hidden Gems Management
- `POST /api/admin/hidden-gems` - Create new hidden gem
- `PUT /api/admin/hidden-gems/{id}` - Update hidden gem
- `DELETE /api/admin/hidden-gems/{id}` - Delete hidden gem
- `GET /api/admin/hidden-gems` - Admin view of all gems
- `GET /api/admin/hidden-gems/{id}` - Admin view of specific gem

#### States Management
- `POST /api/admin/states` - Create new state
- `PUT /api/admin/states/{id}` - Update state
- `DELETE /api/admin/states/{id}` - Delete state (if no associated gems)
- `GET /api/admin/states` - Admin view of all states

#### Adventure Types Management
- `POST /api/admin/adventure-types` - Create new adventure type
- `PUT /api/admin/adventure-types/{id}` - Update adventure type
- `DELETE /api/admin/adventure-types/{id}` - Delete adventure type (if no associated gems)
- `GET /api/admin/adventure-types` - Admin view of all adventure types

## 🎯 Key Features

### Advanced Filtering & Search
- **State-based filtering** - Find gems in specific states
- **Adventure type filtering** - Filter by activity types (multiple selection)
- **Text search** - Search in gem names and descriptions
- **Location-based search** - Find gems within specified radius
- **Combined filtering** - Use multiple filters simultaneously

### Pagination & Sorting
- **Configurable pagination** - Customizable page size
- **Multiple sort options** - Sort by creation date, name, state, etc.
- **Flexible sort direction** - Ascending or descending

### User Interactions
- **Bookmarking system** - Users can save favorite gems
- **Bookmark management** - View, add, remove bookmarks
- **Popularity tracking** - Track bookmark counts per gem

### Data Validation
- **Comprehensive validation** - All inputs validated using Bean Validation
- **Geographic constraints** - Latitude/longitude bounds checking
- **Business rule enforcement** - Prevent deletion of referenced entities

## 📝 Sample Data

The system initializes with sample data including:

### States (15 total)
- Maharashtra, Himachal Pradesh, Uttarakhand, Rajasthan, Kerala
- Karnataka, Tamil Nadu, Goa, Gujarat, Madhya Pradesh
- Jammu and Kashmir, Ladakh, Sikkim, Meghalaya, Assam

### Adventure Types (18 total)
- Trekking, Camping, Water Sports, Rock Climbing, Paragliding
- River Rafting, Scuba Diving, Wildlife Safari, Mountain Biking
- Skiing, Snowboarding, Bungee Jumping, Zip Lining, Cave Exploration
- Photography, Bird Watching, Backpacking, Hiking

### Sample Hidden Gems
1. **Harishchandragad Fort** (Maharashtra) - Trekking, Camping, Photography
2. **Tosh Village** (Himachal Pradesh) - Trekking, Photography, Camping
3. **Chopta Meadows** (Uttarakhand) - Trekking, Camping, Photography
4. **Kumta Beach** (Kerala) - Water Sports, Photography
5. **Khimsar Sand Dunes** (Rajasthan) - Camping, Photography

## 🔧 Technical Implementation

### Architecture
- **Spring Boot 3.x** - Main framework
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Primary database
- **Spring Security** - Authentication & authorization
- **Bean Validation** - Input validation
- **Swagger/OpenAPI 3** - API documentation

### Security Configuration
- **Public endpoints** - All GET operations for browsing
- **Protected endpoints** - Bookmark operations require USER/ADMIN role
- **Admin endpoints** - Management operations require ADMIN role
- **JWT Authentication** - Token-based security

### Performance Optimizations
- **Eager loading** - Strategic fetch strategies for related entities
- **Indexed queries** - Optimized database queries
- **Pagination** - Memory-efficient large dataset handling
- **Query optimization** - Custom JPQL queries for complex filtering

## 🧪 Testing the API

### Using Swagger UI
1. Start the application
2. Visit `http://localhost:8080/swagger-ui.html`
3. Explore the "Hidden Gems", "States", and "Adventure Types" sections

### Sample API Calls

#### Get all hidden gems
```bash
curl -X GET "http://localhost:8080/api/hidden-gems"
```

#### Filter gems by state and adventure type
```bash
curl -X GET "http://localhost:8080/api/hidden-gems?stateId=1&adventureTypeIds=1,2&page=0&size=5"
```

#### Search gems
```bash
curl -X GET "http://localhost:8080/api/hidden-gems?search=fort&sortBy=name&sortDirection=asc"
```

#### Find nearby gems
```bash
curl -X GET "http://localhost:8080/api/hidden-gems/nearby?latitude=19.0760&longitude=72.8777&radius=100"
```

#### Bookmark a gem (requires authentication)
```bash
curl -X POST "http://localhost:8080/api/hidden-gems/1/bookmark" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Create a new gem (admin only)
```bash
curl -X POST "http://localhost:8080/api/admin/hidden-gems" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Secret Waterfall",
    "description": "A hidden waterfall perfect for photography",
    "state": {"id": 1},
    "adventureTypes": [{"id": 1}, {"id": 15}],
    "latitude": 19.1234,
    "longitude": 73.5678,
    "nearestCity": "Pune",
    "bestTimeToVisit": "Monsoon season",
    "difficultyLevel": "Easy",
    "costRange": "₹200-500"
  }'
```

## 🚀 Getting Started

1. **Database Setup** - Ensure PostgreSQL is running
2. **Start Application** - Run the Spring Boot application
3. **Auto-initialization** - Sample data will be loaded automatically
4. **API Documentation** - Visit Swagger UI for interactive testing
5. **Authentication** - Use `/api/auth/signin` to get JWT tokens for protected endpoints

## 🔮 Future Enhancements

- **Rating System** - Allow users to rate hidden gems
- **Review System** - User reviews and comments
- **Image Upload** - Direct image upload functionality
- **Social Features** - Share gems with friends
- **Recommendation Engine** - AI-powered gem recommendations
- **Mobile App Integration** - Dedicated mobile endpoints
- **Geofencing** - Location-based notifications
- **Weather Integration** - Real-time weather data for gems

---

**The Hidden Gems feature transforms Motravel from a simple vehicle rental platform into a comprehensive travel discovery and planning ecosystem! 🌟**
