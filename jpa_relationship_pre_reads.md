# JPA Relationship Mappings with Examples

## 1️⃣ One-to-One (`@OneToOne`)

### Scenario: **An Employee has one Desk**

Each employee is assigned exactly one desk, and each desk is assigned to one employee.

### Table Structure:

| employee_id | name  | desk_id |
| ----------- | ----- | ------- |
| 1           | Alice | 101     |
| 2           | Bob   | 102     |

| desk_id | location |
| ------- | -------- |
| 101     | Floor 1  |
| 102     | Floor 2  |

### Entity Classes:

#### **Unidirectional Mapping (Simple)**

```java
@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "desk_id") // Foreign key in Employee table
    private Desk desk;
}
```

```java
@Entity
public class Desk {
    @Id @GeneratedValue
    private Long id;
    private String location;
}
```

#### **Bidirectional Mapping**

```java
@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;
}
```

```java
@Entity
public class Desk {
    @Id @GeneratedValue
    private Long id;
    private String location;

    @OneToOne(mappedBy = "desk")
    private Employee employee;
}
```

---

## 2️⃣ One-to-Many (`@OneToMany`)

### Scenario: **A Department has multiple Employees**

One department can have many employees, but an employee belongs to only one department.

### Table Structure:

| department_id | name |
| ------------- | ---- |
| 1             | HR   |
| 2             | IT   |

| employee_id | name    | department_id |
| ----------- | ------- | ------------- |
| 1           | Alice   | 1 (HR)        |
| 2           | Bob     | 1 (HR)        |
| 3           | Charlie | 2 (IT)        |

### Entity Classes:

#### **Unidirectional Mapping (Simple)**

```java
@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id") // Foreign key in Employee table
    private Department department;
}
```

```java
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;
    private String name;
}
```

#### **Bidirectional Mapping**

```java
@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
```

```java
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
}
```

---

## 3️⃣ Many-to-One (`@ManyToOne`)

### Scenario: **Multiple Employees work in one Company**

Many employees belong to the same company.

### Table Structure:

| company_id | name   |
| ---------- | ------ |
| 1          | Google |
| 2          | Amazon |

| employee_id | name    | company_id |
| ----------- | ------- | ---------- |
| 1           | Alice   | 1 (Google) |
| 2           | Bob     | 1 (Google) |
| 3           | Charlie | 2 (Amazon) |

### Entity Classes:

#### **Unidirectional Mapping (Simple)**

```java
@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id") // Foreign key in Employee table
    private Company company;
}
```

```java
@Entity
public class Company {
    @Id @GeneratedValue
    private Long id;
    private String name;
}
```

---

## 4️⃣ Many-to-Many (`@ManyToMany`)

### Scenario: **Students enroll in multiple Courses**

A student can enroll in multiple courses, and a course can have multiple students.

### Table Structure:

| student_id | name  |
| ---------- | ----- |
| 1          | Alice |
| 2          | Bob   |

| course_id | name    |
| --------- | ------- |
| 101       | Math    |
| 102       | Science |

| student_id | course_id  |
| ---------- | ---------- |
| 1          | 101 (Math) |
| 1          | 102 (Sci)  |
| 2          | 101 (Math) |

### Entity Classes:

#### **Unidirectional Mapping (Simple)**

```java
@Entity
public class Student {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```

```java
@Entity
public class Course {
    @Id @GeneratedValue
    private Long id;
    private String name;
}
```

#### **Bidirectional Mapping**

```java
@Entity
public class Student {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList<>();
}
```

```java
@Entity
public class Course {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();
}
```

---

## Which One Should You Use?

- ✅ If **one-to-one** (e.g., Employee-Desk), use `@OneToOne`.
- ✅ If **one-to-many** (e.g., Department-Employees), use `@ManyToOne` (simpler) or `@OneToMany` (if needed).
- ✅ If **many-to-many** (e.g., Students-Courses), use `@ManyToMany`.

Since you prefer **simpler, unidirectional** mappings, I recommend using `@ManyToOne` for most cases. You can add bidirectional mappings later when needed.
