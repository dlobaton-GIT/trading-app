# **Trading App**

Trading App provides basic CRUD operations for a trading service


### *Before you start...*
- This application provides embedded database: H2 database with reactive relational database driver (R2DBC). Therefore, no database service needs to be launched independently.
  > Database schema defined in file /src/main/resources/**schema.sql**

  > Database initial data defined in file /src/main/resources/**data.sql**
- All functionalities have been implemented in a reactive basis, included tests. In this way, external consumers are encouraged to consume the microservice in a reactive way.

### Entities

| Entity    | Attributes                                    | Notes        |
|-----------|-----------------------------------------------|--------------|
| Security  | id, name                                      |              |
| Users      | id, username, password                        |  Named as 'Users' because 'User' is a reserved word in JAVA and SQL            |
| Orders     | id, user_id, security_id, type, price, quantity, full_filled |     Named as 'Orders' because 'Order' is a reserved word in JAVA and SQL         |
| Trade     | id, sell_order_id, buy_order_id, price, quantity  |              |

> At the start of the application, 3 users are created automatically, which is enough to test all the functionalities. For this reason, no methods for creating users are provided. See user data for test purposes:

| User ID | Username | Password |
|---------|----------|----------|
| 1       | Diamond  | admin    |
| 2       | Paper    | admin    |
| 3       | Iron     | admin    |

### Build

1. Clone the repository from GitHub.
2. Navigate to the project directory.
3. Build the project using Maven.

### Start

1. Deploy the built application on the server. e.g.: localhost

### Usage
1. Open the API user-interface http://localhost:9150/swagger-ui/index.html#/
2. Use REST API endpoints to create and manage entities (securities, users, orders, trades).
3. Refer to the API documentation available in the Swagger/OpenAPI interface for detailed information about available endpoints and their functionalities.
4. Test the user story scenario to ensure the platform's functionality.


## User Story: Example

- **Given**:
  - A security named "WSB" exists.
  - Two users, "Diamond" and "Paper," are participating in the trading.

- **When**:
  - User "Diamond" places a buy order for 50 units of security "WSB" at a price of $101 per unit.
- **And**:
  - User "Paper" places a sell order for 100 units of security "WSB" at a price of $100 per unit.

- **Then**:
  - A trade occurs between "Diamond" and "Paper" for security "WSB."
  - The trade executes at a price of $100 per unit.
  - The quantity traded is 50 units of "WSB."

