# store
1. Facebook Login/Signup
2. Open Addresses Address Validation
3. MapQuest Driving Distance
4. Stripe Payments

# sample requests

1. Facebook Login
GET localhost:8080/login/facebook

2. SignUp Or Login
GET localhost:8080/customer

3. Add Credit Card
POST localhost:8080/paymentmethods
{
    "data": {
        "exp_month": "10", 
        "exp_year": "2020", 
        "number": "4242424242424242", 
        "cvc": "876", 
        "name": "Hrishikesh Paranjape"
    }, 
    "type": "card"
}

4. Create Order
POST localhost:8080/orders
