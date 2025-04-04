# PySpark DataFrame API Reference Guide

## Table of Contents
- [Introduction](#introduction)
- [Setting Up PySpark Environment](#setting-up-pyspark-environment)
- [Creating DataFrames](#creating-dataframes)
- [Exploring DataFrames](#exploring-dataframes)
- [Data Selection and Filtering](#data-selection-and-filtering)
- [Data Transformation](#data-transformation)
- [Aggregations](#aggregations)
- [Window Functions](#window-functions)
- [Joins](#joins)
- [Set Operations](#set-operations)
- [Sorting and Ordering](#sorting-and-ordering)
- [User-Defined Functions (UDFs)](#user-defined-functions-udfs)
- [Writing Data](#writing-data)
- [Performance Optimization](#performance-optimization)
- [Common Patterns and Recipes](#common-patterns-and-recipes)
  - [Handling Skewed Data](#handling-skewed-data)
  - [Complex Data Types](#complex-data-types)
  - [Common Time Series Operations](#common-time-series-operations)
  - [Error Handling and Debugging](#error-handling-and-debugging)
- [Tips for Working with Large Datasets](#tips-for-working-with-large-datasets)

## Introduction

This comprehensive guide covers essential operations for data processing tasks using PySpark's DataFrame API, organized for efficient reference when working with large datasets.

## Setting Up PySpark Environment

```python
# Import necessary libraries
from pyspark.sql import SparkSession
from pyspark.sql import functions as F
from pyspark.sql import types as T
from pyspark.sql.window import Window

# Create a SparkSession
spark = SparkSession.builder \
    .appName("Your Application Name") \
    .config("spark.some.config.option", "some-value") \
    .getOrCreate()

# Set log level (optional)
spark.sparkContext.setLogLevel("WARN")
```

## Creating DataFrames

```python
# From existing RDD
rdd = spark.sparkContext.parallelize([(1, "John"), (2, "Jane")])
df = spark.createDataFrame(rdd, ["id", "name"])

# From Lists
data = [(1, "John", 30), (2, "Jane", 25)]
df = spark.createDataFrame(data, ["id", "name", "age"])

# From Pandas DataFrame
import pandas as pd
pandas_df = pd.DataFrame({"id": [1, 2], "name": ["John", "Jane"]})
df = spark.createDataFrame(pandas_df)

# Reading from files
df_csv = spark.read.csv("path/to/file.csv", header=True, inferSchema=True)
df_json = spark.read.json("path/to/file.json")
df_parquet = spark.read.parquet("path/to/file.parquet")
df_orc = spark.read.orc("path/to/file.orc")
df_text = spark.read.text("path/to/file.txt")

# Reading from database
df_jdbc = spark.read \
    .format("jdbc") \
    .option("url", "jdbc:postgresql://host:port/database") \
    .option("dbtable", "schema.table") \
    .option("user", "username") \
    .option("password", "password") \
    .load()
```

## Exploring DataFrames

```python
# Basic DataFrame inspection
df.show()  # Display first 20 rows
df.show(n=5, truncate=False)  # Show 5 rows without truncating
df.printSchema()  # Print schema
df.count()  # Number of rows
df.columns  # List column names
df.dtypes  # List column names and data types

# DataFrame statistics
df.describe().show()  # Statistical summary
df.summary().show()   # More detailed summary

# Sampling
sample_df = df.sample(fraction=0.1, seed=42)
```

## Data Selection and Filtering

```python
# Select columns
df.select("name", "age").show()
df.select(df["name"], df["age"] + 1).show()  # With expressions

# Using column expressions
from pyspark.sql.functions import col
df.select(col("name"), col("age") + 1).show()

# Filter rows
df.filter(df["age"] > 25).show()
df.filter("age > 25").show()  # SQL expression
df.where(df["age"] > 25).show()  # Alternative to filter

# Handling NULL values
df.filter(df["age"].isNull()).show()
df.filter(df["age"].isNotNull()).show()

# Distinct values
df.select("department").distinct().show()
df.dropDuplicates(["name", "department"]).show()  # Distinct based on subset of columns

# Limiting results
df.limit(10).show()
```

## Data Transformation

```python
# Adding new columns
df = df.withColumn("age_plus_one", df["age"] + 1)
df = df.withColumn("adult", F.when(df["age"] >= 18, "Yes").otherwise("No"))

# Renaming columns
df = df.withColumnRenamed("age", "years")

# Dropping columns
df = df.drop("age_plus_one")

# Casting data types
df = df.withColumn("age", df["age"].cast(T.IntegerType()))

# Working with strings
df = df.withColumn("upper_name", F.upper(df["name"]))
df = df.withColumn("name_length", F.length(df["name"]))
df = df.withColumn("first_char", F.substring(df["name"], 1, 1))
df = df.withColumn("trimmed", F.trim(df["name"]))
df = df.withColumn("concatenated", F.concat(df["name"], F.lit(" - "), df["department"]))

# Date functions
df = df.withColumn("current_date", F.current_date())
df = df.withColumn("current_timestamp", F.current_timestamp())
df = df.withColumn("date_add", F.date_add(df["date_col"], 1))
df = df.withColumn("year", F.year(df["date_col"]))
df = df.withColumn("month", F.month(df["date_col"]))
df = df.withColumn("day", F.dayofmonth(df["date_col"]))

# Handling nulls
df = df.withColumn("cleaned_col", F.coalesce(df["col1"], df["col2"], F.lit("default")))
df = df.na.fill(0, ["age"])  # Fill nulls with 0 in age column
df = df.na.fill({"age": 0, "name": "Unknown"})  # Fill multiple columns
df = df.na.drop()  # Drop rows with any null values
df = df.na.drop(subset=["age", "name"])  # Drop rows with nulls in specific columns
```

## Aggregations

```python
# Simple aggregations
df.select(F.max("age"), F.min("age"), F.avg("age")).show()

# GroupBy operations
df.groupBy("department").count().show()
df.groupBy("department").agg(F.avg("salary").alias("avg_salary")).show()

# Multiple aggregations
df.groupBy("department").agg(
    F.count("*").alias("count"),
    F.sum("salary").alias("total_salary"),
    F.avg("salary").alias("avg_salary"),
    F.min("salary").alias("min_salary"),
    F.max("salary").alias("max_salary")
).show()

# Common aggregation functions
# F.sum(), F.count(), F.avg(), F.min(), F.max(), F.countDistinct(),
# F.sumDistinct(), F.stddev(), F.variance()

# Pivot tables
df.groupBy("department").pivot("country").sum("salary").show()
```

## Window Functions

```python
# Define a window specification
windowSpec = Window.partitionBy("department").orderBy("salary")

# Rank employees by salary within each department
df = df.withColumn("rank", F.rank().over(windowSpec))
df = df.withColumn("dense_rank", F.dense_rank().over(windowSpec))
df = df.withColumn("row_number", F.row_number().over(windowSpec))

# Calculate cumulative and moving aggregations
df = df.withColumn("cumulative_salary", F.sum("salary").over(windowSpec))
df = df.withColumn("moving_avg", F.avg("salary").over(
    Window.partitionBy("department").orderBy("date").rowsBetween(-2, 0)
))

# Get values from previous/next rows
df = df.withColumn("prev_salary", F.lag("salary", 1).over(windowSpec))
df = df.withColumn("next_salary", F.lead("salary", 1).over(windowSpec))
```

## Joins

```python
# Different join types
joined_df = df1.join(df2, df1["id"] == df2["id"], "inner")  # Inner join
joined_df = df1.join(df2, df1["id"] == df2["id"], "left")   # Left outer join
joined_df = df1.join(df2, df1["id"] == df2["id"], "right")  # Right outer join
joined_df = df1.join(df2, df1["id"] == df2["id"], "full")   # Full outer join
joined_df = df1.join(df2, df1["id"] == df2["id"], "leftsemi")  # Left semi join
joined_df = df1.join(df2, df1["id"] == df2["id"], "leftanti")  # Left anti join

# Join on multiple conditions
joined_df = df1.join(df2, 
                     (df1["id"] == df2["id"]) & (df1["dept"] == df2["dept"]), 
                     "inner")

# Cross join / Cartesian product
cross_joined_df = df1.crossJoin(df2)
```

## Set Operations

```python
# Union (combines and keeps duplicates)
union_df = df1.union(df2)

# Union by name (matches columns by name, not position)
union_df = df1.unionByName(df2)

# Union by name with schema resolution
union_df = df1.unionByName(df2, allowMissingColumns=True)

# Intersect (common rows)
intersect_df = df1.intersect(df2)

# Except/Minus (rows in df1 but not in df2)
except_df = df1.exceptAll(df2)
```

## Sorting and Ordering

```python
# Sort by a single column
sorted_df = df.orderBy("age")
sorted_df = df.orderBy(df["age"].asc())  # Ascending
sorted_df = df.orderBy(df["age"].desc())  # Descending

# Sort by multiple columns
sorted_df = df.orderBy("department", F.col("salary").desc())
```

## User-Defined Functions (UDFs)

```python
# Define a Python function
def celsius_to_fahrenheit(celsius):
    return (celsius * 9/5) + 32

# Register UDF
celsius_to_fahrenheit_udf = F.udf(celsius_to_fahrenheit, T.DoubleType())

# Apply UDF
df = df.withColumn("temp_fahrenheit", celsius_to_fahrenheit_udf(df["temp_celsius"]))

# Pandas UDFs (vectorized UDFs - much faster)
from pyspark.sql.functions import pandas_udf
from pyspark.sql.types import DoubleType
import pandas as pd

@pandas_udf(DoubleType())
def pandas_celsius_to_fahrenheit(celsius: pd.Series) -> pd.Series:
    return (celsius * 9/5) + 32

df = df.withColumn("temp_fahrenheit", pandas_celsius_to_fahrenheit(df["temp_celsius"]))
```

## Writing Data

```python
# Save as Parquet (recommended for performance)
df.write.parquet("path/to/output.parquet")

# Save as CSV
df.write.csv("path/to/output.csv", header=True)

# Save as JSON
df.write.json("path/to/output.json")

# Save to database
df.write \
    .format("jdbc") \
    .option("url", "jdbc:postgresql://host:port/database") \
    .option("dbtable", "schema.table") \
    .option("user", "username") \
    .option("password", "password") \
    .mode("append")  # or "overwrite", "ignore", "error"
    .save()

# Partition by columns (for better query performance)
df.write.partitionBy("year", "month").parquet("path/to/partitioned_output")

# Save with specific options
df.write \
    .format("parquet") \
    .mode("overwrite") \
    .option("compression", "snappy") \
    .save("path/to/output_with_options")
```

## Performance Optimization

```python
# Cache a DataFrame in memory
df.cache()
# Or with storage level specification
from pyspark.storagelevel import StorageLevel
df.persist(StorageLevel.MEMORY_AND_DISK)
# Release from memory
df.unpersist()

# Repartition (increase/decrease partitions)
df_repartitioned = df.repartition(10)
# Repartition by specific columns
df_repartitioned = df.repartition("department", "country")
# Coalesce (only reduces partitions - more efficient than repartition)
df_coalesced = df.coalesce(5)

# Broadcast join for small tables
from pyspark.sql.functions import broadcast
joined_df = df1.join(broadcast(df2), df1["id"] == df2["id"])
```

## Common Patterns and Recipes

### Handling Skewed Data

```python
# Salting technique for skewed keys
from pyspark.sql.functions import rand
num_salts = 10

# Add salt to the skewed column
df_salted = df.withColumn("salt", (F.rand() * num_salts).cast("int"))

# Join with salt
salted_join = df_salted.join(
    other_df,
    df_salted["key"] == other_df["key"]
)
```

### Complex Data Types

```python
# Working with arrays
df = df.withColumn("array_column", F.split(df["string_col"], ","))
df = df.withColumn("array_length", F.size(df["array_column"]))
df = df.withColumn("contains_item", F.array_contains(df["array_column"], "item"))
df = df.withColumn("first_item", df["array_column"].getItem(0))
df = df.withColumn("sorted_array", F.sort_array(df["array_column"]))

# Explode arrays (one row per array element)
df_exploded = df.select("id", F.explode("array_column").alias("single_item"))
# Explode with position
df_exploded = df.select("id", F.posexplode("array_column").alias("pos", "single_item"))

# Working with maps
df = df.withColumn("map_values", F.create_map(
    F.lit("key1"), F.col("value1"),
    F.lit("key2"), F.col("value2")
))
df = df.withColumn("key1_value", df["map_values"].getItem("key1"))
df = df.withColumn("map_keys", F.map_keys(df["map_values"]))
df = df.withColumn("map_values_only", F.map_values(df["map_values"]))

# Explode maps
df_exploded = df.select("id", F.explode("map_values").alias("key", "value"))

# Working with structs
df = df.withColumn("struct_col", F.struct("col1", "col2", "col3"))
df = df.withColumn("extracted_field", df["struct_col"]["col1"])
```

### Common Time Series Operations

```python
# Calculate time difference between events
df = df.withColumn(
    "time_diff_seconds", 
    F.unix_timestamp(df["current_time"]) - F.unix_timestamp(df["previous_time"])
)

# Group time into bins
df = df.withColumn(
    "hour_bin", 
    F.date_trunc("hour", df["timestamp"])
)

# Moving averages
windowSpec = Window.partitionBy("id").orderBy("timestamp").rowsBetween(-3, 0)
df = df.withColumn("moving_avg_4_periods", F.avg("value").over(windowSpec))

# Lag/lead calculations
windowSpec = Window.partitionBy("id").orderBy("timestamp")
df = df.withColumn("prev_value", F.lag("value", 1).over(windowSpec))
df = df.withColumn("change", df["value"] - df["prev_value"])
df = df.withColumn("pct_change", (df["value"] - df["prev_value"]) / df["prev_value"] * 100)
```

### Error Handling and Debugging

```python
# Handling exceptions in UDFs
def safe_divide(a, b):
    try:
        return a / b
    except:
        return None  # Return None for errors

safe_divide_udf = F.udf(safe_divide, T.DoubleType())

# Validate data with when/otherwise
df = df.withColumn(
    "status",
    F.when(df["age"] < 0, "Invalid age")
    .when(df["salary"] < 0, "Invalid salary")
    .otherwise("Valid")
)

# Get a sample of problematic records
problem_records = df.filter("status != 'Valid'").limit(10)
problem_records.show()

# Write execution plan for debugging
print(df.explain())  # Logical and physical plans
print(df.explain(True))  # Extended explanation
```

## Tips for Working with Large Datasets

- Use appropriate file formats (Parquet or ORC) instead of CSV/JSON for better compression and performance
- Partition your data sensibly based on frequent query patterns
- Use broadcast joins for small dataframes to avoid shuffling
- Reduce the number of transformations by combining operations
- Persist/cache intermediate results that are used multiple times
- Use appropriate data types (e.g., using IntegerType instead of StringType for numeric data)
- Consider repartitioning if your partitions are very skewed
- Use explain() to understand and optimize execution plans

Remember that PySpark is lazy in evaluation - transformations are not executed until an action (like show(), count(), or collect()) is called. This allows Spark to optimize the execution plan.
