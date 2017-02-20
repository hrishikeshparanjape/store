# store
1. Facebook Login/Signup
2. Open Addresses Address Validation
3. MapQuest Driving Distance
4. Stripe Payments

# how to run this project
1. Install java 8 jdk, gradle, postgres 
2. Run `psql < database/new_db.sql`
3. run `gradle build`
4. run `gradle bootRun`
5. Go to localhost:5000/swagger-ui.html

# sample requests

1. Facebook Login
GET localhost:8080/login/facebook

2. Add Credit Card
POST localhost:8080/paymentmethods
{
    "data": {
        "expiryMonth": "10", 
        "expiryYear": "2020", 
        "number": "4242424242424242", 
        "cvc": "876", 
        "name": "Hrishikesh Paranjape"
    }, 
    "type": "card"
}

4. Create Order
POST localhost:8080/orders
