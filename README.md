# Inventory Management System (MERN)

A full-stack inventory management system built with **MongoDB**, **Express**, **React**, and **Node.js**.

## Stack

- **Frontend**: React (Vite) — [client/](client/)
- **Backend**: Express + Mongoose — [server/](server/)
- **Database**: MongoDB
- **Auth**: JWT + bcrypt password hashing

## Project Structure

```
server/
  models/        # Mongoose schemas (User, Category, Supplier, Product, Order)
  routes/        # Express routes (auth, products, categories, suppliers, orders)
  middleware/    # JWT auth middleware
  server.js      # App entry point
  seed.js        # Seeds demo users/categories/suppliers/products

client/
  src/pages/     # Login, Dashboard, Products
  src/api.js     # Axios instance with JWT interceptor
  src/AuthContext.jsx

docker-compose.yml  # mongo + server + client (nginx), for local or EC2 use
```

## Local Development (without Docker)

**1. Start MongoDB** (locally installed, or `docker run -p 27017:27017 mongo:7`)

**2. Backend**
```
cd server
cp .env.example .env   # edit JWT_SECRET
npm install
npm run seed            # creates demo users + sample data
npm run dev
```

**3. Frontend**
```
cd client
npm install
npm run dev
```
Vite proxies `/api` requests to `http://localhost:8080` (see `client/vite.config.js`).

Visit `http://localhost:5173` and log in with:

| Username | Password  |
|----------|-----------|
| admin    | admin123  |
| manager1 | admin123  |
| staff1   | admin123  |

## Running with Docker Compose

```
cp .env.example .env   # edit JWT_SECRET
docker compose up -d --build
docker compose exec server npm run seed
```
Visit `http://localhost`.

## API

All routes except `/api/auth/*` require `Authorization: Bearer <token>`.

- `POST /api/auth/login`, `POST /api/auth/register`, `POST /api/auth/logout`
- `GET /api/products?action=getAll|getById|search|lowStock|stats`
- `POST /api/products`, `PUT /api/products?id=`, `DELETE /api/products?id=`
- `GET /api/categories`, `POST /api/categories`
- `GET /api/suppliers`, `POST /api/suppliers`
- `GET /api/orders`, `POST /api/orders`
