# 🍕 Foodies - Food Delivery REST API

A comprehensive food delivery backend application built with Spring Boot, featuring user authentication, cart management, order processing, and payment integration with Stripe.

## 🚀 Features

### Core Functionality

- **User Management**: Registration, authentication, and profile management
- **Food Catalog**: Browse and manage food items with categories
- **Shopping Cart**: Add, update, and remove items from cart
- **Order Management**: Place orders and track order history
- **Payment Processing**: Secure payments via Stripe integration
- **File Upload**: AWS S3 integration for food images
- **JWT Authentication**: Secure API access with JSON Web Tokens

### Technical Features

- RESTful API design
- MongoDB database integration
- AWS S3 for file storage
- Stripe payment gateway
- JWT-based authentication
- Input validation
- Error handling
- CORS support

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.4.9
- **Language**: Java 21
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **File Storage**: AWS S3
- **Payment**: Stripe
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (Code generation)
  - Spring Security
  - Spring Data MongoDB
  - Spring Validation

## 📋 Prerequisites

Before running this application, make sure you have:

- Java 21 or higher
- Maven 3.6+
- MongoDB database
- AWS S3 bucket
- Stripe account

## 🔧 Environment Variables

Create environment variables for the following:

```bash
# MongoDB Configuration
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/foodies

# AWS S3 Configuration
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key

# JWT Configuration
JWT_SECRET_KEY=your_jwt_secret_key

# Stripe Configuration
STRIPE_API_KEY=your_stripe_api_key
STRIPE_WEBHOOK=your_stripe_webhook_secret
```

## 🚀 Getting Started

1. **Clone the repository**

   ```bash
   git clone https://github.com/CANDELY001/food_delivery_app_springboot.git
   cd food_delivery_app_springboot
   ```

2. **Set up environment variables**

   - Create a `.env` file or set environment variables as shown above

3. **Install dependencies**

   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication

#### Login

```http
POST /api/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

### Food Management

#### Get All Foods

```http
GET /api/foods
```

#### Get Food by ID

```http
GET /api/foods/{id}
```

#### Add New Food (Admin)

```http
POST /api/foods
Content-Type: multipart/form-data

food: {
  "name": "Pizza Margherita",
  "description": "Classic pizza with tomatoes and mozzarella",
  "price": 12.99,
  "category": "Pizza"
}
file: [image file]
```

#### Delete Food (Admin)

```http
DELETE /api/foods/{id}
```

### Cart Management

#### Add Item to Cart

```http
POST /api/cart
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "foodId": "food_id",
  "quantity": 2
}
```

#### Get Cart Items

```http
GET /api/cart
Authorization: Bearer {jwt_token}
```

### Order Management

#### Place Order

```http
POST /api/orders
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "items": [
    {
      "foodId": "food_id",
      "quantity": 2
    }
  ]
}
```

#### Get Order History

```http
GET /api/orders
Authorization: Bearer {jwt_token}
```

### Payment

#### Process Payment

```http
POST /api/payments
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "orderId": "order_id",
  "amount": 25.98,
  "paymentMethodId": "stripe_payment_method_id"
}
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/very/delicious/food/
│   │   ├── FoodApplication.java          # Main application class
│   │   ├── config/                       # Configuration classes
│   │   │   ├── AWSConfig.java           # AWS S3 configuration
│   │   │   ├── SecurityConfig.java      # Security configuration
│   │   │   └── StripeConfig.java        # Stripe configuration
│   │   ├── controller/                   # REST controllers
│   │   │   ├── AuthController.java      # Authentication endpoints
│   │   │   ├── CartController.java      # Cart management
│   │   │   ├── FoodController.java      # Food CRUD operations
│   │   │   ├── OrderController.java     # Order management
│   │   │   ├── PaymentController.java   # Payment processing
│   │   │   ├── StripeWebhookController.java # Stripe webhooks
│   │   │   └── UserController.java      # User management
│   │   ├── entities/                     # MongoDB entities
│   │   │   ├── CartEntity.java
│   │   │   ├── FoodEntity.java
│   │   │   ├── OrderEntity.java
│   │   │   └── UserEntity.java
│   │   ├── filters/                      # Security filters
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── io/                          # Request/Response DTOs
│   │   ├── repository/                   # MongoDB repositories
│   │   ├── service/                      # Business logic
│   │   └── util/                        # Utility classes
│   └── resources/
│       ├── application.properties        # Application configuration
│       ├── static/                      # Static resources
│       └── templates/                   # Templates
└── test/                               # Test classes
```

## 🔒 Security

- JWT-based authentication for API access
- Password encryption using Spring Security
- CORS configuration for cross-origin requests
- Input validation on all endpoints
- Secure file upload with AWS S3

## 🗄️ Database Schema

### Users Collection

```json
{
  "_id": "ObjectId",
  "name": "string",
  "email": "string",
  "password": "string (encrypted)"
}
```

### Foods Collection

```json
{
  "_id": "ObjectId",
  "name": "string",
  "description": "string",
  "imageUrl": "string",
  "price": "number",
  "category": "string"
}
```

### Orders Collection

```json
{
  "_id": "ObjectId",
  "userId": "string",
  "items": [
    {
      "foodId": "string",
      "quantity": "number",
      "price": "number"
    }
  ],
  "totalAmount": "number",
  "status": "string",
  "createdAt": "date"
}
```

### Cart Collection

```json
{
  "_id": "ObjectId",
  "userId": "string",
  "foodId": "string",
  "quantity": "number"
}
```

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

## 📦 Deployment

### Build for Production

```bash
mvn clean package
```

This creates a JAR file in the `target/` directory that can be deployed to any server with Java 21.

### Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:21-jre-slim
COPY target/food-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:

```bash
docker build -t foodies-api .
docker run -p 8080:8080 foodies-api
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**CANDELY001**

- GitHub: [@CANDELY001](https://github.com/CANDELY001)
- Repository: [food_delivery_app_springboot](https://github.com/CANDELY001/food_delivery_app_springboot)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB for the flexible database solution
- AWS for reliable cloud storage
- Stripe for secure payment processing
- Lombok for reducing boilerplate code

## 📞 Support

If you have any questions or need help with setup, please open an issue in the GitHub repository.

---

**Happy Coding! 🚀**
