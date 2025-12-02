# RealtyGen - Real Estate Platform

## 📋 Project Overview
MVP for a real estate website similar to 99acres - a scalable, responsive, and SEO-optimized realty platform enabling buyers to explore properties and administrators to manage listings efficiently.

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Spring Boot 4.0.0, Java |
| **Frontend** | Thymeleaf, Bootstrap/Vanilla JS |
| **Database** | PostgreSQL |
| **Build Tool** | Maven |
| **Utilities** | Lombok, Spring Data JPA, DevTools |

---

## 📂 Project Structure
realtygen/
├── src/main/java/com/realtygen/
│    ├── config/
│    ├── controller/
│    ├── dto/
│    ├── entity/
│    ├── enums/          ← Add this
│    ├── exception/
│    ├── mapper/
│    ├── repository/
│    ├── security/       ← Add this (or put in config)
│    ├── service/
│    ├── storage/
│    ├── util/           ← Add this
│    └── RealtyGenApplication.java
│
├── src/main/resources/
│    ├── static/         ← Add this
│    ├── templates/      ← Add this
│    ├── application.yml
│    └── schema.sql

---

## 🚀 Development Phases

### ✅ **Phase 1: Core Entity Models**
**Status**: 🔴 Not Started  
**Priority**: High

**Tasks**:
- [ ] Create `PropertyType` enum (APARTMENT, VILLA, PLOT, COMMERCIAL)
- [ ] Create `PropertyStatus` enum (ACTIVE, SOLD, RENTED, INACTIVE)
- [ ] Create `UserRole` enum (ADMIN, BROKER, USER)
- [ ] Create `Property` entity with fields:
  - id, title, description, price, address, city, state, pincode
  - area, bedrooms, bathrooms, propertyType, status
  - brokerId, createdAt, updatedAt
- [ ] Create `Broker` entity with fields:
  - id, name, email, phone, company, rating
- [ ] Create `PropertyImage` entity (Many-to-One with Property)
- [ ] Create `PropertyDocument` entity (Many-to-One with Property)
- [ ] Create `User` entity for admin authentication

---

### ⏳ **Phase 2: Database Layer**
**Status**: 🔴 Not Started  
**Priority**: High

**Tasks**:
- [ ] Create `PropertyRepository` with custom queries
  - findByStatus, findByPropertyType, findByCityOrState
  - searchByKeyword, findByPriceRange
- [ ] Create `BrokerRepository`
- [ ] Create `UserRepository`
- [ ] Create `PropertyImageRepository`
- [ ] Create `PropertyDocumentRepository`
- [ ] Configure PostgreSQL in `application.yml`
- [ ] Set up connection pooling
- [ ] Create database indexes for performance

---

### ⏳ **Phase 3: Service Layer**
**Status**: 🔴 Not Started  
**Priority**: High

**Tasks**:
- [ ] `PropertyService` interface & implementation
  - CRUD operations
  - Search, filter, pagination logic
- [ ] `BrokerService` interface & implementation
- [ ] `FileStorageService` for image/document uploads
- [ ] `SearchService` for advanced search & filters
- [ ] Create DTOs (PropertyRequestDTO, PropertyResponseDTO)
- [ ] Create Mappers (Entity ↔ DTO)
- [ ] Exception handling (ResourceNotFoundException, etc.)

---

### ⏳ **Phase 4: Admin Portal**
**Status**: 🔴 Not Started  
**Priority**: Medium

**Tasks**:
- [ ] `AdminController` for dashboard
- [ ] `PropertyController` for CRUD operations
- [ ] Thymeleaf templates:
  - Admin dashboard with statistics
  - Property creation/edit form
  - Property list with pagination
  - Bulk upload interface (CSV/Excel)
- [ ] File upload handling (images, documents)
- [ ] Form validation
- [ ] Success/error messages
- [ ] Basic authentication (Spring Security)

---

### ⏳ **Phase 5: Public Front-End**
**Status**: 🔴 Not Started  
**Priority**: Medium

**Tasks**:
- [ ] `PublicPropertyController` for listing & detail pages
- [ ] Thymeleaf templates:
  - Property listing (grid/list view)
  - Property detail page with gallery
  - Search page with filters
  - Broker contact form
- [ ] Responsive design (Bootstrap)
- [ ] Search & filter UI (location, type, price range)
- [ ] Pagination for listings
- [ ] Share functionality (generate URLs)

---

### ⏳ **Phase 6: Advanced Features**
**Status**: 🔴 Not Started  
**Priority**: Low

**Tasks**:
- [ ] Map integration (Google Maps / Leaflet)
- [ ] Session-based favorites (no login required)
- [ ] Contact broker form with email notification
- [ ] SEO optimization:
  - Dynamic meta tags
  - Sitemap generation
  - Canonical URLs
  - OpenGraph tags for sharing
- [ ] Analytics integration (Google Analytics)
- [ ] Performance optimization:
  - Image lazy loading
  - Caching strategies
  - CDN for static assets
- [ ] Accessibility (AA compliance)

---

## 📊 Feature Implementation Checklist

### 🔐 Admin Features
- [ ] Role-based secure login (Spring Security)
- [ ] Create/edit/delete property listings
- [ ] Image/document/video management
- [ ] Bulk uploads via CSV/Excel
- [ ] Dashboard with activity tracking & analytics
- [ ] Automated SEO metadata generation
- [ ] Email notifications for inquiries
- [ ] Preview listings before publishing

### 👥 Buyer Features
- [ ] Interactive map view with property markers
- [ ] Satellite view toggle
- [ ] Advanced filters (location, type, price, bedrooms)
- [ ] Save favorites (session-based, no login)
- [ ] View broker details & ratings
- [ ] Contact broker via form submission
- [ ] Share listings via direct links
- [ ] Responsive design (mobile, tablet, desktop)
- [ ] Property comparison tool

### ⚡ Technical Features
- [ ] SEO-optimized URLs (e.g., `/property/villa-in-bangalore-123`)
- [ ] Dynamic metadata for each listing
- [ ] XML sitemap generation
- [ ] Fast page load times (<2s)
- [ ] Image optimization & compression
- [ ] Browser caching
- [ ] WCAG AA accessibility compliance
- [ ] Analytics integration (visitor tracking)
- [ ] Error handling & logging

---

## 📦 Built Components

### ✅ Entities
*None built yet - Starting Phase 1*

### ✅ Repositories
*Pending*

### ✅ Services
*Pending*

### ✅ Controllers
*Pending*

### ✅ Templates
*Pending*

---

## 📝 Development Notes

- **Lombok**: Used to reduce boilerplate (getters, setters, constructors)
- **PostgreSQL**: Production-grade relational database
- **Thymeleaf**: Server-side rendering for SEO benefits
- **Bootstrap**: Responsive UI framework
- **Spring Security**: Role-based access control
- **File Storage**: Local filesystem (can be upgraded to S3)

---

## 🎯 Current Focus

**Phase**: Phase 1 - Core Entity Models  
**Next Task**: Create enums and Property entity  
**Last Updated**: *Today*

---

## 🔄 Version History

| Date | Phase | Changes |
|------|-------|---------|
| Today | Setup | Initial project structure created |

---

*This document will be updated as each component is built.*