package com.mj.market.app.article;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity(name="Article")
@Table(name = "article")
public class Article implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "short_description", length = 400)
    @Size(min = 10, max = 400, message = "Długość Opisu powinna się zawierać od 10 do 150 znaków")
    @NotBlank(message = "Podaj opis")
    private String shortDescription;

    @Column(name = "description", length = 3000)
    @Size(min = 10, max = 3000, message = "Długość tekstu powinna się zawierać od 10 do 3.000 znaków")
    @NotBlank(message = "Napisz treść")
    private String Description;

    @Column(name = "local_date")
    @NotNull
    private LocalDate localDate;

    public Article() {
        this.localDate = LocalDate.now();
    }

    public Article(String shortDescription, String description) {
        this.shortDescription = shortDescription;
        this.Description = description;
        this.localDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return Description;
    }

    public LocalDate getLocalDate() {

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String text = localDate.format(formatters);
        LocalDate parsedDate = LocalDate.parse(text, formatters);
        return parsedDate;
    }

    public void setLocalDate(LocalDate localDate) {

        this.localDate = localDate;
    }
    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", Description='" + Description + '\'' +
                ", localDate=" + localDate + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Article pa = (Article) o;
        //revers order
        return Long.compare(pa.getId(), this.getId());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(shortDescription, article.shortDescription) && Objects.equals(Description, article.Description) && Objects.equals(localDate, article.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortDescription, Description, localDate);
    }
}
