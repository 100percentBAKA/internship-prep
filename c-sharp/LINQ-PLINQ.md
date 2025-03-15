# LINQ and PLINQ Guide

## Table of Contents

- [LINQ (Language Integrated Query)](#linq-language-integrated-query)
  - [Query Syntax vs Method Syntax](#query-syntax-vs-method-syntax)
  - [Common LINQ Methods](#common-linq-methods)
  - [Best Practices](#best-practices)
- [PLINQ (Parallel LINQ)](#plinq-parallel-linq)
  - [When to Use PLINQ](#when-to-use-plinq)
  - [When Not to Use PLINQ](#when-not-to-use-plinq)
  - [Exception Handling](#exception-handling)
  - [Performance Considerations](#performance-considerations)

## LINQ (Language Integrated Query)

LINQ is a powerful feature in C# that enables you to query and manipulate data from different sources using a consistent syntax.

### Query Syntax vs Method Syntax

#### Query Syntax

```csharp
var numbers = new[] { 1, 2, 3, 4, 5 };

// Query syntax
var evenNumbers = from num in numbers
                 where num % 2 == 0
                 select num;
```

#### Method Syntax

```csharp
var numbers = new[] { 1, 2, 3, 4, 5 };

// Method syntax
var evenNumbers = numbers.Where(num => num % 2 == 0);
```

### Common LINQ Methods and Real-World Examples

#### 1. Where (Filtering)

```csharp
// Basic filtering
var numbers = new[] { 1, 2, 3, 4, 5 };
var evenNumbers = numbers.Where(n => n % 2 == 0); // Returns: { 2, 4 }

// Complex object filtering
var students = new[] {
    new { Name = "John", Age = 20, Grade = 85 },
    new { Name = "Alice", Age = 22, Grade = 92 },
    new { Name = "Bob", Age = 21, Grade = 78 }
};

// Multiple conditions
var honorsStudents = students.Where(s => s.Age < 22 && s.Grade >= 85);
// Returns students under 22 with grades 85 or higher
```

#### 2. Select (Projection)

```csharp
// Basic projection
var numbers = new[] { 1, 2, 3 };
var doubled = numbers.Select(n => n * 2); // Returns: { 2, 4, 6 }

// Object transformation
var users = new[] {
    new { FullName = "John Smith", Email = "john@example.com" },
    new { FullName = "Jane Doe", Email = "jane@example.com" }
};

var userDtos = users.Select(u => new {
    Name = u.FullName.Split(' ')[0], // Get first name
    EmailDomain = u.Email.Split('@')[1] // Get email domain
});
// Returns: { { Name = "John", EmailDomain = "example.com" }, ... }

// Index-aware projection
var numbered = numbers.Select((num, index) =>
    $"Item {index + 1}: {num}");
// Returns: { "Item 1: 1", "Item 2: 2", "Item 3: 3" }
```

#### 3. OrderBy/OrderByDescending

```csharp
var fruits = new[] { "apple", "banana", "cherry" };
var ordered = fruits.OrderBy(f => f); // Returns: { "apple", "banana", "cherry" }
var descending = fruits.OrderByDescending(f => f); // Returns: { "cherry", "banana", "apple" }
```

#### 4. GroupBy

```csharp
var pets = new[] {
    new { Name = "Rover", Type = "Dog" },
    new { Name = "Whiskers", Type = "Cat" },
    new { Name = "Spot", Type = "Dog" }
};

var groupedPets = pets.GroupBy(p => p.Type);
```

#### 5. First/FirstOrDefault

```csharp
var numbers = new[] { 1, 2, 3 };
var firstNumber = numbers.First(); // Returns: 1
var firstEven = numbers.FirstOrDefault(n => n % 2 == 0); // Returns: 2
```

#### 6. Any/All

```csharp
var numbers = new[] { 1, 2, 3 };
var hasEven = numbers.Any(n => n % 2 == 0); // Returns: true
var allEven = numbers.All(n => n % 2 == 0); // Returns: false
```

#### 7. Join

```csharp
var categories = new[] {
    new { Id = 1, Name = "Electronics" },
    new { Id = 2, Name = "Books" }
};

var products = new[] {
    new { Id = 1, CategoryId = 1, Name = "Laptop" },
    new { Id = 2, CategoryId = 2, Name = "C# Guide" }
};

var joined = products.Join(
    categories,
    p => p.CategoryId,
    c => c.Id,
    (p, c) => new { ProductName = p.Name, CategoryName = c.Name }
);
```

### Common Scenarios and Best Practices

#### String Operations

```csharp
// Working with strings
var sentences = new[] {
    "Hello World",
    "LINQ is Great",
    "C# Programming"
};

// Case-insensitive search
var containsWorld = sentences
    .Where(s => s.IndexOf("world", StringComparison.OrdinalIgnoreCase) >= 0);

// String manipulation
var words = sentences
    .SelectMany(s => s.Split(' '))
    .Select(w => w.ToLower())
    .Distinct()
    .OrderBy(w => w);

// Word frequency count
var wordFrequency = sentences
    .SelectMany(s => s.Split(' '))
    .GroupBy(w => w.ToLower())
    .Select(g => new { Word = g.Key, Count = g.Count() });
```

#### Nested Queries

```csharp
// Complex data structure
var departments = new[] {
    new {
        Name = "IT",
        Employees = new[] {
            new { Name = "John", Skills = new[] { "C#", "SQL", "JavaScript" } },
            new { Name = "Alice", Skills = new[] { "Python", "Java", "C#" } }
        }
    },
    new {
        Name = "HR",
        Employees = new[] {
            new { Name = "Bob", Skills = new[] { "Communication", "Recruiting" } }
        }
    }
};

// Find employees with specific skills
var csharpDevs = departments
    .SelectMany(d => d.Employees)
    .Where(e => e.Skills.Contains("C#"))
    .Select(e => new {
        Employee = e.Name,
        Department = departments.First(d =>
            d.Employees.Contains(e)).Name
    });

// Skill distribution across departments
var skillsByDepartment = departments
    .Select(d => new {
        Department = d.Name,
        Skills = d.Employees
            .SelectMany(e => e.Skills)
            .Distinct()
            .OrderBy(s => s)
    });
```

#### Troubleshooting Common Issues

```csharp
// 1. Multiple enumeration pitfall
var query = numbers.Where(n => n > 5); // Query is not executed yet

// Bad: Enumerates twice
Console.WriteLine($"Count: {query.Count()}");
foreach (var num in query) { } // Enumerates again

// Good: Store results when needed multiple times
var results = query.ToList(); // Executes once
Console.WriteLine($"Count: {results.Count}");
foreach (var num in results) { }

// 2. Null reference handling
var nullableList = GetPossiblyNullList();

// Bad: Might throw NullReferenceException
var badQuery = nullableList.Where(x => x.Length > 5);

// Good: Safe null handling
var goodQuery = (nullableList ?? Enumerable.Empty<string>())
    .Where(x => x?.Length > 5);

// 3. Performance with large collections
// Bad: Inefficient for large collections
var largeList = Enumerable.Range(1, 1000000);
if (largeList.Contains(999999)) { } // Searches entire collection

// Good: Use HashSet for better performance
var hashSet = new HashSet<int>(largeList);
if (hashSet.Contains(999999)) { } // O(1) lookup
```

### Best Practices

1. **Deferred Execution**

```csharp
// Query is defined but not executed
var query = numbers.Where(n => n % 2 == 0);

// Query is executed when enumerated
foreach (var num in query) { }
```

2. **Multiple Iterations**

```csharp
// Bad: Multiple iterations
if (numbers.Count() > 0 && numbers.First() == 1) { }

// Good: Single iteration
var firstNum = numbers.FirstOrDefault();
if (firstNum != null && firstNum == 1) { }
```

3. **Null Checking**

```csharp
// Always check for null when using First()
try {
    var first = numbers.First();
} catch (InvalidOperationException) {
    // Handle empty sequence
}

// Better: Use FirstOrDefault()
var first = numbers.FirstOrDefault();
if (first != null) { }
```

## PLINQ (Parallel LINQ)

PLINQ is a parallel implementation of LINQ that improves performance by utilizing multiple processors.

### When to Use PLINQ

1. **Large Data Sets**

```csharp
var numbers = Enumerable.Range(1, 1000000);
var evenNumbers = numbers.AsParallel()
                        .Where(n => n % 2 == 0)
                        .ToList();
```

2. **Computationally Intensive Operations**

```csharp
public class ComplexCalculation
{
    public static double ExpensiveOperation(int n)
    {
        // Simulate complex calculation
        Thread.Sleep(100);
        return Math.Pow(n, 2);
    }

    public static void ProcessData()
    {
        var numbers = Enumerable.Range(1, 100);
        var results = numbers.AsParallel()
                            .Select(n => ExpensiveOperation(n))
                            .ToList();
    }
}
```

3. **Independent Operations**

```csharp
var urls = new[] { "url1", "url2", "url3" };
var contents = urls.AsParallel()
                   .Select(url => DownloadContent(url))
                   .ToList();
```

### When Not to Use PLINQ

1. **Small Data Sets**

```csharp
// Don't use PLINQ for small collections
var numbers = Enumerable.Range(1, 10);
var result = numbers.Where(n => n % 2 == 0); // Use regular LINQ
```

2. **Operations with Side Effects**

```csharp
// Bad: Side effects in parallel operations
var numbers = Enumerable.Range(1, 1000);
var sharedList = new List<int>();

numbers.AsParallel().ForAll(n => {
    sharedList.Add(n); // Unsafe: concurrent access to shared list
});

// Good: Use thread-safe collections or avoid side effects
var results = numbers.AsParallel()
                     .Select(n => n * 2)
                     .ToList(); // Thread-safe operation
```

### Exception Handling

1. **AggregateException Handling**

```csharp
try {
    var numbers = Enumerable.Range(1, 100);
    var results = numbers.AsParallel()
                        .Select(n => {
                            if (n % 10 == 0) throw new Exception($"Error at {n}");
                            return n * 2;
                        })
                        .ToList();
} catch (AggregateException ae) {
    foreach (var ex in ae.InnerExceptions) {
        Console.WriteLine($"Error: {ex.Message}");
    }
}
```

2. **Handling Specific Exceptions**

```csharp
try {
    var results = collection.AsParallel()
                           .WithDegreeOfParallelism(4)
                           .Select(item => ProcessItem(item))
                           .ToList();
} catch (AggregateException ae) {
    ae.Handle(ex => {
        if (ex is ArgumentException) {
            Console.WriteLine("Argument error: " + ex.Message);
            return true; // Exception handled
        }
        return false; // Rethrow other exceptions
    });
}
```

### Performance Considerations

1. **Controlling Parallelism**

```csharp
// Control degree of parallelism
var results = source.AsParallel()
                   .WithDegreeOfParallelism(Environment.ProcessorCount)
                   .Select(item => ProcessItem(item))
                   .ToList();
```

2. **Ordering**

```csharp
// Maintaining order (slower)
var orderedResults = source.AsParallel()
                          .AsOrdered()
                          .Select(item => ProcessItem(item))
                          .ToList();

// Without ordering (faster)
var unorderedResults = source.AsParallel()
                            .Select(item => ProcessItem(item))
                            .ToList();
```

3. **Partitioning**

```csharp
// Custom partitioning for better performance
var customPartitioner = Partitioner.Create(largeArray, true);
var results = customPartitioner.AsParallel()
                              .Select(item => ProcessItem(item))
                              .ToList();
```

4. **Cancellation Support**

```csharp
using var cts = new CancellationTokenSource();

try {
    var results = source.AsParallel()
                        .WithCancellation(cts.Token)
                        .Select(item => {
                            if (ShouldCancel())
                                cts.Cancel();
                            return ProcessItem(item);
                        })
                        .ToList();
} catch (OperationCanceledException) {
    Console.WriteLine("Operation was cancelled");
}
```
