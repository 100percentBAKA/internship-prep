package com.adarshgs.demo.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name of the book cannot be blank")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Publisher publisher;
    // default column name of publisher_id is given
    // this is because Publisher is the name of the entity
}
