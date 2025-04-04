# SQL for Data Engineering and PySpark

## Table of Contents
1. [SQL Fundamentals](#sql-fundamentals)
2. [Intermediate SQL](#intermediate-sql)
3. [Advanced SQL](#advanced-sql)
4. [SQL Optimization](#sql-optimization)
5. [SQL in PySpark Context](#sql-in-pyspark-context)
6. [Practice Exercises](#practice-exercises)

## SQL Fundamentals

### Basic Query Structure
```sql
SELECT column1, column2
FROM table_name
WHERE condition;
```

### Data Types
- **Numeric**: INTEGER, BIGINT, DECIMAL, FLOAT
- **String**: VARCHAR, CHAR, TEXT
- **Date/Time**: DATE, TIME, TIMESTAMP
- **Boolean**: BOOLEAN
- **Others**: ARRAY, MAP, STRUCT (especially important for PySpark)

### Data Manipulation
- **SELECT**: Retrieve data
- **INSERT**: Add new records
- **UPDATE**: Modify existing records
- **DELETE**: Remove records
- **TRUNCATE**: Remove all records from a table

### Filtering
```sql
SELECT * FROM employees WHERE department = 'Engineering';
SELECT * FROM sales WHERE amount > 1000 AND region = 'West';
SELECT * FROM products WHERE category IN ('Electronics', 'Gadgets');
SELECT * FROM customers WHERE name LIKE 'A%';
```

### Sorting
```sql
SELECT * FROM employees ORDER BY salary DESC;
SELECT * FROM products ORDER BY category ASC, price DESC;
```

### Limiting Results
```sql
SELECT * FROM logs LIMIT 100;
```

## Intermediate SQL

### Aggregations
```sql
SELECT COUNT(*) FROM users;
SELECT department, AVG(salary) FROM employees GROUP BY department;
SELECT region, SUM(sales) as total_sales FROM transactions GROUP BY region;
SELECT product_id, MIN(price) as lowest_price FROM price_history GROUP BY product_id;
```

### Common Aggregate Functions
- COUNT(): Count records
- SUM(): Sum values
- AVG(): Calculate average
- MIN(): Find minimum value
- MAX(): Find maximum value
- STDDEV(): Calculate standard deviation
- VARIANCE(): Calculate variance

### GROUP BY with HAVING
```sql
SELECT department, AVG(salary) as avg_salary 
FROM employees 
GROUP BY department 
HAVING AVG(salary) > 50000;
```

### Joins
```sql
-- Inner Join
SELECT orders.order_id, customers.name 
FROM orders 
INNER JOIN customers ON orders.customer_id = customers.id;

-- Left Join
SELECT employees.name, departments.name as dept_name 
FROM employees 
LEFT JOIN departments ON employees.dept_id = departments.id;

-- Right Join
SELECT products.name, inventory.quantity 
FROM products 
RIGHT JOIN inventory ON products.id = inventory.product_id;

-- Full Outer Join
SELECT a.id as id_a, b.id as id_b 
FROM table_a a 
FULL OUTER JOIN table_b b ON a.id = b.id;

-- Cross Join
SELECT employees.name, locations.city 
FROM employees 
CROSS JOIN locations;
```

### Subqueries
```sql
-- Subquery in WHERE
SELECT * FROM products 
WHERE price > (SELECT AVG(price) FROM products);

-- Subquery in FROM
SELECT dept_name, avg_salary 
FROM (
    SELECT department as dept_name, AVG(salary) as avg_salary 
    FROM employees 
    GROUP BY department
) as dept_stats;

-- Correlated subquery
SELECT employee_name, 
       (SELECT department_name FROM departments 
        WHERE departments.id = employees.department_id) as dept_name
FROM employees;
```

### UNION, INTERSECT, EXCEPT
```sql
-- UNION combines results and removes duplicates
SELECT product_id FROM inventory_2023
UNION
SELECT product_id FROM inventory_2024;

-- UNION ALL combines results and keeps duplicates
SELECT product_id FROM inventory_2023
UNION ALL
SELECT product_id FROM inventory_2024;

-- INTERSECT returns only common rows
SELECT customer_id FROM active_customers
INTERSECT
SELECT customer_id FROM premium_customers;

-- EXCEPT returns rows in first query but not in second
SELECT product_id FROM all_products
EXCEPT
SELECT product_id FROM discontinued_products;
```

## Advanced SQL

### Window Functions
Window functions are extremely important for data engineering and PySpark.

```sql
-- Row number within partition
SELECT 
    customer_id, 
    order_date,
    order_amount,
    ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY order_date) as purchase_number
FROM orders;

-- Running total
SELECT 
    order_date,
    daily_sales,
    SUM(daily_sales) OVER (ORDER BY order_date) as running_total
FROM daily_sales_report;

-- Moving average
SELECT 
    date,
    temperature,
    AVG(temperature) OVER (ORDER BY date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) as weekly_avg
FROM weather_data;

-- Rank and dense_rank
SELECT 
    employee_name,
    department,
    salary,
    RANK() OVER (PARTITION BY department ORDER BY salary DESC) as salary_rank,
    DENSE_RANK() OVER (PARTITION BY department ORDER BY salary DESC) as dense_salary_rank
FROM employees;

-- Lag and lead
SELECT 
    date,
    stock_price,
    LAG(stock_price, 1) OVER (ORDER BY date) as previous_day_price,
    LEAD(stock_price, 1) OVER (ORDER BY date) as next_day_price
FROM stock_history;
```

### Common Window Functions
- ROW_NUMBER(): Assigns unique row numbers
- RANK(): Assigns rank with gaps for ties
- DENSE_RANK(): Assigns rank without gaps
- NTILE(n): Divides rows into n equal buckets
- LAG(col, n): Access data from previous rows
- LEAD(col, n): Access data from following rows
- FIRST_VALUE(col): First value in window
- LAST_VALUE(col): Last value in window

### Common Table Expressions (CTEs)
```sql
WITH revenue_by_region AS (
    SELECT region, SUM(amount) as total_revenue
    FROM sales
    GROUP BY region
),
top_regions AS (
    SELECT region, total_revenue
    FROM revenue_by_region
    ORDER BY total_revenue DESC
    LIMIT 3
)
SELECT * FROM top_regions;
```

### Recursive CTEs
```sql
WITH RECURSIVE employee_hierarchy AS (
    -- Base case
    SELECT id, name, manager_id, 1 as level
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive case
    SELECT e.id, e.name, e.manager_id, eh.level + 1
    FROM employees e
    JOIN employee_hierarchy eh ON e.manager_id = eh.id
)
SELECT * FROM employee_hierarchy;
```

### Pivot & Unpivot
```sql
-- Pivot example (implementation varies by database)
SELECT * FROM (
    SELECT product_category, quarter, sales
    FROM sales_data
) 
PIVOT (
    SUM(sales)
    FOR quarter IN ('Q1', 'Q2', 'Q3', 'Q4')
);

-- Unpivot example
SELECT product_category, quarter, sales
FROM pivoted_sales
UNPIVOT (
    sales FOR quarter IN (Q1, Q2, Q3, Q4)
);
```

### JSON Functions
```sql
-- Extract value from JSON
SELECT JSON_EXTRACT(user_data, '$.address.city') as city
FROM users;

-- Check if JSON key exists
SELECT JSON_EXISTS(properties, '$.features.premium') as has_premium
FROM subscriptions;

-- Build JSON object
SELECT JSON_OBJECT('name', first_name, 'age', age) as user_json
FROM users;
```

## SQL Optimization

### Indexing Basics
- **B-tree indexes**: General-purpose indexes
- **Hash indexes**: Equality comparisons
- **Bitmap indexes**: Low-cardinality columns
- **GIN/GiST indexes**: Full-text search and complex data types

### Query Optimization
- Use EXISTS instead of IN for large subqueries
- Avoid SELECT * when you only need specific columns
- Use EXPLAIN ANALYZE to understand query execution plan
- Be careful with functions in WHERE clauses (can prevent index usage)
- Denormalize when appropriate for analytical queries

### Materialized Views
```sql
CREATE MATERIALIZED VIEW monthly_sales AS
SELECT 
    DATE_TRUNC('month', order_date) as month,
    product_category,
    SUM(amount) as total_sales
FROM orders
JOIN products ON orders.product_id = products.id
GROUP BY 1, 2;

-- Refresh materialized view
REFRESH MATERIALIZED VIEW monthly_sales;
```

### Partitioning
```sql
CREATE TABLE sales (
    sale_id INT,
    sale_date DATE,
    amount DECIMAL(10,2)
) PARTITION BY RANGE (sale_date);

CREATE TABLE sales_2023 PARTITION OF sales
    FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');
    
CREATE TABLE sales_2024 PARTITION OF sales
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
```

## SQL in PySpark Context

### PySpark SQL Basics
```python
# Register a DataFrame as a SQL temporary view
df.createOrReplaceTempView("people")

# Run SQL queries on the view
result = spark.sql("""
    SELECT age, COUNT(*) as count
    FROM people
    WHERE age > 21
    GROUP BY age
    ORDER BY age
""")
```

### PySpark DataFrames vs SQL
```python
# DataFrame API
df.filter(df.age > 21) \
  .groupBy("age") \
  .count() \
  .orderBy("age")

# Equivalent SQL
spark.sql("""
    SELECT age, COUNT(*) as count
    FROM people
    WHERE age > 21
    GROUP BY age
    ORDER BY age
""")
```

### Catalyst Optimizer
- PySpark uses a query optimizer called Catalyst
- Understands both DataFrame operations and SQL queries
- Optimizes queries based on statistics and rules
- Creates efficient execution plans

### Window Functions in PySpark
```python
from pyspark.sql import Window
import pyspark.sql.functions as F

window_spec = Window.partitionBy("department").orderBy(F.desc("salary"))

df.withColumn("rank", F.rank().over(window_spec)) \
  .withColumn("dense_rank", F.dense_rank().over(window_spec)) \
  .withColumn("row_number", F.row_number().over(window_spec))
```

### SQL UDFs in PySpark
```python
# Register a UDF
from pyspark.sql.types import IntegerType

@spark.udf.register("double_int", IntegerType())
def double_int(x):
    return x * 2

# Use in SQL
spark.sql("SELECT id, double_int(value) FROM data")
```

### Spark SQL Catalog
```python
# List all tables
spark.catalog.listTables()

# List columns in a table
spark.catalog.listColumns("tableName")

# Cache a table
spark.catalog.cacheTable("frequently_used_table")
```

## Practice Exercises

### Basic Exercises
1. Select all employees in the Engineering department with a salary above $100,000
2. Find the average transaction amount by customer category
3. List the top 5 products by sales volume

### Intermediate Exercises
1. Calculate month-over-month growth percentage for each product category
2. Find customers who have purchased all available product categories
3. Identify transactions that are more than 2 standard deviations above average

### Advanced Exercises
1. Create a customer cohort analysis by signup month and activity month
2. Build a product recommendation query based on co-purchase patterns
3. Implement a rolling 7-day average calculation for daily metrics

### PySpark SQL Exercises
1. Convert a complex PySpark DataFrame operation chain to equivalent SQL
2. Optimize a slow-running query using appropriate techniques
3. Implement a sessionization algorithm using window functions
